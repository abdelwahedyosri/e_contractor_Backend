package group_6.e_contractor_backend.job_offer_module.repositories;

import group_6.e_contractor_backend.job_offer_module.entities.JobOfferSaving;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobOfferSavingRepository extends JpaRepository<JobOfferSaving, Long> {
    Optional<JobOfferSaving> getJobOfferSavingsByJobOfferOfferId(Long offerId);

    Optional<JobOfferSaving> getJobOfferSavingsByStudentStudentId(Long studentId);
}
