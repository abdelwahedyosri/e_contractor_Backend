package group_6.e_contractor_backend.user.controller;

import java.util.List;
import java.util.Optional;

import group_6.e_contractor_backend.user.dto.UserDTO;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.service.spec.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        Optional<UserDTO> createdUser = userService.registerUser(userDTO);
        if (createdUser.isPresent()) {
            return ResponseEntity.ok(createdUser.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/session-expired")
    public ResponseEntity<Void> handleSessionExpired(@RequestParam String userId) {
        userService.handleSessionExpired(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        Optional<UserDTO> user = userService.getUserById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {
        try {
            userService.updateUser(userDTO);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/assign-role")
    public ResponseEntity<Void> assignRoleToUser(@RequestParam String userId, @RequestParam String roleId) {
        try {
            userService.assignRoleToUser(userId, roleId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/remove-role")
    public ResponseEntity<Void> removeRoleFromUser(@RequestParam String userId, @RequestParam String roleId) {
        try {
            userService.removeRoleFromUser(userId, roleId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/enable-2fa")
    public ResponseEntity<Void> enableTwoWayVerification(@RequestParam String userId) {
        try {
            userService.enableTwoWayVerification(userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/disable-2fa")
    public ResponseEntity<Void> disableTwoWayVerification(@RequestParam String userId) {
        try {
            userService.disableTwoWayVerification(userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/link-social")
    public UserEntity linkSocialAccount(@PathVariable Long userId, @RequestParam String provider, @RequestParam String providerUserId) {
        return userService.linkSocialAccount(userId, provider, providerUserId);
    }

    @GetMapping("/social-account")
    public UserEntity getUserBySocialAccount(@RequestParam String provider, @RequestParam String providerUserId) {
        return userService.getUserBySocialAccount(provider, providerUserId);
    }

    @GetMapping("/username")
    public String getUsername(@RequestParam String token) {
        return String.valueOf(userService.getUserFromToken(token));
    }

    @GetMapping("/user")
    public Optional<Optional<UserEntity>> getUserByUsername(@RequestParam String username) {
        return  userService.getUserByUsername(username);
    }

    @PutMapping("/update")
    public ResponseEntity<UserEntity> updateUserField(
            @RequestParam String fieldName,
            @RequestParam String fieldValue,
            @RequestParam String username
            ) {
        UserEntity updatedUser = userService.updateUserField(username, fieldName, fieldValue);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/exists/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.sendPasswordResetEmail(request.getEmail());
        return ResponseEntity.ok("Password reset email sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (userService.validateResetToken(request.getToken(),request.getEmail())) {
            userService.updatePassword(request.getEmail(), request.getNewPassword());
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }

    public static class ForgotPasswordRequest {

        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class ResetPasswordRequest {
        private String token;
        private String email;
        private String newPassword;

        // Getters and setters
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}

