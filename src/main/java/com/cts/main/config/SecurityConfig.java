package com.cts.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf)->csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/api/menu-items","/user/adduser").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/api/menuItems/**").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.PUT, "/api/menuItems/**").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.DELETE, "/api/menuItems/**").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/api/customerOrders/**").authenticated()
//                                .requestMatchers(HttpMethod.POST, "/api/customerOrders/**").hasAnyRole("ADMIN", "CUSTOMER")
//                                .requestMatchers(HttpMethod.PUT, "/api/customerOrders/**").hasAnyRole("ADMIN", "CUSTOMER")
//                                .requestMatchers(HttpMethod.DELETE, "/api/customerOrders/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}