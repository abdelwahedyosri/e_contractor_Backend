package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICandidateRepository extends JpaRepository<CandidateEntity, Long> {
}
