package group_6.e_contractor_backend.job_offer_module.repositories;

import group_6.e_contractor_backend.job_offer_module.entities.JobApplicationRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRequirementRepository extends JpaRepository<JobApplicationRequirement, Long> {
}
