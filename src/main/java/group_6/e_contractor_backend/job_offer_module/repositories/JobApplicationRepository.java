package group_6.e_contractor_backend.job_offer_module.repositories;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationStatus;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> getJobApplicationsByJobOfferOfferId(Long offerId);

    List<JobApplication> getJobApplicationsByJobOfferReferenceOrderByUpdateDate(String reference);
    List<JobApplication> getJobApplicationsByStudentStudentId(Long studentId);

    List<JobApplication> getJobApplicationsByJobOfferEmployerEmployerIdOrderByUpdateDate(Long employerId);
    List<JobApplication> getJobApplicationsByStudentStudentIdAndApplicationStatusIn(Long offerId, List<JobApplicationStatus> statuses);

    JobApplication getJobApplicationByReference(String reference);

    List<JobApplication> findALLByOrderByUpdateDate();

}