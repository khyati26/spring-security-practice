package com.example.springsecuritydemo2.config;

import com.example.springsecuritydemo2.filter.UserAuthenticationEntryPoint;
import com.example.springsecuritydemo2.util.CustomPersistentTokenRepository;
import com.example.springsecuritydemo2.util.CustomeDaoAuthenticationProvider;
import com.example.springsecuritydemo2.util.MyUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfigLoginUsingFilterAndJwt {

    @Autowired
    UserAuthenticationEntryPoint userAuthenticationFilter;

    @Autowired
    MyUserDetailService userDetailsService;

//    @Autowired
//    JwtRequestFilter jwtRequestFilter;
//
//    @Autowired
//    JwtTokenService jwtTokenService;

    @Bean
    public AuthenticationProvider authProvider() {
        return new CustomeDaoAuthenticationProvider();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getOrBuild();

        http
                .csrf().disable()
                .cors().disable()
                .authenticationProvider(authProvider())
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(authorize -> authorize
//                        .antMatchers("/login2").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling()
                .authenticationEntryPoint(userAuthenticationFilter)
                .and()
                    .formLogin()
                    //this is for .html form login
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/login2")
                        .permitAll()
        //                .successHandler(this::loginSuccessHandler)
                        .failureHandler(this::loginFailureHandler)

                .and()
                    .logout()
                .and()
                    .rememberMe()
                        .rememberMeServices(rememberMeServices())
                        .key("933500A9-1D54-4B7B-BC0A-3CE2749250A7")
        ;
//                .rememberMeCookieName("cookie-remember-me")
//                .rememberMeParameter("remember-me")
//                .tokenValiditySeconds(60*24);

        //THis is for rest Login
//        UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter("/login2",authenticationManager);
//        userAuthenticationFilter.setAuthenticationSuccessHandler(this::loginSuccessHandler);
//        userAuthenticationFilter.setAuthenticationFailureHandler(this::loginFailureHandler);
//        http.addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        String key = "933500A9-1D54-4B7B-BC0A-3CE2749250A7";
        PersistentTokenBasedRememberMeServices rememberMeServices =
                new PersistentTokenBasedRememberMeServices(
                        key, userDetailsService, persistentTokenRepository());
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
       return new CustomPersistentTokenRepository();
    }

    private void loginFailureHandler(HttpServletRequest request, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        objectMapper.writeValue(httpServletResponse.getWriter(), "user login fail");
    }

    private void loginSuccessHandler(HttpServletRequest request, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletResponse.sendRedirect("/");
        //        ObjectMapper objectMapper =new ObjectMapper();
//        httpServletResponse.setStatus(HttpStatus.OK.value());
//        objectMapper.writeValue(httpServletResponse.getWriter(),"user logged in");
//        objectMapper.writeValue(httpServletResponse.getWriter(), "token: "+jwtTokenService.generateToken(authentication));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }
//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        UserDetails userDetails = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("user")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(userDetails);
//    }
}
