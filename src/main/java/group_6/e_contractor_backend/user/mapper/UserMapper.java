package group_6.e_contractor_backend.user.mapper;

import group_6.e_contractor_backend.user.dto.UserDTO;
import group_6.e_contractor_backend.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "role.roleId", target = "roleId")
    @Mapping(source = "twoWayVerificationEnabled", target = "twoWayVerificationEnabled")
    UserDTO toDto(UserEntity userEntity);

    @Mapping(source = "roleId", target = "role.roleId")
    @Mapping(source = "twoWayVerificationEnabled", target = "twoWayVerificationEnabled")
    UserEntity toEntity(UserDTO userDTO);

    List<UserDTO> toDtoList(List<UserEntity> userEntities);
}
