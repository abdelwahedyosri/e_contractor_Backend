package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.dto.RoleCreationDTO;
import group_6.e_contractor_backend.user.entity.PermissionEntity;
import group_6.e_contractor_backend.user.entity.RoleEntity;
import group_6.e_contractor_backend.user.mapper.UserMapper;
import group_6.e_contractor_backend.user.repository.IPermissionRepository;
import group_6.e_contractor_backend.user.repository.IRoleRepository;
import group_6.e_contractor_backend.user.repository.ISocialAccountRepository;
import group_6.e_contractor_backend.user.repository.IUserRepository;
import group_6.e_contractor_backend.user.service.spec.IRoleService;
import group_6.e_contractor_backend.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService implements IRoleService {

    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;
    public RoleService(IRoleRepository roleRepository, IPermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<RoleEntity> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public RoleEntity createRole(RoleCreationDTO roleCreationDto) {
        Set<PermissionEntity> permissions = new HashSet<>();
        for (Long permissionId : roleCreationDto.getPermissionIds()) {
            PermissionEntity permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new RuntimeException("Permission not found"));
            permissions.add(permission);
        }

        RoleEntity roleEntity = RoleEntity.builder()
                .role(roleCreationDto.getRole())
                .permissions(permissions)
                .creationDate(LocalDateTime.now())
                .createdBy(roleCreationDto.getCreatedBy())
                .isDeleted(false)
                .build();

        return roleRepository.save(roleEntity);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }
}
