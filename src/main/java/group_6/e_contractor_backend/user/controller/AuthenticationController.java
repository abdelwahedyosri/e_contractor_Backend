package group_6.e_contractor_backend.user.controller;

import group_6.e_contractor_backend.user.dto.JwtRequest;
import group_6.e_contractor_backend.user.dto.JwtResponse;
import group_6.e_contractor_backend.user.service.impl.CustomUserDetailsService;
import group_6.e_contractor_backend.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest; // Use jakarta.servlet package

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());

            final String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Invalid credentials\", \"details\": \"" + e.getMessage() + "\"}");
        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("{\"error\": \"User account is locked\", \"details\": \"" + e.getMessage() + "\"}");
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\": \"User account is disabled\", \"details\": \"" + e.getMessage() + "\"}");
        } catch (InternalAuthenticationServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"An internal error occurred during authentication\", \"details\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"An error occurred during authentication\", \"details\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<?> oauth2Callback(OAuth2AuthenticationToken authentication, HttpServletRequest request) {
        try {
            OAuth2User oAuth2User = authentication.getPrincipal();
            String username = oAuth2User.getAttribute("email"); // Assuming the OAuth2 provider returns the email as the username
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            final String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"An error occurred during OAuth2 authentication\", \"details\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/update-last-login")
    public ResponseEntity<?> updateLastLogin(@AuthenticationPrincipal UserDetails userDetails) {
        userDetailsService.updateLastLogin(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
