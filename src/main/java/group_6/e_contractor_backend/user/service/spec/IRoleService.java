package group_6.e_contractor_backend.user.service.spec;

import group_6.e_contractor_backend.user.dto.RoleDTO;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    List<RoleDTO> findAll();
    Optional<RoleDTO> findById(Long id);
    RoleDTO save(RoleDTO roleDTO);
    void deleteById(Long id);
}
