package com.example.materialmanager.config;

import com.example.materialmanager.interceptor.ApprovalInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    private final ApprovalInterceptor approvalInterceptor;
    
    public WebConfig(ApprovalInterceptor approvalInterceptor) {
        this.approvalInterceptor = approvalInterceptor;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(approvalInterceptor)
                .addPathPatterns("/**") // 모든 경로에 적용
                .excludePathPatterns(
                    "/auth/**",        // 인증 관련 제외
                    "/css/**",         // 정적 자원 제외
                    "/js/**",
                    "/images/**",
                    "/favicon.ico",
                    "/"               // 메인 페이지 제외
                );
    }
}