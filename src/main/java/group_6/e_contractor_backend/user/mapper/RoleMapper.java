package group_6.e_contractor_backend.user.mapper;

import group_6.e_contractor_backend.user.dto.RoleCreationDTO;
import group_6.e_contractor_backend.user.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleCreationDTO toRoleDTO(RoleEntity role);

    RoleEntity toRole(RoleCreationDTO roleDTO);
}

