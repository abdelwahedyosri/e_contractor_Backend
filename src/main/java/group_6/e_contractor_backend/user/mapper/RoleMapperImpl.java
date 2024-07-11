package group_6.e_contractor_backend.user.mapper;

import group_6.e_contractor_backend.user.dto.PermissionDTO;
import group_6.e_contractor_backend.user.dto.RoleCreationDTO;
import group_6.e_contractor_backend.user.dto.RoleDTO;
import group_6.e_contractor_backend.user.entity.PermissionEntity;
import group_6.e_contractor_backend.user.entity.RoleEntity;
import group_6.e_contractor_backend.user.mapper.spec.RoleMapper;
import group_6.e_contractor_backend.user.service.impl.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapperImpl implements RoleMapper {

    @Autowired
    private PermissionService permissionService;

    @Override
    public RoleDTO toRoleDTO(RoleEntity roleEntity) {
        if (roleEntity == null) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(roleEntity.getRoleId());
        roleDTO.setRole(roleEntity.getRole());
        roleDTO.setRoleDescription(roleEntity.getRoleDescription());
        roleDTO.setCreationDate(roleEntity.getCreationDate());
        roleDTO.setCreatedBy(roleEntity.getCreatedBy());
        roleDTO.setIsDeleted(roleEntity.getIsDeleted());
        roleDTO.setPermissions(roleEntity.getPermissions().stream()
                .map(this::toPermissionDTO)
                .collect(Collectors.toSet()));

        return roleDTO;
    }

    @Override
    public RoleEntity toRoleEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(roleDTO.getRoleId());
        roleEntity.setRole(roleDTO.getRole());
        roleEntity.setRoleDescription(roleDTO.getRoleDescription());
        roleEntity.setCreationDate(roleDTO.getCreationDate());
        roleEntity.setCreatedBy(roleDTO.getCreatedBy());
        roleEntity.setIsDeleted(roleDTO.getIsDeleted());
        roleEntity.setPermissions(roleDTO.getPermissions().stream()
                .map(this::toPermissionEntity)
                .collect(Collectors.toSet()));

        return roleEntity;
    }

    @Override
    public RoleEntity toRoleEntity(RoleCreationDTO roleCreationDTO) {
        if (roleCreationDTO == null) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole(roleCreationDTO.getRole());
        roleEntity.setCreationDate(LocalDateTime.now());
        roleEntity.setCreatedBy(roleCreationDTO.getCreatedBy());
        roleEntity.setIsDeleted(false);
        Set<PermissionEntity> permissions = permissionService.getPermissionsByIds(roleCreationDTO.getPermissionIds());
        System.out.println("Permissions_names: " + permissions.stream()
                .map(PermissionEntity::getName)
                .collect(Collectors.joining(", ")));
        roleEntity.setPermissions(permissions);

        return roleEntity;
    }

    @Override
    public PermissionDTO toPermissionDTO(PermissionEntity permissionEntity) {
        if (permissionEntity == null) {
            return null;
        }

        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setId(permissionEntity.getId());
        permissionDTO.setName(permissionEntity.getName());

        return permissionDTO;
    }

    @Override
    public PermissionEntity toPermissionEntity(PermissionDTO permissionDTO) {
        if (permissionDTO == null) {
            return null;
        }

        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setId(permissionDTO.getId());
        permissionEntity.setName(permissionDTO.getName());

        return permissionEntity;
    }
}
