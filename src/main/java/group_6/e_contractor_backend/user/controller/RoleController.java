package group_6.e_contractor_backend.user.controller;


import group_6.e_contractor_backend.user.dto.PermissionDTO;
import group_6.e_contractor_backend.user.dto.RoleCreationDTO;
import group_6.e_contractor_backend.user.dto.RoleDTO;
import group_6.e_contractor_backend.user.entity.RoleEntity;
import group_6.e_contractor_backend.user.entity.PermissionEntity;
import group_6.e_contractor_backend.user.mapper.RoleMapperImpl;
import group_6.e_contractor_backend.user.service.impl.RoleService;
import group_6.e_contractor_backend.user.service.impl.UserService;
import group_6.e_contractor_backend.user.service.spec.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "http://localhost:4200")
public class RoleController {

    private final RoleService roleService;
    private final RoleMapperImpl roleMapper;

    public RoleController(RoleService roleService,RoleMapperImpl roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleEntity> getRoleById(@PathVariable Long id) {
        Optional<RoleEntity> role = roleService.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoleEntity> createRole(@RequestBody RoleCreationDTO roleCreationDto) {
        RoleEntity roleEntity = roleService.createRole(roleCreationDto);
        return ResponseEntity.ok(roleEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        if (roleService.existsById(id)) {
            roleService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleCreationDTO roleCreationDto) {
        RoleEntity updatedRole = roleService.updateRole(id, roleCreationDto);
        RoleDTO roleDTO = roleMapper.toRoleDTO(updatedRole);
        return ResponseEntity.ok(roleDTO);
    }

    @GetMapping("/name/{roleName}")
    public ResponseEntity<RoleEntity> getRoleByRoleName(@PathVariable String roleName) {
        Optional<RoleEntity> role = roleService.findByRole(roleName);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
