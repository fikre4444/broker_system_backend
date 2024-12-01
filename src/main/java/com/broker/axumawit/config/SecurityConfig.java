package com.broker.axumawit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.broker.axumawit.filter.JwtFilter;
import com.broker.axumawit.handlers.CustomAuthenticationEntryPoint;
import com.broker.axumawit.handlers.OAuth2LoginSuccessHandler;
import com.broker.axumawit.service.MyUserDetailsService;

@Configuration
public class SecurityConfig {

  @Autowired
  private MyUserDetailsService userDetailsService;

  @Autowired
  private JwtFilter jwtFilter;

  @Autowired
  private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Autowired
  private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

  // @Bean
  // public WebMvcConfigurer corsConfigurer() {
  // return new WebMvcConfigurer() {
  // @Override
  // public void addCorsMappings(CorsRegistry registry) {
  // registry.addMapping("/**") // Allow all paths
  // .allowedOrigins("http://localhost:5173") // Allow frontend origin
  // .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific
  // HTTP methods
  // .allowedHeaders("*") // Allow all headers
  // .allowCredentials(true); // Allow crede
  // }
  // };
  // }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all paths
            .allowedOrigins("*") // Allow all origins
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific HTTP methods
            .allowedHeaders("*") // Allow all headers
            .allowCredentials(false); // Disable credentials to comply with wildcard origins
      }
    };
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http = http.csrf(customzer -> customzer.disable())
        .httpBasic(customizer -> customizer.disable())
        .authorizeHttpRequests(customizer -> customizer
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/api/hello/**", "/api/auth/**", "/api/register/**").permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
            .requestMatchers("/api/user/**").hasRole("USER"))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(customAuthenticationEntryPoint))
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/oauth2/authorization/google") // Ensure this matches your setup
            .successHandler(oAuth2LoginSuccessHandler)); // Success handler

    return http.build();
  }

  @Bean
  public AuthenticationProvider authenticator() {
    DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
    dao.setUserDetailsService(userDetailsService);
    dao.setPasswordEncoder(new BCryptPasswordEncoder());
    return dao;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

}
