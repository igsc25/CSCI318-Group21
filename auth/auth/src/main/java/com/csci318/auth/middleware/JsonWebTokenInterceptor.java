package com.csci318.auth.middleware;

import com.csci318.auth.util.JsonWebTokenValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JsonWebTokenInterceptor implements HandlerInterceptor {
    private boolean validateToken(String token) throws Exception {
        return JsonWebTokenValidator.validateJWT(token);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extract token after "Bearer "

            // Validate the JWT token
            if (validateToken(token)) {
                return true;
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return false;
            }
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or invalid");
        return false; // Block the request
    }
}
