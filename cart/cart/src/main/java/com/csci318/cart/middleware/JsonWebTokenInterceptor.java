package com.csci318.cart.middleware;

import com.csci318.cart.service.TokenService;
import com.csci318.cart.util.JsonWebTokenValidator;
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

        // Allow guest users to interact with the cart (they don't have token)
        if (token == null) {
            return true;
        }

        // In case if user log in as CUSTOMER
        if (validateToken(token)) {
            Map<String, Object> claims = decodeToken(token);
            String role = (String) claims.get("role");

            // Block all requests if the user is an admin
            if (role.equals("ADMIN")) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admins are not allowed to interact with the cart");
                return false;
            }

            return true;
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return false;
        }
    }
}
