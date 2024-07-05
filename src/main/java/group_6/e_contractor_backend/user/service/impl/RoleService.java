package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.dto.RoleCreationDTO;
import group_6.e_contractor_backend.user.dto.RoleDTO;
import group_6.e_contractor_backend.user.entity.PermissionEntity;
import group_6.e_contractor_backend.user.entity.RoleEntity;
import group_6.e_contractor_backend.user.mapper.RoleMapperImpl;
import group_6.e_contractor_backend.user.repository.IPermissionRepository;
import group_6.e_contractor_backend.user.repository.IRoleRepository;
import group_6.e_contractor_backend.user.service.spec.IRoleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;
    private final RoleMapperImpl roleMapperImpl;
    public RoleService(IRoleRepository roleRepository, IPermissionRepository permissionRepository, RoleMapperImpl roleMapperImpl) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapperImpl = roleMapperImpl;
    }

    @Override
    public List<RoleDTO> findAll() {
        List<RoleEntity> roles = roleRepository.findAll();
        System.out.println("roles"+roles);
        return roles.stream().map(roleMapperImpl::toRoleDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<RoleEntity> findById(Long id) {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(id);
        if (roleEntityOptional.isPresent()) {
            RoleEntity roleEntity = roleEntityOptional.get();
            Set<PermissionEntity> permissions = permissionRepository.findByRoleId(roleEntity.getRoleId());
            roleEntity.setPermissions(permissions);
        }
        return roleEntityOptional;
    }

    @Override
    public RoleEntity createRole(RoleCreationDTO roleCreationDto) {

        RoleEntity roleEntity = roleMapperImpl.toRoleEntity(roleCreationDto);
        return roleRepository.save(roleEntity);
    }

    @Override
    public void deleteById(Long id) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found for ID: " + id));
        role.setPermissions(new HashSet<>());  // Clear permissions first
        roleRepository.save(role);
        roleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }


    @Override
    public RoleEntity updateRole(Long id, RoleCreationDTO roleCreationDto) {
        RoleEntity existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found for ID: " + id));

        existingRole.setRole(roleCreationDto.getRole());
        existingRole.setCreatedBy(roleCreationDto.getCreatedBy());

        Set<PermissionEntity> permissions = new HashSet<>();
        for (Long permissionId : roleCreationDto.getPermissionIds()) {
            PermissionEntity permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new RuntimeException("Permission not found for ID: " + permissionId));
            permissions.add(permission);
        }
        existingRole.setPermissions(permissions);

        return roleRepository.save(existingRole);
    }
    @Override
    public Optional<RoleEntity> findByRole(String roleName) {
        return roleRepository.findByRole(roleName);
    }

}
