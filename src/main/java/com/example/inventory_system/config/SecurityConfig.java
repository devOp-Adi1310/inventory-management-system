package com.example.inventory_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // 4. Create our permanent Admin user!
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("Admin")
                .password("admin@123") // Feel free to change this!
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF to allow the logout button to work
            .csrf(csrf -> csrf.disable()) 
            
            // 2. Allow CSS to load, but secure EVERYTHING else
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**").permitAll() 
                .anyRequest().authenticated()
            )
            
            // 3. Enforce the strict default Spring Security login page
            .formLogin(org.springframework.security.config.Customizer.withDefaults())
            
            // 4. Clean and secure logout routing
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}