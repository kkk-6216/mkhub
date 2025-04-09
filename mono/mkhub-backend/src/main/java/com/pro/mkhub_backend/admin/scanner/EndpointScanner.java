package com.pro.mkhub_backend.admin.scanner;

import com.pro.mkhub_backend.admin.dto.EndpointInfo;
import com.pro.mkhub_backend.security.config.StaticEndpointRoles;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class EndpointScanner {

    private final ApplicationContext context;

    @Getter
    private final List<EndpointInfo> endpoints = new ArrayList<>();

    @PostConstruct
    public void scanEndpoints() {
        Map<String, RequestMappingHandlerMapping> mappings = context.getBeansOfType(RequestMappingHandlerMapping.class);
        Set<String> added = new HashSet<>();

        for (var mapping : mappings.values()) {
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

            for (var entry : handlerMethods.entrySet()) {
                RequestMappingInfo info = entry.getKey();
                HandlerMethod handler = entry.getValue();

                Set<String> urls = Optional.ofNullable(info.getPathPatternsCondition())
                        .map(c -> c.getPatterns().stream()
                                .map(PathPattern::getPatternString)
                                .collect(Collectors.toSet()))
                        .orElseGet(() -> Optional.ofNullable(info.getPatternsCondition())
                                .map(p -> Set.copyOf(p.getPatterns()))
                                .orElse(Set.of()));

                Set<RequestMethod> httpMethods = info.getMethodsCondition().getMethods();

                for (String url : urls) {
                    if (httpMethods.isEmpty()) {
                        String key = url + "::ALL";
                        if (added.add(key)) {
                            Set<String> roles = collectRoles(url, null, handler);
                            endpoints.add(new EndpointInfo(url, "ALL", new ArrayList<>(roles)));
                        }
                    } else {
                        for (RequestMethod method : httpMethods) {
                            String key = url + "::" + method.name();
                            if (added.add(key)) {
                                Set<String> roles = collectRoles(url, method, handler);
                                endpoints.add(new EndpointInfo(url, method.name(), new ArrayList<>(roles)));
                            }
                        }
                    }
                }
            }
        }
    }

    private Set<String> collectRoles(String url, RequestMethod method, HandlerMethod handler) {
        Set<String> staticRoles = new HashSet<>(StaticEndpointRoles.getRoles(url, method).orElse(Collections.emptyList()));

        PreAuthorize annotation = handler.getMethodAnnotation(PreAuthorize.class);
        if (annotation == null) {
            annotation = handler.getBeanType().getAnnotation(PreAuthorize.class);
        }

        Set<String> annotationRoles = new HashSet<>();
        if (annotation != null) {
            annotationRoles = extractRolesFromExpression(annotation.value());
        }

        Set<String> combinedRoles = new HashSet<>();
        combinedRoles.addAll(staticRoles);
        combinedRoles.addAll(annotationRoles);

        // Определим, как интерпретировать роли
        if (!combinedRoles.isEmpty()) {
            // Есть конкретные роли (ROLE_ADMIN, ...), убираем AUTHENTICATED если нужно
            Set<String> actualRoles = combinedRoles.stream()
                    .filter(role -> role.startsWith("ROLE_"))
                    .collect(Collectors.toSet());

            if (!actualRoles.isEmpty()) {
                return actualRoles;
            }

            if (combinedRoles.contains("AUTHENTICATED")) {
                return Set.of("AUTHENTICATED");
            }
        }

        // Не защищён
        return Set.of("PUBLIC");
    }


    private Set<String> extractRolesFromExpression(String expression) {
        Set<String> result = new HashSet<>();
        Pattern rolePattern = Pattern.compile("hasRole\\('(.*?)'\\)|hasAnyRole\\((.*?)\\)");

        Matcher matcher = rolePattern.matcher(expression);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                result.add("ROLE_" + matcher.group(1));
            } else if (matcher.group(2) != null) {
                String[] parts = matcher.group(2).split(",");
                for (String part : parts) {
                    String role = part.replaceAll("[\"'\\s]", "");
                    if (!role.isBlank()) result.add("ROLE_" + role);
                }
            }
        }
        return result;
    }
}
