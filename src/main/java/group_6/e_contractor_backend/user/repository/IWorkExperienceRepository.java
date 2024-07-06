package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.CompanyEntity;
import group_6.e_contractor_backend.user.entity.WorkExperienceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkExperienceRepository extends JpaRepository<WorkExperienceEntity, Long> {
}
