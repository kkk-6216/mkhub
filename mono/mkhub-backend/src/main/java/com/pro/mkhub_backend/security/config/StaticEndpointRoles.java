package com.pro.mkhub_backend.security.config;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

public class StaticEndpointRoles {

    private static final String ALL_METHODS = "ALL";
    private static final String ROLE_AUTHENTICATED = "AUTHENTICATED";
    private static final String ROLE_PUBLIC = "PUBLIC";

    private static final Map<String, Map<String, List<String>>> protectedEndpoints = new HashMap<>();
    private static final Map<String, List<String>> cache = new HashMap<>();
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static boolean defaultAuthenticated = false;

    static {
        // API-доступы по аналогии с фильтром Spring Security
        register("/auth/**", ALL_METHODS, List.of(ROLE_PUBLIC));
        register("/swagger-ui/**", ALL_METHODS, List.of(ROLE_PUBLIC));
        register("/v3/api-docs/**", ALL_METHODS, List.of(ROLE_PUBLIC));

        register("/admin/**", ALL_METHODS, List.of("ROLE_ADMIN"));
        register("/departments/**", ALL_METHODS, List.of("ROLE_MODERATOR"));
        register("/faculties/**", ALL_METHODS, List.of("ROLE_MODERATOR"));

        // Добавим аналог anyRequest().authenticated()
        enableDefaultAuthenticated(true);
    }


    /**
     * Включает правило по умолчанию: все остальные запросы требуют аутентификации
     */
    public static void enableDefaultAuthenticated(boolean enabled) {
        defaultAuthenticated = enabled;
    }

    /**
     * Регистрирует правило доступа
     */
    public static void register(String urlPattern, RequestMethod method, List<String> roles) {
        String methodKey = (method != null) ? method.name() : ALL_METHODS;
        register(urlPattern, methodKey, roles);
    }

    /**
     * Регистрирует правило доступа (внутренний метод)
     */
    public static void register(String urlPattern, String methodKey, List<String> roles) {
        protectedEndpoints
                .computeIfAbsent(urlPattern, k -> new HashMap<>())
                .put(methodKey, roles);
    }

    /**
     * Получает роли, разрешённые для заданного URL и метода.
     */
    public static Optional<List<String>> getRoles(String url, RequestMethod method) {
        String methodKey = (method != null) ? method.name() : ALL_METHODS;
        String cacheKey = url + "::" + methodKey;

        if (cache.containsKey(cacheKey)) {
            return Optional.ofNullable(cache.get(cacheKey));
        }

        for (Map.Entry<String, Map<String, List<String>>> entry : protectedEndpoints.entrySet()) {
            String pattern = entry.getKey();
            if (pathMatcher.match(pattern, url)) {
                Map<String, List<String>> methodRoles = entry.getValue();
                List<String> roles = methodRoles.getOrDefault(methodKey, methodRoles.get(ALL_METHODS));
                if (roles != null) {
                    cache.put(cacheKey, roles);
                    return Optional.of(roles);
                }
            }
        }

        if (defaultAuthenticated) {
            List<String> roles = List.of(ROLE_AUTHENTICATED);
            cache.put(cacheKey, roles);
            return Optional.of(roles);
        }

        return Optional.empty();
    }

}
