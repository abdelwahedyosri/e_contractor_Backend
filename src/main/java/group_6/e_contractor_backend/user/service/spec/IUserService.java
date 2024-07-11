package group_6.e_contractor_backend.user.service.spec;

import group_6.e_contractor_backend.user.dto.UserCreationDTO;
import group_6.e_contractor_backend.user.dto.UserDTO;
import group_6.e_contractor_backend.user.entity.UserEntity;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IUserService {

    Optional<UserEntity> registerUser(UserCreationDTO userCreationDTO);

    Optional<UserEntity> loginUser(String email, String password);

    void resetPassword(String email);

    void handleSessionExpired(String userId);

    Page<UserEntity> getUsers(int page, int size, String search, String sortColumn, String sortDirection);

    Optional<UserEntity> getUserById(String userId);

    void deleteUser(String userId);

    void updateUser(UserEntity user);

    void assignRoleToUser(String userId, String roleId);

    void removeRoleFromUser(String userId, String roleId);

    void enableTwoWayVerification(String userId);

    void disableTwoWayVerification(String userId);

    UserEntity linkSocialAccount(Long userId, String provider, String providerUserId);

    UserEntity getUserBySocialAccount(String provider, String providerUserId);
    UserEntity save(UserEntity user);

     Optional<Optional<UserEntity>> getUserFromToken(String token);

     Optional<Optional<UserEntity>> getUserByUsername(String username);
    UserEntity updateUserField(String username, String fieldName, String fieldValue);
    boolean existsByUsername(String username);
    String generateResetToken(String username);
    boolean validateResetToken(String token,String username);
    void sendPasswordResetEmail(String email);
    void updatePassword(String username, String newPassword);
    boolean existsByEmail(String email);
    String test (String test);
    Page<UserEntity> getUsersExcludingSpecificRoles(int page, int size, String search, String sortColumn, String sortDirection);

    UserEntity getConnectedUser(String token);
}
