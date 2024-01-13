package com.fvthree.blogpost.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fvthree.blogpost.security.JwtAuthenticationEntryPoint;
import com.fvthree.blogpost.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter authenticationFilter){
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
        	.authorizeHttpRequests((authorize) ->
                        //authorize.anyRequest().authenticated()
                        authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        		.requestMatchers(HttpMethod.POST, "/api/v1/users/login").permitAll()
                        		.requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                                .anyRequest().authenticated()

                ).exceptionHandling( exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                ).sessionManagement( session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        
        return http.build();
    }
}
