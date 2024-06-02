package group_6.e_contractor_backend.user.service.spec;

import group_6.e_contractor_backend.user.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<UserDTO> registerUser(UserDTO userDTO);

    Optional<UserDTO> loginUser(String username, String password);

    void resetPassword(String email);

    void handleSessionExpired(String userId);

    List<UserDTO> getAllUsers();

    Optional<UserDTO> getUserById(String userId);

    void deleteUser(String userId);

    void updateUser(UserDTO userDTO);

    void assignRoleToUser(String userId, String roleId);

    void removeRoleFromUser(String userId, String roleId);

    void enableTwoWayVerification(String userId);

    void disableTwoWayVerification(String userId);
}