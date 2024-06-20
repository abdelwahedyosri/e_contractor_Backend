package group_6.e_contractor_backend.user.service.spec;

import group_6.e_contractor_backend.user.entity.PermissionEntity;

import java.util.List;

public interface IPermissionService {

    List<PermissionEntity> getAllPermissions();

    PermissionEntity getPermissionById(Long id);

    PermissionEntity createPermission(PermissionEntity permission);

    PermissionEntity updatePermission(Long id, PermissionEntity permissionDetails);

    boolean deletePermission(Long id);
}
