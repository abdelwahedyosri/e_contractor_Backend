package group_6.e_contractor_backend.job_offer_module.repositories;

import group_6.e_contractor_backend.job_offer_module.entities.JobOfferConsultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobOfferConsultationRepository extends JpaRepository<JobOfferConsultation, Long> {
    Optional<JobOfferConsultation> getJobOfferConsultationsByJobOfferOfferId(Long offerId);

    Optional<JobOfferConsultation> getJobOfferConsultationsByStudentStudentId(Long studentId);
}
