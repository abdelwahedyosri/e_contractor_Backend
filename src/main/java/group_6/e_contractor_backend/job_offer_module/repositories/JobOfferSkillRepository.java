package group_6.e_contractor_backend.job_offer_module.repositories;
import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobOfferSkillRepository extends JpaRepository<JobOfferSkill, Long>{
    Optional<JobOfferSkill> getJobOfferSkillByLabel(String label);
    JobOfferSkill getJobOfferSkillBySkillId(Long skillId);
    List<JobOfferSkill> findAllByOrderByLabelAsc();
    List<JobOfferSkill> getJobOfferSkillByIsDeleted(boolean isDeleted);
}
