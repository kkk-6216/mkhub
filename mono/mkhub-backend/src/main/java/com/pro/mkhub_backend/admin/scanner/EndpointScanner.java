package com.pro.mkhub_backend.admin.scanner;

import com.pro.mkhub_backend.admin.dto.EndpointInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

@Component
public class EndpointScanner implements ApplicationListener<ContextRefreshedEvent> {

    private final List<EndpointInfo> endpoints = new ArrayList<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, HandlerMapping> mappings = event.getApplicationContext().getBeansOfType(HandlerMapping.class);

        for (HandlerMapping mapping : mappings.values()) {
            if (mapping instanceof RequestMappingHandlerMapping rmhm) {
                rmhm.getHandlerMethods().forEach((info, method) -> {
                    Set<String> urls = new HashSet<>();

                    // Поддержка PathPattern (Spring Boot 3+)
                    if (info.getPathPatternsCondition() != null) {
                        info.getPathPatternsCondition().getPatterns()
                                .forEach(pathPattern -> urls.add(pathPattern.getPatternString()));
                    }

                    // Поддержка старой модели (на всякий случай)
                    if (info.getPatternsCondition() != null) {
                        urls.addAll(info.getPatternsCondition().getPatterns());
                    }

                    for (String url : urls) {
                        Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
                        if (methods.isEmpty()) {
                            // Если не указано явно — считается, что поддерживаются все методы
                            endpoints.add(new EndpointInfo(url, "ALL", resolveRole(method)));
                        } else {
                            for (RequestMethod requestMethod : methods) {
                                endpoints.add(new EndpointInfo(url, requestMethod.name(), resolveRole(method)));
                            }
                        }
                    }
                });
            }
        }
    }


    private String resolveRole(HandlerMethod method) {
        if (method.hasMethodAnnotation(PreAuthorize.class)) {
            String value = method.getMethodAnnotation(PreAuthorize.class).value();
            if (value.contains("ADMIN")) return "ADMIN";
            if (value.contains("MODERATOR")) return "MODERATOR";
            if (value.contains("STUDENT")) return "STUDENT";
        }
        return "PUBLIC";
    }

    public List<EndpointInfo> getEndpoints() {
        return endpoints;
    }
}