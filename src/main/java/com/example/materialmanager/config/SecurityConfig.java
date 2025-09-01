package com.example.materialmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/", "/auth/**", "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                )
                // 1. formLogin()을 제거합니다.
                // 2. exceptionHandling을 통해 인증되지 않은 사용자가 접근 시 로그인 페이지로 리디렉션하도록 설정합니다.
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/auth/login"))
                );

        return http.build();
    }
}
