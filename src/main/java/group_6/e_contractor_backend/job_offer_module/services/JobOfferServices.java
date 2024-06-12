package group_6.e_contractor_backend.job_offer_module.services;
import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import group_6.e_contractor_backend.job_offer_module.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
@Service
@RequiredArgsConstructor
public class JobOfferServices implements JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferRequirementRepository jobOfferRequirementRepository;
    private final JobOfferSkillService jobOfferSkillService;
    @Override
    public JobOffer createJobOffer(JobOffer jobOffer) {
        LocalDate currentDate = LocalDate.now();

        jobOffer.setCreatedBy(1L);
        jobOffer.setCreationDate(currentDate);
        jobOffer.setUpdatedBy(1L);
        jobOffer.setUpdateDate(currentDate);

        if (JobOfferStatus.Published.equals(jobOffer.getStatus())) {
            jobOffer.setPublishingDate(currentDate);
        }

        JobOffer savedJobOffer = jobOfferRepository.save(jobOffer);
        if (jobOffer.getSkills() != null && !jobOffer.getSkills().isEmpty()) {
            List<String> skillsList = Arrays.asList(jobOffer.getSkills().split(","));
            for (String skill : skillsList) {
                jobOfferSkillService.createJobOfferSkill(skill);
            }
        }
        if (jobOffer.getRequirements() != null && !jobOffer.getRequirements().isEmpty()) {
            for (JobOfferRequirement requirement : jobOffer.getRequirements()) {
                requirement.setJobOffer(jobOffer);
                jobOfferRequirementRepository.save(requirement);
            }
        }
        return savedJobOffer;
    }


    @Override
    public JobOffer getJobOfferByReference(String reference) {
        return jobOfferRepository.getJobOfferByReference(reference);
    }

    @Override
    public JobOffer getJobOfferById(Long id) {
        return null;
    }
    @Override
    public JobOfferRequirement createJobOfferRequirement(JobOfferRequirement jobOfferRequirement, Long offerId) {
        JobOffer jobOffer = jobOfferRepository.findById(offerId).orElse(null);
        if(jobOffer!=null) {
            jobOffer.getRequirements().add(jobOfferRequirement);
            return jobOfferRequirementRepository.save(jobOfferRequirement);
        }else{
            return null;
        }
    }

    @Override
    public List<JobOffer> listJobOffers() {
        return null;
    }

    @Override
    public List<JobOffer> listAllJobOffers() {
        return null;
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
    public List<JobOffer> listJobOffersNotDeleted(Boolean isDeleted) {
        return jobOfferRepository.getJobOffersByIsDeleted(isDeleted);
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
