package group_6.e_contractor_backend.job_offer_module.repositories;
import group_6.e_contractor_backend.job_offer_module.entities.JobApplicationAppointment;
import group_6.e_contractor_backend.job_offer_module.entities.JobApplicationFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationFileRepository extends JpaRepository<JobApplicationFile, Long>{
    List<JobApplicationFile> getJobApplicationFilesByJobApplicationStudentCandidateId(Long studentId);
}
