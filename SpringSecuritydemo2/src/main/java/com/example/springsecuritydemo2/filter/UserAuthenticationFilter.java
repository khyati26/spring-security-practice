package com.example.springsecuritydemo2.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class UserAuthenticationFilter
        extends AbstractAuthenticationProcessingFilter {
    private boolean postOnly = true;

    public UserAuthenticationFilter(String defaultFilterProcessesUrl,
                                                     AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl,authenticationManager);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        //this is for REST api login
//        String username = null;
//        String password = null;
//        StringBuffer jb = new StringBuffer();
//
//        String line = null;
//        try {
//            BufferedReader reader = request.getReader();
//            while ((line = reader.readLine()) != null)
//                jb.append(line);
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, String> map = mapper.readValue(jb.toString(), Map.class);
//            username = map.get("username");
//            password = map.get("password");
//
//        } catch (Exception e) {
//            System.out.println("Something went wrong");
//        }
//        return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username,password));

        //this is for .html form login
        return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(request.getParameter("username"), request.getParameter("password")));

    }

}
