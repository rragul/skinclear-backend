package com.skinclear.skinclearbackend.security;

import com.skinclear.skinclearbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ResourceServerSecurityConfig {
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .authorizeRequests()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/test/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/brand/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/brand/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/brand/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/brand/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/ingredient/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/ingredient/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/ingredient/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/ingredient/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/ingredient-insight/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/ingredient-insight/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/ingredient-insight/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/ingredient-insight/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/product/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/product/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/product/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/product/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new FirebaseTokenFilter(userRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}




