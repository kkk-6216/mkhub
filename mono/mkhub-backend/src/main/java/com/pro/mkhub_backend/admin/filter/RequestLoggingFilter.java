package com.pro.mkhub_backend.admin.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger REQUEST_LOGGER = LoggerFactory.getLogger("REQUEST_LOGGER");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        filterChain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        String user = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";
        String role = request.isUserInRole("ADMIN") ? "ADMIN" :
                request.isUserInRole("MODERATOR") ? "MODERATOR" :
                        request.isUserInRole("STUDENT") ? "STUDENT" : "UNKNOWN";

        REQUEST_LOGGER.info("User={}, Role={}, Method={}, URI={}, Duration={}ms",
                user, role, request.getMethod(), request.getRequestURI(), duration);
    }
}
