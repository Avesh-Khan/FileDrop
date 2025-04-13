package com.AveshDeveloper.FileDropBackend.configration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.oauth2.client.registration.ClientRegistrations.fromOidcIssuerLocation;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF for API access
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequest ->
                         authorizeRequest.requestMatchers("/file-drop/files","file-drop/share/{id}/**").permitAll()
                                 .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                                 .requestMatchers("/fonts/**").permitAll()
                                 .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("http://localhost:5173/upload-file", true))
//                .formLogin(form -> form.defaultSuccessUrl("/file-drop/files", true));
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("http://localhost:5173/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                );

        return http.build();
    }
}
