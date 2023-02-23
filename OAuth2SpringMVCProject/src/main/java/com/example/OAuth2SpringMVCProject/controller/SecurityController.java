package com.example.OAuth2SpringMVCProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/app")
public class SecurityController {
//    @Autowired
//    private AuthenticatedPrincipalOAuth2AuthorizedClientRepository clientRepository = new OAuth2AuthorizedClientRepository();

    @RequestMapping(method = RequestMethod.GET, value = "/home")
    public String getPage() {
        return "home";
    }

    @GetMapping("/page")
    public String getJSPPage() {
        return "index";
    }

//    @GetMapping("/loginSuccess")
//    public String getLoginInfo(Model model, OAuth2AuthenticationToken authenticationToken, HttpServletRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        OAuth2AuthorizedClient client = clientRepository.loadAuthorizedClient(
//                authenticationToken.getAuthorizedClientRegistrationId(),
//                authentication, request);
//        String userInfoEndpointUri = client.getClientRegistration()
//                .getProviderDetails().getUserInfoEndpoint().getUri();
//
//        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
//                    .getTokenValue());
//            HttpEntity entity = new HttpEntity("", headers);
//            ResponseEntity<Map> response = restTemplate
//                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
//            Map userAttributes = response.getBody();
//            model.addAttribute("name", userAttributes.get("name"));
//        }
//        return "index";
//    }
}
