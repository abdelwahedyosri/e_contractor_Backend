package group_6.e_contractor_backend.user.service.impl;

import java.util.List;
import java.util.Optional;

import group_6.e_contractor_backend.user.dto.UserDTO;
import group_6.e_contractor_backend.user.entity.RoleEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.mapper.UserMapper;
import group_6.e_contractor_backend.user.repository.IRoleRepository;
import group_6.e_contractor_backend.user.repository.IUserRepository;
import group_6.e_contractor_backend.user.service.spec.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(IUserRepository userRepository, IRoleRepository roleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<UserDTO> registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        UserEntity userEntity = userMapper.toEntity(userDTO);
        userEntity = userRepository.save(userEntity);
        return Optional.of(userMapper.toDto(userEntity));
    }

    @Override
    public Optional<UserDTO> loginUser(String email, String password) {
        Optional<UserEntity> userOpt = userRepository.findByEmailAndPassword(email, password);
        return userOpt.map(userMapper::toDto);
    }

    @Override
    public void resetPassword(String email) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        userOpt.ifPresent(user -> {
            String newPassword = "newRandomPassword"; // Generate a random password or token
            user.setPassword(newPassword);
            userRepository.save(user);
        });
    }

    @Override
    public void handleSessionExpired(String userId) {
        Optional<UserEntity> userOpt = userRepository.findById(Long.valueOf(userId));
        userOpt.ifPresent(user -> {
            user.setIsActive(false);
            userRepository.save(user);
        });
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    @Override
    public Optional<UserDTO> getUserById(String userId) {
        Optional<UserEntity> userOpt = userRepository.findById(Long.valueOf(userId));
        return userOpt.map(userMapper::toDto);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(Long.valueOf(userId));
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        Optional<UserEntity> userOpt = userRepository.findById(userDTO.getUserId());
        if (userOpt.isPresent()) {
            UserEntity userEntity = userMapper.toEntity(userDTO);
            userRepository.save(userEntity);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public void assignRoleToUser(String userId, String roleId) {
        Optional<UserEntity> userOpt = userRepository.findById(Long.valueOf(userId));
        Optional<RoleEntity> roleOpt = roleRepository.findById(Long.valueOf(roleId));
        if (userOpt.isPresent() && roleOpt.isPresent()) {
            UserEntity user = userOpt.get();
            RoleEntity role = roleOpt.get();
            user.setRole(role);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User or Role not found");
        }
    }

    @Override
    public void removeRoleFromUser(String userId, String roleId) {

    }

    @Override
    public void enableTwoWayVerification(String userId) {
        Optional<UserEntity> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setTwoWayVerificationEnabled(true);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public void disableTwoWayVerification(String userId) {
        Optional<UserEntity> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            user.setTwoWayVerificationEnabled(false);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
