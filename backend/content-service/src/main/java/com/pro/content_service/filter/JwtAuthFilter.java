package com.pro.content_service.filter;

import com.pro.content_service.client.AuthClient;
import com.pro.content_service.dto.user.UserInfoDto;
import com.pro.content_service.service.TokenCacheService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthClient authClient;
    private final TokenCacheService tokenCacheService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (tokenCacheService.isTokenCached(token)) {

            setAuthenticationFromCache(token);
            chain.doFilter(request, response);
            return;
        }
        try {
            ResponseEntity<UserInfoDto> userInfoResponse = authClient.validateToken(authHeader);

            if (userInfoResponse.getStatusCode().is2xxSuccessful()) {
                UserInfoDto userInfo = userInfoResponse.getBody();

                assert userInfo != null;
                tokenCacheService.cacheToken(token, userInfo);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userInfo.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(userInfo.getRole())));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return;
        }

        chain.doFilter(request, response);
    }

    private void setAuthenticationFromCache(String token) {
        UserInfoDto user = tokenCacheService.getValue(token);

        if (user.getId() != null && user.getRole() != null) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user.getId(), null, Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
