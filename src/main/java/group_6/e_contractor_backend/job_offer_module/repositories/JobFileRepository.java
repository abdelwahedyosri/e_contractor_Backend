package group_6.e_contractor_backend.job_offer_module.repositories;

import group_6.e_contractor_backend.job_offer_module.entities.JobFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobFileRepository extends JpaRepository<JobFile, Long> {
}
