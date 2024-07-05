package group_6.e_contractor_backend.user.mapper;

import group_6.e_contractor_backend.user.dto.UserCreationDTO;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.mapper.spec.UserMapper;
import group_6.e_contractor_backend.user.service.impl.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private PermissionService permissionService;



    @Override
    public UserEntity toEntity(UserCreationDTO userCreationDTO) {
        if (userCreationDTO == null) {
            return null;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userCreationDTO.getFirstName());
        userEntity.setLastName(userCreationDTO.getLastName());
        userEntity.setPhoneNumber(userCreationDTO.getPhoneNumber());
        userEntity.setEmail(userCreationDTO.getEmail());
        userEntity.setUsername(userCreationDTO.getUsername());
        userEntity.setGender(userCreationDTO.getGender());
        userEntity.setDob(userCreationDTO.getDob());
        userEntity.setLocation(userCreationDTO.getLocation());
        userEntity.setPassword(userCreationDTO.getPassword());
        userEntity.setRole(userCreationDTO.getRole());
        userEntity.setIsActive(true);
        userEntity.setCreationDate(LocalDateTime.now());
        userEntity.setCreatedBy(userCreationDTO.getCreatedBy());
        userEntity.setIsDeleted(false);
        userEntity.setTwoWayVerificationEnabled(false);
        userEntity.setAvatar(userCreationDTO.getAvatar());
        return userEntity;
    }


}
