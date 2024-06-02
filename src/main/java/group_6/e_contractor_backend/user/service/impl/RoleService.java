package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.dto.RoleDTO;
import group_6.e_contractor_backend.user.entity.RoleEntity;
import group_6.e_contractor_backend.user.mapper.RoleMapper;
import group_6.e_contractor_backend.user.repository.IRoleRepository;
import group_6.e_contractor_backend.user.service.spec.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDTO> findById(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toRoleDTO);
    }

    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        RoleEntity role = roleMapper.toRole(roleDTO);
        RoleEntity savedRole = roleRepository.save(role);
        return roleMapper.toRoleDTO(savedRole);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
