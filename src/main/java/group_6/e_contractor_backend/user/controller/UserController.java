package group_6.e_contractor_backend.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import group_6.e_contractor_backend.user.dto.UserCreationDTO;
import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.entity.CompanyEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.repository.ICandidateRepository;
import group_6.e_contractor_backend.user.repository.ICompanyRepository;
import group_6.e_contractor_backend.user.repository.IUserRepository;
import group_6.e_contractor_backend.user.service.impl.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class UserController {

    private final UserService userService;
    private final ICompanyRepository companyRepository;
    private final ICandidateRepository candidateRepository;
    public UserController(UserService userService,ICompanyRepository companyRepository,ICandidateRepository candidateRepository) {
        this.userService = userService;
        this.companyRepository=companyRepository;
        this.candidateRepository=candidateRepository;
    }


    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@RequestBody UserCreationDTO userCreationDTO) {
        Optional<UserEntity> createdUser = userService.registerUser(userCreationDTO);
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
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortColumn,
            @RequestParam(required = false) String sortDirection) {

        Page<UserEntity> usersPage = userService.getUsersExcludingSpecificRoles(page, size, search, sortColumn, sortDirection);
        Map<String, Object> response = new HashMap<>();
        response.put("users", usersPage.getContent());
        response.put("total", usersPage.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable String userId) {
        Optional<UserEntity> user = userService.getUserById(userId);
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
    public ResponseEntity<Void> updateUser(@RequestBody UserEntity user) {
        try {
            userService.updateUser(user);
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

    @GetMapping("/get-me")
    public  ResponseEntity<Map<String, Object>> getMe(@RequestParam String token) {
        UserEntity user = userService.getConnectedUser(token);
        Map<String, Object> response = new HashMap<>();
        CompanyEntity company = companyRepository.getCompanyEntityByUserUsername(user.getUsername());
        CandidateEntity candidate = candidateRepository.getCandidateEntityByUserUsername(user.getUsername());
        response.put("candidate", candidate);
        response.put("company", company);

        return ResponseEntity.ok(response);
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

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    @GetMapping("/test")
    public String test() {
        return userService.test("test");

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

