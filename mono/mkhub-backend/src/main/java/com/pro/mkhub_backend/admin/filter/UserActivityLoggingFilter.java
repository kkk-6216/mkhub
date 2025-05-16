package com.pro.mkhub_backend.admin.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserActivityLoggingFilter extends OncePerRequestFilter {

    private static final Logger REQUEST_LOGGER = LoggerFactory.getLogger("REQUEST_LOGGER");
    private static final AntPathMatcher matcher = new AntPathMatcher();



    private static final List<String> EXCLUDED_PATHS = List.of(
            "/admin/logs",
            "/admin/stats",
            "/admin/endpoints"
    );
    private static final List<String> EXCLUDED_PATTERNS = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        if (EXCLUDED_PATHS.stream().anyMatch(requestUri::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }


        if (shouldExclude(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - start;

            String method = wrappedRequest.getMethod();
            String uri = wrappedRequest.getRequestURI();
            int status = wrappedResponse.getStatus();

            String requestBody = extractRequestBody(wrappedRequest);
            String responseBody = extractResponseBody(wrappedResponse);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
            String role = (auth != null && auth.getAuthorities() != null)
                    ? auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","))
                    : "NONE";

            // 🔍 Заголовки и IP
            String ip = getClientIp(request);
            String userAgent = request.getHeader("User-Agent");
            String contentType = request.getHeader("Content-Type");
            String authorization = request.getHeader("Authorization");

            REQUEST_LOGGER.info("""
                    User={}, Role={}, IP={}, Method={}, URI={}, Status={}, Duration={}ms
                    Headers => [Authorization={}, Content-Type={}, User-Agent={}]
                    RequestBody => {}
                    ResponseBody => {}
                    Timestamp => {}
                    """,
                    username, role, ip, method, uri, status, duration,
                    authorization, contentType, userAgent,
                    requestBody,
                    responseBody,
                    LocalDateTime.now()
            );

            wrappedResponse.copyBodyToResponse();
        }
    }

    private boolean shouldExclude(String uri) {
        return EXCLUDED_PATTERNS.stream().anyMatch(pattern -> matcher.match(pattern, uri));
    }

    private String extractRequestBody(ContentCachingRequestWrapper request) {
        String contentType = request.getContentType();

        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            if (request instanceof MultipartHttpServletRequest multipartRequest) {
                List<String> filenames = multipartRequest.getFileMap().values().stream()
                        .map(MultipartFile::getOriginalFilename)
                        .filter(name -> name != null && !name.isBlank())
                        .toList();
                return "[multipart] files: " + filenames;
            }
            return "[multipart] request";
        }

        byte[] buf = request.getContentAsByteArray();
        return (buf.length == 0) ? "-" : new String(buf, StandardCharsets.UTF_8).replaceAll("\\s+", " ");
    }

    private String extractResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        return (buf.length == 0) ? "-" : new String(buf, StandardCharsets.UTF_8).replaceAll("\\s+", " ");
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isBlank()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
