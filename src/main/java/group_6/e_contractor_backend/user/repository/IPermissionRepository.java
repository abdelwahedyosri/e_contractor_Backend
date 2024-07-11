package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IPermissionRepository extends JpaRepository<PermissionEntity, Long> {
    @Query("SELECT p FROM PermissionEntity p JOIN p.roles r WHERE r.id = :roleId")
    Set<PermissionEntity> findByRoleId(@Param("roleId") Long roleId);
}
