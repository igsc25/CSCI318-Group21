package com.csci318.user.middleware;

import com.csci318.user.service.TokenService;
import com.csci318.user.util.JsonWebTokenValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class JsonWebTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private final TokenService tokenService;

    public JsonWebTokenInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    private boolean validateToken(String token) throws Exception {
        return JsonWebTokenValidator.validateJWT(token);
    }

    private Map<String, Object> decodeToken(String token) throws Exception {
        return JsonWebTokenValidator.decodeJWT(token); // Assume decodeJWT extracts and returns the claims
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String token = tokenService.getToken();

        if (token == null) {
            // No token found, block the request
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token missing or invalid");
            return false;
        }

        if (validateToken(token)) {
            Map<String, Object> claims = decodeToken(token);
            String role = (String) claims.get("role");

            if (request.getRequestURI().startsWith("/users/admins") && !role.equals("ADMIN")) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied for non-admin users");
                return false;
            }

            return true;
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return false;
        }
    }
}
