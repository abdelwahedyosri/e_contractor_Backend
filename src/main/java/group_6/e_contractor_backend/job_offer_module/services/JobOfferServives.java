package group_6.e_contractor_backend.job_offer_module.services;
import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import group_6.e_contractor_backend.job_offer_module.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class JobOfferServives implements JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferRequirementRepository jobOfferRequirementRepository;
    @Override
    public JobOffer createJobOffer(JobOffer jobOffer) {
        return jobOfferRepository.save(jobOffer);
    }

    @Override
    public JobOfferRequirement createJobOfferRequirement(JobOfferRequirement jobOfferRequirement, Long offerId) {
        JobOffer jobOffer = jobOfferRepository.findById(offerId).orElse(null);
        jobOffer.getRequirements().add(jobOfferRequirement);
        return jobOfferRequirementRepository.save(jobOfferRequirement);
    }

    @Override
    public List<JobOffer> listJobOffersByStatus(JobOfferStatus status) {
        return jobOfferRepository.getJobOffersByStatus(status);
    }

    @Override
    public List<JobOffer> listJobOffersByType(JobOfferType type) {
        return jobOfferRepository.getJobOffersByType(type);
    }

    @Override
    public JobOffer updateJobOffer(JobOffer jobOffer) {
        return null;
    }

    @Override
    public JobOffer publishJobOffer(Long offerId) {
        return null;
    }

    @Override
    public JobOffer archiveJobOffer(Long offerId) {
        return null;
    }

    @Override
    public JobOffer deleteJobOffer(Long offerId) {
        return null;
    }
}
