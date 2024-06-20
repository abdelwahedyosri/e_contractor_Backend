package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.entity.PermissionEntity;
import group_6.e_contractor_backend.user.repository.IPermissionRepository;
import group_6.e_contractor_backend.user.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PermissionService {

    private final IPermissionRepository permissionRepository;

    public PermissionService(IPermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
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

    public boolean deletePermission(Long id) {
        if (permissionRepository.existsById(id)) {
            permissionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
