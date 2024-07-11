package group_6.e_contractor_backend.job_offer_module.repositories;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    List<JobOffer> getJobOffersByStatus(JobOfferStatus status);
    List<JobOffer> getJobOffersByStatusOrderByUpdateDateDesc(JobOfferStatus status);

    List<JobOffer> getJobOffersByType(JobOfferType type);
    List<JobOffer> getJobOffersByIsDeleted(Boolean isDeleted);
    JobOffer getJobOfferByReference(String reference);
}
