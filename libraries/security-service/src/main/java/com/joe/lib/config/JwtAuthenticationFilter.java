package com.joe.lib.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
//@Order(2)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userName;
            final Integer roleTypeId;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);

            if (jwtService.validateToken(jwt)) {
                userName = jwtService.extractUsername(jwt);
                roleTypeId = jwtService.extractRoleType(jwt);

                if (userName != null && roleTypeId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    final RoleType[] roleType = new RoleType[1];
                    Arrays.stream(RoleType.values()).filter(roleType1 -> roleType1.roleId == roleTypeId).findFirst().ifPresent(roleType1 -> roleType[0] = roleType1);
                    customUserDetailsService.setRoleType(roleType[0]);
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        throw new AccessDeniedException("Invalid Token!!! Token {role ID OR user Email} does not match the expected role ID OR user Email ");
                    }


                }
            }
            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            BaseResponse<List<String>> errorResponse = new BaseResponse<>(Collections.singletonList(e.getMessage()), HttpStatus.UNAUTHORIZED.name(), Boolean.FALSE, HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(convertObjectToJson(errorResponse));
        } catch (AccessDeniedException e) {
            BaseResponse<List<String>> errorResponse = new BaseResponse<>(Collections.singletonList(e.getMessage()), HttpStatus.FORBIDDEN.name(), Boolean.FALSE, HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(convertObjectToJson(errorResponse));
        }

    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}
