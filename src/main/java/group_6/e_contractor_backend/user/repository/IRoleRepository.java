package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRole(String role);
}
