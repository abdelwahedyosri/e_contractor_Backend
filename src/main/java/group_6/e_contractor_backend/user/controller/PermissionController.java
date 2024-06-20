package group_6.e_contractor_backend.user.controller;

import group_6.e_contractor_backend.user.entity.PermissionEntity;
import group_6.e_contractor_backend.user.service.impl.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/permissions")
@CrossOrigin(origins = "http://localhost:4200")
public class PermissionController {


    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public List<PermissionEntity> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionEntity> getPermissionById(@PathVariable Long id) {
        PermissionEntity permission = permissionService.getPermissionById(id);
        if (permission != null) {
            return ResponseEntity.ok(permission);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public PermissionEntity createPermission(@RequestBody PermissionEntity permission) {
        return permissionService.createPermission(permission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionEntity> updatePermission(@PathVariable Long id, @RequestBody PermissionEntity permissionDetails) {
        PermissionEntity updatedPermission = permissionService.updatePermission(id, permissionDetails);
        if (updatedPermission != null) {
            return ResponseEntity.ok(updatedPermission);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        boolean deleted = permissionService.deletePermission(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

