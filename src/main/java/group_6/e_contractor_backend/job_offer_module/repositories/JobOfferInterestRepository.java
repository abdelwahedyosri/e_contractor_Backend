package group_6.e_contractor_backend.job_offer_module.repositories;

import group_6.e_contractor_backend.job_offer_module.entities.JobOfferInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobOfferInterestRepository extends JpaRepository<JobOfferInterest, Long> {
    Optional<JobOfferInterest> getJobOfferInterestsByJobOfferOfferId(Long offerId);

    Optional<JobOfferInterest> getJobOfferInterestsByStudentCandidateId(Long studentId);
}
