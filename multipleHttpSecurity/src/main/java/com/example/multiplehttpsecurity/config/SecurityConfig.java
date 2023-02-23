package com.example.multiplehttpsecurity.config;

import com.example.multiplehttpsecurity.util.MyUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@EnableWebSecurity
public class SecurityConfig {
//    @Configuration
//    @Order(1)
    public class GeneralSecurityConfig {


//        @Bean
//        public UserDetailsService userDetailsService() {
//            return new MyUserDetailService();
//        }
//
        @Bean
        public PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }

        @Bean
        public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {

            http
                    .authorizeRequests()
                        .antMatchers("/").permitAll()
                        .anyRequest().authenticated()
                    .and()
                    .formLogin()
                        .loginPage("/login")
                        .loginProcessingUrl("/loginUser")
                        .permitAll()
                        .successHandler(this::loginSuccessHandler)
                        .failureHandler(this::loginFailureHandler);

            return http.build();
        }

        private void loginFailureHandler(HttpServletRequest request, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
            httpServletResponse.sendRedirect("/login");
        }

        private void loginSuccessHandler(HttpServletRequest request, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
            if(authentication.getAuthorities().contains("ADMIN"))
                httpServletResponse.sendRedirect("/admin/home");
            else httpServletResponse.sendRedirect("/user/home");
        }
    }

//    @Configuration
//    @Order(2)
    public class AdminSecurityConfig {
        @Bean
        public SwitchUserFilter switchUserFilter() {
            SwitchUserFilter filter = new SwitchUserFilter();
            filter.setUserDetailsService(userDetailsService2());
            filter.setSwitchUserUrl("/admin/impersonate");
            filter.setSwitchFailureUrl("/admin/switchUser");
            filter.setTargetUrl("/user/home");
            return filter;
        }
        @Bean
        public UserDetailsService userDetailsService2() {
            return new MyUserDetailService();
        }


        @Bean
        public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
//            http.authorizeRequests().antMatchers("/").permitAll();

            http.antMatcher("/admin/**")
                    .authorizeRequests()
//                    .antMatchers("/admin/impersonate*","/admin/switchUser").hasRole("ADMIN")
                    .anyRequest().hasRole("ADMIN")
//                    .and()
//                    .formLogin()
//                    .loginPage("/admin/login")
//                    .usernameParameter("username")
//                    .loginProcessingUrl("/admin/login")
//                    .defaultSuccessUrl("/admin/home")
//                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/admin/logout")
                    .logoutSuccessUrl("/");
            http.addFilterAfter(switchUserFilter(), FilterSecurityInterceptor.class);


            return http.build();
        }
    }

//    @Configuration
//    @Order(3)
    public class UserSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain3(HttpSecurity http) throws Exception {

            http.antMatcher("/user/**")
                    .authorizeRequests().anyRequest().hasRole("USER")
//                    .and()
//                    .formLogin()
//                    .loginPage("/user/login")
//                    .usernameParameter("username")
//                    .loginProcessingUrl("/user/login")
//                    .defaultSuccessUrl("/user/home")
//                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/user/logout")
                    .logoutSuccessUrl("/");

            return http.build();
        }
    }


    //    @Configuration
//    @Order(1)
//    public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
//
//        @Bean
//        public UserDetailsService userDetailsService() {
//            return new MyUserDetailService();
//        }
//
//        @Bean
//        public PasswordEncoder passwordEncoder() {
//            return NoOpPasswordEncoder.getInstance();
//        }
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests().antMatchers("/").permitAll();
//
//            http.antMatcher("/admin/**")
//                    .authorizeRequests().anyRequest().hasAuthority("ADMIN")
//                    .and()
//                    .formLogin()
//                    .loginPage("/admin/login")
//                    .usernameParameter("username")
//                    .loginProcessingUrl("/admin/login")
//                    .defaultSuccessUrl("/admin/home")
//                    .permitAll()
//                    .and()
//                    .logout()
//                    .logoutUrl("/admin/logout")
//                    .logoutSuccessUrl("/");
//        }
//    }
//
//    @Configuration
//    @Order(2)
//    public class UserSecurityConfig extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//
//            http.antMatcher("/user/**")
//                    .authorizeRequests().anyRequest().hasAuthority("USER")
//                    .and()
//                    .formLogin()
//                    .loginPage("/user/login")
//                    .usernameParameter("username")
//                    .loginProcessingUrl("/user/login")
//                    .defaultSuccessUrl("/user/home")
//                    .permitAll()
//                    .and()
//                    .logout()
//                    .logoutUrl("/user/logout")
//                    .logoutSuccessUrl("/");
//        }
//    }

}
