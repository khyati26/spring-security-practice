package com.example.springsecuritydemo2.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


public class CustomeDaoAuthenticationProvider
        implements AuthenticationProvider {

    @Autowired
    MyUserDetailService myUserDetailService ;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = myUserDetailService.loadUserByUsername(authentication.getName());
        String username = userDetails.getUsername();
        String password = authentication.getCredentials().toString();
        System.out.println(password + "  " + userDetails.getPassword());
//        if (passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
          if(authentication.getCredentials().toString().equals(userDetails.getPassword())){
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
            authenticationToken.setDetails(authentication.getDetails());
            return authenticationToken;
        } else
            throw new BadCredentialsException("Invalid password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
