package group_6.e_contractor_backend.job_offer_module.repositories;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    List<JobOffer> getJobOffersByStatus(JobOfferStatus status);
    List<JobOffer> getJobOffersByStatusOrderByUpdateDateDesc(JobOfferStatus status);
    List<JobOffer> findByEmployerCompanyIdAndStatusOrderByUpdateDateDesc(Long employerId, JobOfferStatus status);
    List<JobOffer> getJobOffersByType(JobOfferType type);
    List<JobOffer> getJobOffersByIsDeleted(Boolean isDeleted);
    JobOffer getJobOfferByReference(String reference);
    List<JobOffer> getJobOffersByEmployerCompanyId(Long employerId);
    List<JobOffer> findAllByDeadlineBeforeAndStatusNot(LocalDateTime deadline,JobOfferStatus status);

    List<JobOffer> findAllByDeadlineBeforeAndStatus(LocalDateTime deadline,JobOfferStatus status);
}
