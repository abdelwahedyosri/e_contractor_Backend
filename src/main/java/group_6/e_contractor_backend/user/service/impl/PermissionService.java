package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.entity.PermissionEntity;
import group_6.e_contractor_backend.user.entity.RoleEntity;
import group_6.e_contractor_backend.user.repository.IPermissionRepository;
import group_6.e_contractor_backend.user.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final IPermissionRepository permissionRepository;
    private final IRoleRepository roleRepository;
    public PermissionService(IPermissionRepository permissionRepository,IRoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository=roleRepository;
    }

    public List<PermissionEntity> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public PermissionEntity getPermissionById(Long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    public PermissionEntity createPermission(PermissionEntity permission) {
        return permissionRepository.save(permission);
    }

    public PermissionEntity updatePermission(Long id, PermissionEntity permissionDetails) {
        PermissionEntity permission = getPermissionById(id);
        if (permission != null) {
            permission.setName(permissionDetails.getName());
            return permissionRepository.save(permission);
        }
        return null;
    }
    public Set<PermissionEntity> getPermissionsByIds(Set<Long> ids) {
        return new HashSet<>(permissionRepository.findAllById(ids));
    }
    public boolean deletePermission(Long id) {
        if (permissionRepository.existsById(id)) {
            // Ensure no role is using this permission before deleting
            List<RoleEntity> rolesUsingPermission = roleRepository.findAll().stream()
                    .filter(role -> role.getPermissions().stream()
                            .anyMatch(permission -> permission.getId().equals(id)))
                    .collect(Collectors.toList());

            for (RoleEntity role : rolesUsingPermission) {
                role.getPermissions().removeIf(permission -> permission.getId().equals(id));
                roleRepository.save(role);
            }

            permissionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
