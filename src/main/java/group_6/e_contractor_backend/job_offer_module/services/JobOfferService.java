package group_6.e_contractor_backend.job_offer_module.services;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;

import java.util.List;

public interface JobOfferService {
    JobOffer createJobOffer(JobOffer jobOffer,Long employerId);
    JobOffer updateJobOffer(JobOffer jobOffer);
    JobOffer getJobOfferByReference(String reference);
    JobOffer getJobOfferById(Long id);
    List<JobOffer> listJobOffers();
    List<JobOffer> listAllJobOffers();
    List<JobOffer> listJobOffersByStatus(JobOfferStatus status);
    List<JobOffer> listJobOffersByType(JobOfferType type);
    List<JobOffer> listJobOffersNotDeleted(Boolean isDeleted);
    JobOffer publishJobOffer(Long offerId);
    JobOffer archiveJobOffer(Long offerId);
    JobOffer deleteJobOffer(Long offerId);
    JobOfferRequirement createJobOfferRequirement(JobOfferRequirement jobOfferRequirement, Long offerId);

}
