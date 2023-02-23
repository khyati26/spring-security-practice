package com.example.springsecuritydemo2.config;

import com.example.springsecuritydemo2.util.CustomeDaoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity
//@Configuration
public class SecurityCustomAuthenticationProvider {

    @Bean
    public AuthenticationProvider authProvider() {
        return new CustomeDaoAuthenticationProvider();
    }

    @Bean
    public SecurityFilterChain filterChain2(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .authenticationManager(new ProviderManager(authProvider()))
                //for CustomeuthenticationProvider
//                .authenticationProvider(authProvider())
//                .authorizeRequests()
//                    .anyRequest().authenticated()
//                .and()
//                    .formLogin()
//                .and()
//                    .rememberMe()
//                        .rememberMeCookieName("cookie-remember-me")
//                        .tokenValiditySeconds(60);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
}
