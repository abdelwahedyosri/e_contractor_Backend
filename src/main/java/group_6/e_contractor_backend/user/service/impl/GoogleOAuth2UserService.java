package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.service.spec.IUserService;
import group_6.e_contractor_backend.user.service.spec.IOAuth2UserService;
import group_6.e_contractor_backend.user.util.JwtUtil;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Service
public class GoogleOAuth2UserService extends OidcUserService implements IOAuth2UserService {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();
        String providerUserId = oidcUser.getSubject();
        String email = oidcUser.getEmail();

        UserEntity user = userService.getUserBySocialAccount("google", providerUserId);
        if (user == null) {
            user = new UserEntity();
            user.setEmail(email);
            user.setUsername(oidcUser.getName());
            user = userService.save(user);
            userService.linkSocialAccount(user.getUserId(), "google", providerUserId);
        }

        String jwt = jwtUtil.generateToken(user.getUsername());
        oidcUser.getAttributes().put("jwt", jwt);

        return oidcUser;
    }
}
