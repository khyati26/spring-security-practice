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
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class OAuth2LoginConfig {
//    @Bean
//    public OAuth2AuthorizedClientManager authorizedClientManager(
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientRepository authorizedClientRepository) {
//
//        OAuth2AuthorizedClientProvider authorizedClientProvider =
//                OAuth2AuthorizedClientProviderBuilder.builder()
//                        .authorizationCode()
//                        .refreshToken()
//                        .clientCredentials()
//                        .password()
//                        .build();
//
//        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
//                new DefaultOAuth2AuthorizedClientManager(
//                        clientRegistrationRepository, authorizedClientRepository);
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return authorizedClientManager;
//    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .oauth2Login(oauth2 -> oauth2
//                        .clientRegistrationRepository(this.clientRegistrationRepository())
//                        .authorizedClientRepository(this.authorizedClientRepository())
//                        .authorizedClientService(this.authorizedClientService())
//                        .loginPage("/login")
//                        .authorizationEndpoint(authorization -> authorization
//                                .baseUri(this.authorizationRequestBaseUri())
//                                .authorizationRequestRepository(this.authorizationRequestRepository())
//                                .authorizationRequestResolver(this.authorizationRequestResolver())
//                        )
//                        .redirectionEndpoint(redirection -> redirection
//                                .baseUri(this.authorizationResponseBaseUri())
//                        )
//                        .tokenEndpoint(token -> token
//                                .accessTokenResponseClient(this.accessTokenResponseClient())
//                        )
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userAuthoritiesMapper(this.userAuthoritiesMapper())
//                                .userService(this.oauth2UserService())
//                                .oidcUserService(this.oidcUserService())
//                        )
//                );
//        return http.build();
//    }


    //    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login()
//                    .defaultSuccessUrl("/loginSuccess");
//        return http.build();
//    }
//
    @Autowired
    private CustomOAuth2UserService oauthUserService;
    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/admin/login", "/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .clientRegistrationRepository(this::googleClientRegistration)
//                .authorizedClientRepository(this::authorizedClientRepository)
                .loginPage("/admin/login")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .defaultSuccessUrl("/loginSuccess")
//                .successHandler(this::authenticationSuccessHandler)
                .and()
                .logout()
                .logoutSuccessUrl("/");
        return http.build();

    }

    private void authenticationSuccessHandler(HttpServletRequest request, HttpServletResponse response,
                                              Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        userService.processOAuthPostLogin(oauthUser.getEmail());
        response.sendRedirect("/admin/home");
    }
    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new HttpSessionOAuth2AuthorizedClientRepository();
    }
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration("google"));
    }

    public ClientRegistration googleClientRegistration(String s) {
        return ClientRegistration.withRegistrationId("google")
                .clientId("290378066656-934255mra4hvbjh92our85fvasl74cvf.apps.googleusercontent.com")
                .clientSecret("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")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email", "address", "phone")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }
}