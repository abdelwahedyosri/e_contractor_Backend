package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermissionRepository extends JpaRepository<PermissionEntity, Long> {
}
