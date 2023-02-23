package com.example.multiplehttpsecurity.config;

import com.example.multiplehttpsecurity.util.CustomOAuth2User;
import com.example.multiplehttpsecurity.util.CustomOAuth2UserService;
import com.example.multiplehttpsecurity.util.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@EnableWebSecurity
//@Configuration
public class SecurityConfigSwitchUser {

//    @Autowired
//    private CustomOAuth2UserService oauthUserService;
//    @Autowired
//    private UserService userService;

    //    @Bean
//    public UserDetailsService userDetailsService() {
//        return new MyUserDetailService();
//    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    //    @Bean
    public SwitchUserFilter switchUserFilter() {
        SwitchUserFilter filter = new SwitchUserFilter();
//        filter.setUserDetailsService(userDetailsService());
        filter.setSwitchUserUrl("/impersonate");
        filter.setSwitchFailureUrl("/switchUser");
        filter.setTargetUrl("/user/home");
        return filter;
    }

    @Bean
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/impersonate*").hasRole("ADMIN")
//                .antMatchers("/impersonate*").authenticated()
//                .antMatchers("/**").permitAll()
                .antMatchers("/", "/login", "/oauth/**").permitAll()
                .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll()
//                .and()
//                .oauth2Login()
//                .loginPage("/login")
//                .userInfoEndpoint()
//                .userService(oauthUserService)
//                .and()
//                .successHandler(this::authenticationSuccessHandler)
        ;
//                .and()
//                .logout()
//                .logoutSuccessUrl("/");
//        http.addFilterAfter(switchUserFilter(), FilterSecurityInterceptor.class);
        return http.build();
    }

    private void authenticationSuccessHandler(HttpServletRequest request, HttpServletResponse response,
                                              Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
//        userService.processOAuthPostLogin(oauthUser.getEmail());
        response.sendRedirect("/admin/home");
    }
}
