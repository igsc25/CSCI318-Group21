package com.csci318.auth.config;

import com.csci318.auth.middleware.JsonWebTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final JsonWebTokenInterceptor jsonWebTokenInterceptor;

    @Autowired
    public WebConfig(JsonWebTokenInterceptor jsonWebTokenInterceptor) {
        this.jsonWebTokenInterceptor = jsonWebTokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Specify the paths to apply the interceptor to
        registry.addInterceptor(jsonWebTokenInterceptor)
                .addPathPatterns("/**") // Apply to all endpoints
                .excludePathPatterns("/token-sessions/register", "/token-sessions/login");
    }
}
