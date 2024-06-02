package group_6.e_contractor_backend.user.controller;

import java.util.List;
import java.util.Optional;

import group_6.e_contractor_backend.user.dto.UserDTO;
import group_6.e_contractor_backend.user.service.spec.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
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

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestParam String email, @RequestParam String password) {
        Optional<UserDTO> user = userService.loginUser(email, password);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String email) {
        userService.resetPassword(email);
        return ResponseEntity.noContent().build();
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
}

