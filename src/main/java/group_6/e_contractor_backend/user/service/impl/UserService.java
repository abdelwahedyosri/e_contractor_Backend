package group_6.e_contractor_backend.user.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import group_6.e_contractor_backend.user.dto.UserCreationDTO;
import group_6.e_contractor_backend.user.dto.UserDTO;
import group_6.e_contractor_backend.user.entity.RoleEntity;
import group_6.e_contractor_backend.user.entity.SocialAccountEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.mapper.UserMapperImpl;
import group_6.e_contractor_backend.user.repository.IRoleRepository;
import group_6.e_contractor_backend.user.repository.ISocialAccountRepository;
import group_6.e_contractor_backend.user.repository.IUserRepository;
import group_6.e_contractor_backend.user.service.spec.IUserService;
import group_6.e_contractor_backend.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.SimpleMailMessage;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final UserMapperImpl userMapper;
    private final ISocialAccountRepository socialAccountRepository;
    private final JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender mailSender;
    @Autowired
    public UserService(IUserRepository userRepository, IRoleRepository roleRepository, UserMapperImpl userMapper, ISocialAccountRepository socialAccountRepository , JwtUtil jwtUtil, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.socialAccountRepository = socialAccountRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder=passwordEncoder;
        this.mailSender = mailSender;
    }
    public String test (String test){
        return passwordEncoder.encode(test);
    }
    public Optional<UserEntity> registerUser(UserCreationDTO userCreationDTO) {


        // Map the UserCreationDTO to UserEntity
        UserEntity userEntity = userMapper.toEntity(userCreationDTO);

        // Set the password after encoding it
        userEntity.setPassword(passwordEncoder.encode(userCreationDTO.getPassword()));

        // Handle default values for nullable fields if they are not set in the DTO
        if (userEntity.getIsActive() == null) {
            userEntity.setIsActive(true); // Default value
        }
        if (userEntity.getTwoWayVerificationEnabled() == null) {
            userEntity.setTwoWayVerificationEnabled(false); // Default value
        }
        if (userEntity.getCreationDate() == null) {
            userEntity.setCreationDate(LocalDateTime.now()); // Set current time as default
        }
        if (userEntity.getCreatedBy() == null) {
            userEntity.setCreatedBy("system"); // Default value
        }
        if (userEntity.getIsDeleted() == null) {
            userEntity.setIsDeleted(false); // Default value
        }

        // Save the user entity to the repository
        userEntity = userRepository.save(userEntity);

        // Send a registration email
        sendRegistrationEmail(userEntity);

        return Optional.of(userEntity);
    }

    private void sendRegistrationEmail(UserEntity userEntity) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEntity.getEmail());
        message.setSubject("Registration Confirmation");
        message.setText("Dear " + userEntity.getUsername() + ",\n\nThank you for registering!");

        mailSender.send(message);
    }

    @Override
    public Optional<UserEntity> loginUser(String email, String password) {
        Optional<UserEntity> userOpt = userRepository.findByEmailAndPassword(email, password);
        return userOpt;
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
    public Page<UserEntity> getUsers(int page, int size, String search, String sortColumn, String sortDirection) {
        Pageable pageable;
        if (sortColumn != null && !sortColumn.isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortColumn).descending() : Sort.by(sortColumn).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }

        if (search != null && !search.isEmpty()) {
            return userRepository.findBySearchTerm(search, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }


    @Override
    public Optional<UserEntity> getUserById(String userId) {
        Optional<UserEntity> userOpt = userRepository.findById(Long.valueOf(userId));
        return userOpt;
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(Long.valueOf(userId));
    }

    @Override
    public void updateUser(UserEntity user) {
        Optional<UserEntity> userOpt = userRepository.findById(user.getUserId());
        if (userOpt.isPresent()) {
            userRepository.save(user);
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

    @Transactional
    public UserEntity linkSocialAccount(Long userId, String provider, String providerUserId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        SocialAccountEntity socialAccount = new SocialAccountEntity();
        socialAccount.setProvider(provider);
        socialAccount.setProviderUserId(providerUserId);
        socialAccount.setUser(user);
        user.getSocialAccounts().add(socialAccount);
        return userRepository.save(user);
    }

    public UserEntity getUserBySocialAccount(String provider, String providerUserId) {
        SocialAccountEntity socialAccount = socialAccountRepository.findByProviderAndProviderUserId(provider, providerUserId);
        return socialAccount != null ? socialAccount.getUser() : null;
    }
    @Transactional
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
    public Optional<Optional<UserEntity>> getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public Optional<Optional<UserEntity>> getUserFromToken(String token) {
        String username = jwtUtil.extractUsername(token);
        return getUserByUsername(username);
    }

    public UserEntity updateUserField(String username, String fieldName, String fieldValue) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get(); // Get the UserEntity from Optional
            switch (fieldName) {
                case "firstName":
                    user.setFirstName(fieldValue);
                    break;
                case "lastName":
                    user.setLastName(fieldValue);
                    break;
                case "email":
                    user.setEmail(fieldValue);
                    break;
                case "gender":
                    user.setGender(fieldValue);
                    break;
                case "mobileNumber":
                    user.setPhoneNumber(Long.valueOf(fieldValue)); // Corrected field setter
                    break;
                case "dob":
                    user.setDob(fieldValue);
                    break;
                case "location":
                    user.setLocation(fieldValue);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field name: " + fieldName);
            }
            userRepository.save(user); // Save the modified user entity
            return user;
        }
        return null; // Return null if user not found
    }


    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public String generateResetToken(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email); // Assuming UserRepository has this method
        if (user.isPresent()) {
            return jwtUtil.generateToken(user.get().getUsername());
        } else {
            throw new IllegalArgumentException("User not found with the provided email.");
        }
    }

    // Validate reset token
    @Override
    public boolean validateResetToken(String token,String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        return jwtUtil.validateToken(token,user.get().getUsername());
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        String token = generateResetToken(email);
        String resetUrl = "http://localhost:4200/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetUrl);

        mailSender.send(message);
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword)); // Encode the password before saving
            userRepository.save(user); // Save the UserEntity
        } else {
            // Handle the case when the user is not found
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    @Override
    public Page<UserEntity> getUsersExcludingSpecificRoles(int page, int size, String search, String sortColumn, String sortDirection) {
        Pageable pageable;
        List<String> excludedRoles = Arrays.asList("company", "candidate"); // Roles to exclude

        if (sortColumn != null && !sortColumn.isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortColumn).descending() : Sort.by(sortColumn).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }

        return userRepository.findByRolesNotIn(excludedRoles, pageable);
    }

    @Override
    public UserEntity getConnectedUser(String token) {
        String username = jwtUtil.extractUsername(token);
        return userRepository.getUserEntityByUsername(username);
    }
}
