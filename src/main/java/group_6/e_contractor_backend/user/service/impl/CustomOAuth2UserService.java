package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.service.spec.IUserService;
import group_6.e_contractor_backend.user.util.JwtUtil;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends OidcUserService {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oidcUser.getAttributes();
        String providerUserId;
        String email = null;

        switch (provider) {
            case "facebook":
                providerUserId = attributes.get("id").toString();
                email = attributes.get("email").toString();
                break;
            case "linkedin":
                providerUserId = attributes.get("id").toString();
                email = fetchLinkedInEmail(userRequest);
                break;
            case "instagram":
                providerUserId = attributes.get("id").toString();
                break;
            default:
                providerUserId = oidcUser.getSubject();
                email = oidcUser.getEmail();
                break;
        }

        UserEntity user = userService.getUserBySocialAccount(provider, providerUserId);
        if (user == null) {
            // User not linked yet, handle account creation or linking here
            user = new UserEntity();
            user.setEmail(email);
            user.setUsername(oidcUser.getName());
            // Save new user
            user = userService.save(user);
            // Link social account
            userService.linkSocialAccount(user.getUserId(), provider, providerUserId);
        }

        String jwt = jwtUtil.generateToken(user.getUsername());
        oidcUser.getAttributes().put("jwt", jwt);

        return oidcUser;
    }

    private String fetchLinkedInEmail(OidcUserRequest userRequest) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("linkedin");
        String token = userRequest.getAccessToken().getTokenValue();
        String emailEndpoint = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";

        Map<String, Object> response = restTemplate.getForObject(emailEndpoint + "&oauth2_access_token=" + token, Map.class);
        if (response != null && response.containsKey("elements")) {
            List<Map<String, Object>> elements = (List<Map<String, Object>>) response.get("elements");
            if (!elements.isEmpty() && elements.get(0).containsKey("handle~")) {
                Map<String, Object> handle = (Map<String, Object>) elements.get(0).get("handle~");
                return handle.get("emailAddress").toString();
            }
        }
        return null;
    }
}
