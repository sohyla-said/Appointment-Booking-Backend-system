package com.example.SABS.config;

import com.example.SABS.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        jsr250Enabled = true,
        securedEnabled = true
)
public class SecurityConfig {

    private final JwtAuthentiactionFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // FIX: Manually creating a constructor
    public SecurityConfig(JwtAuthentiactionFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**").permitAll()
////                        .requestMatchers("/api/patient/**").hasAnyRole(Role.PATIENT.name(), Role.ADMIN.name(), Role.SERVICE_PROVIDER.name())
//                        .requestMatchers("/api/patient/**").hasAnyRole("PATIENT", "ADMIN", "SERVICE_PROVIDER")
//                        .requestMatchers("/api/serviceprovider/**").hasAnyRole(Role.SERVICE_PROVIDER.name(), Role.ADMIN.name())
//                        .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
//                        .anyRequest().authenticated())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return httpSecurity.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/patient/**").hasAnyRole(Role.PATIENT.name(), Role.ADMIN.name(), Role.SERVICE_PROVIDER.name())
                        .requestMatchers("/api/serviceprovider/**").hasAnyRole(Role.SERVICE_PROVIDER.name(), Role.ADMIN.name())
                        .requestMatchers("/api/admin/**").hasAnyRole(Role.ADMIN.name())
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // ✅ Ensure JWT filter is executed before

        return httpSecurity.build();
    }

}
