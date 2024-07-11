package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.CompanyEntity;
import group_6.e_contractor_backend.user.entity.EducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEducationRepository extends JpaRepository<EducationEntity, Long> {
}
