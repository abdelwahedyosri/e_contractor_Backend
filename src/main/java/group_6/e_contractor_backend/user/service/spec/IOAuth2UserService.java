package group_6.e_contractor_backend.user.service.spec;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;

public interface IOAuth2UserService {
    OidcUser loadUser(OidcUserRequest userRequest);
}
