package group_6.e_contractor_backend.user.service.spec;
import group_6.e_contractor_backend.user.dto.RoleCreationDTO;
import group_6.e_contractor_backend.user.entity.RoleEntity;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    List<RoleEntity> findAll();
    Optional<RoleEntity> findById(Long id);
    RoleEntity createRole(RoleCreationDTO roleCreationDto);
    void deleteById(Long id);
    boolean existsById(Long id);

}
