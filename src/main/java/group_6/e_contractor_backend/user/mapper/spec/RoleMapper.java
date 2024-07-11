package group_6.e_contractor_backend.user.mapper.spec;

import group_6.e_contractor_backend.user.dto.PermissionDTO;
import group_6.e_contractor_backend.user.dto.RoleCreationDTO;
import group_6.e_contractor_backend.user.dto.RoleDTO;
import group_6.e_contractor_backend.user.entity.PermissionEntity;
import group_6.e_contractor_backend.user.entity.RoleEntity;

public interface RoleMapper {
    RoleDTO toRoleDTO(RoleEntity roleEntity);
    RoleEntity toRoleEntity(RoleDTO roleDTO);
    RoleEntity toRoleEntity(RoleCreationDTO roleCreationDTO);
    PermissionDTO toPermissionDTO(PermissionEntity permissionEntity);
    PermissionEntity toPermissionEntity(PermissionDTO permissionDTO);
}
