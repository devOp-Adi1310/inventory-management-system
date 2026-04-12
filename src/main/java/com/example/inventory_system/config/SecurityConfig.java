package com.example.inventory_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF so your JavaScript fetch() requests aren't blocked
                .csrf(csrf -> csrf.disable())

                // 2. Lock down every single URL
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())

                // 3. Keep the default HTML login screen
                .formLogin(withDefaults())
                .httpBasic(withDefaults());

        return http.build();
    }

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
                // ... (your existing authorizeHttpRequests and formLogin code) ...

                // ADD THIS NEW LOGOUT BLOCK
                .logout(logout -> logout
                        .logoutUrl("/logout") // The URL that triggers logout
                        .logoutSuccessUrl("/login?logout") // Where to send them after logging out
                        .invalidateHttpSession(true) // Destroys the active session
                        .deleteCookies("JSESSIONID") // Clears the browser cookie
                    );
        return http.build();            
    }
}