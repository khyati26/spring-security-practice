package com.example.oauth2practiceproject.controller;

import com.example.oauth2practiceproject.util.Constants;
import com.example.oauth2practiceproject.util.PkceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Controller
public class SecurityController {
    MultiValueMap<String, String> uriParameters = null;
    String code_verifier = "";
    @GetMapping("/home")
    public String getHomePage() {
        return "home";
    }

    // Authorization Code grant type flow
    @GetMapping("/oauth2/authorization/google")
    public String doAuthenticationWithGoogle() {
         uriParameters = new LinkedMultiValueMap<>();
        uriParameters.add("response_type", "code");
        uriParameters.add("client_id", "290378066656-934255mra4hvbjh92our85fvasl74cvf.apps.googleusercontent.com");
        uriParameters.add("redirect_uri", "http://localhost:8080/login/oauth2/code/google");
        uriParameters.add("scope", "email");
        uriParameters.add("state", "1236548");
        UriBuilder uriBuilder = new DefaultUriBuilderFactory().uriString(Constants.GOOGLE_AUTHORIZATION_ENDPOINT).queryParams(uriParameters);
        Function<UriBuilder, URI> authorizationRequestUriFunction = (builder) -> builder.build();
        URI uri = uriBuilder.build();
        System.out.println(uri);
        return "redirect:" + uri;
    }

    @GetMapping("/login/oauth2/code/google")
    public String getAuthenticationResponse(@RequestParam MultiValueMap<String, String> params) {
//        params.forEach((s, s2) -> System.out.println("key:"+s+" value:"+s2));
        params.forEach((s, strings) -> System.out.println(s + " : " + strings.toString()));
        String code = params.get("code").get(0);
        return "redirect:http://localhost:8080/tokenEndpoint?code=" + code;
    }

    @GetMapping("/tokenEndpoint")
    public String requestToTokenEndpoint(@RequestParam String code) {
//        String tokenEndpoint = "https://www.googleapis.com/oauth2/v4/token";
         uriParameters = new LinkedMultiValueMap<>();
        uriParameters.add("grant_type", "authorization_code");
        uriParameters.add("code", code);
        uriParameters.add("redirect_uri", "http://localhost:8080/login/oauth2/code/google");
        uriParameters.add("client_id", "290378066656-934255mra4hvbjh92our85fvasl74cvf.apps.googleusercontent.com");
        uriParameters.add("client_secret", "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");
        if(!code_verifier.isEmpty()){
            uriParameters.add("code_verifier",code_verifier);
        }
        UriBuilder uriBuilder = new DefaultUriBuilderFactory().uriString(Constants.GOOGLE_TOKEN_ENDPOINT).queryParams(uriParameters);
        URI uri = uriBuilder.build();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(uriParameters, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, null, String.class);
        System.out.println(response.getBody());
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,String> map=objectMapper.readValue(response.getBody(), Map.class);

            return "redirect:http://localhost:8080/userinfo?token="+map.get("access_token");
        } catch (JsonMappingException e) {
            e.printStackTrace();return "error"+e.getMessage();
        } catch (JsonProcessingException e) {
            e.printStackTrace();return "error"+e.getMessage();
        }

//        response.forEach((o, o2) -> System.out.println(o+" : "+o2));
    }

    @GetMapping("/userinfo")
    public Map<String,String> getAuthorizedUserInfo(@RequestParam String token) throws JsonProcessingException {
//        String userEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        URI uri = UriComponentsBuilder.fromUriString(Constants.GOOGLE_USERINFO_ENDPOINT).build().toUri();
        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<?> requestEntity = new RequestEntity<>(null, httpHeaders, HttpMethod.GET, uri);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        System.out.println(responseEntity.getBody());
        ObjectMapper objectMapper = new ObjectMapper();

        return(Map<String, String>) objectMapper.readValue(responseEntity.getBody(),Map.class);
    }

    //Authorization Code Flow with Proof Key for Code Exchange (PKCE) grant type flow
    @GetMapping("/oauth2/pkce/authorization/google")
    public String doAuthorization() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        PkceUtil  pkceUtil =  new PkceUtil();
        code_verifier=pkceUtil.generateCodeVerifier();
        uriParameters = new LinkedMultiValueMap<>();
        uriParameters.add("response_type", "code");
        uriParameters.add("client_id", "290378066656-934255mra4hvbjh92our85fvasl74cvf.apps.googleusercontent.com");
        uriParameters.add("redirect_uri", "http://localhost:8080/login/oauth2/code/google");
        uriParameters.add("scope", "email");
        uriParameters.add("state", "1236548");
        uriParameters.add("code_challenge",pkceUtil.generateCodeChallange(code_verifier));
        uriParameters.add("code_challenge_method","S256");

        UriBuilder uriBuilder = new DefaultUriBuilderFactory().uriString(Constants.GOOGLE_AUTHORIZATION_ENDPOINT).queryParams(uriParameters);

        URI uri = uriBuilder.build();
        System.out.println(uri.toString());
        return "redirect:" + uri.toString();

    }

    //Client Credentials Flow grant type flow
    @GetMapping("/oauth2/clientcredentials/authorization/google")
    public String getAccessToken(){
        uriParameters = new LinkedMultiValueMap<>();
        uriParameters.add("grant_type", "client_credential");
        uriParameters.add("redirect_uri", "http://localhost:8080/login/oauth2/code/google");
        uriParameters.add("client_id", "290378066656-934255mra4hvbjh92our85fvasl74cvf.apps.googleusercontent.com");
        uriParameters.add("client_secret", "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");
        uriParameters.add("scope", "email");
        UriBuilder  uriBuilder = new DefaultUriBuilderFactory().uriString(Constants.GOOGLE_TOKEN_ENDPOINT).queryParams(uriParameters);
        URI uri = uriBuilder.build();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(uriParameters, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, null, String.class);
        System.out.println(response.getBody());
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,String> map=objectMapper.readValue(response.getBody(), Map.class);

            return "redirect:http://localhost:8080/userinfo?token="+map.get("access_token");
        } catch (JsonMappingException e) {
            e.printStackTrace();return "error"+e.getMessage();
        } catch (JsonProcessingException e) {
            e.printStackTrace();return "error"+e.getMessage();
        }

    }
}
