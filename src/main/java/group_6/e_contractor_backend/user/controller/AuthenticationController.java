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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping(value = "/authenticate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createAuthenticationToken( @RequestBody JwtRequest jwtRequest) {
        try {
            logger.info("Attempting to authenticate user: {}", jwtRequest.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());

            final String jwt = jwtUtil.generateToken(userDetails.getUsername());

            logger.info("Authentication successful for user: {}", jwtRequest.getUsername());
            logger.info("Generated JWT Token: {}", jwt);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for user: {}", jwtRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Invalid credentials\", \"details\": \"" + e.getMessage() + "\"}");
        } catch (LockedException e) {
            logger.error("User account is locked: {}", jwtRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.LOCKED).body("{\"error\": \"User account is locked\", \"details\": \"" + e.getMessage() + "\"}");
        } catch (DisabledException e) {
            logger.error("User account is disabled: {}", jwtRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\": \"User account is disabled\", \"details\": \"" + e.getMessage() + "\"}");
        } catch (InternalAuthenticationServiceException e) {
            logger.error("Internal authentication service exception for user: {}", jwtRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"An internal error occurred during authentication\", \"details\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            logger.error("An error occurred during authentication for user: {}", jwtRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"An error occurred during authentication\", \"details\": \"" + e.getMessage() + "\"}");
        }
    }
}
