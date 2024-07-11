package group_6.e_contractor_backend.job_offer_module.services;
import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import group_6.e_contractor_backend.job_offer_module.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobOfferServices implements JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferRequirementRepository jobOfferRequirementRepository;
    private final JobOfferSkillService jobOfferSkillService;
    private final EmployerRepository employerRepository;
    @Override
    public JobOffer createJobOffer(JobOffer jobOffer,Long employerId) {
        LocalDateTime currentDate = LocalDateTime.now();
        Employer employer = employerRepository.findById(employerId).orElse(null);
        jobOffer.setEmployer(employer);
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
                jobOfferSkillService.createJobOfferSkill(skill,employerId);
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
    public JobOffer updateJobOffer(JobOffer jobOffer) {
        Optional<JobOffer> existingJobOfferOptional = jobOfferRepository.findById(jobOffer.getOfferId());
        if (!existingJobOfferOptional.isPresent()) {
            return null;
        }

        JobOffer existingJobOffer = existingJobOfferOptional.get();

        existingJobOffer.setJobTitle(jobOffer.getJobTitle());
        existingJobOffer.setDescription(jobOffer.getDescription());
        existingJobOffer.setTasksDescription(jobOffer.getTasksDescription());
        existingJobOffer.setType(jobOffer.getType());
        existingJobOffer.setWorkspaceType(jobOffer.getWorkspaceType());
        existingJobOffer.setJobContract(jobOffer.getJobContract());
        existingJobOffer.setStatus(jobOffer.getStatus());
        existingJobOffer.setCountry(jobOffer.getCountry());
        existingJobOffer.setCity(jobOffer.getCity());
        existingJobOffer.setLocation(jobOffer.getLocation());
        existingJobOffer.setRenumeration(jobOffer.getRenumeration());
        existingJobOffer.setRenumerationPeriod(jobOffer.getRenumerationPeriod());
        existingJobOffer.setRenumerationCurrency(jobOffer.getRenumerationCurrency());
        existingJobOffer.setCategory(jobOffer.getCategory());
        existingJobOffer.setOpenPositions(jobOffer.getOpenPositions());
        existingJobOffer.setDeadline(jobOffer.getDeadline());
        existingJobOffer.setEducationLevel(jobOffer.getEducationLevel());
        existingJobOffer.setExperience(jobOffer.getExperience());
        existingJobOffer.setSkills(jobOffer.getSkills());
        existingJobOffer.setLanguage(jobOffer.getLanguage());
        existingJobOffer.setAllowSimpleApplications(jobOffer.getAllowSimpleApplications());

        existingJobOffer.setUpdatedBy(1L);
        existingJobOffer.setUpdateDate(LocalDateTime.now());

        if (JobOfferStatus.Published.equals(jobOffer.getStatus()) && existingJobOffer.getPublishingDate() == null) {
            existingJobOffer.setPublishingDate(LocalDateTime.now());
        }

        // Save the updated job offer
        JobOffer updatedJobOffer = jobOfferRepository.save(existingJobOffer);
        Long employerId = existingJobOffer.getEmployer().getEmployerId();
        // Update or create skills
        if (jobOffer.getSkills() != null && !jobOffer.getSkills().isEmpty()) {
            List<String> skillsList = Arrays.asList(jobOffer.getSkills().split(","));
            for (String skill : skillsList) {
                jobOfferSkillService.createJobOfferSkill(skill,employerId);
            }
        }

        if (jobOffer.getRequirements() != null && !jobOffer.getRequirements().isEmpty()) {

            List<JobOfferRequirement> currentRequirements = jobOfferRequirementRepository.getJobOfferRequirementsByJobOfferOfferId(existingJobOffer.getOfferId());

           List<String> incomingRequirementLabels = jobOffer.getRequirements().stream()
                    .map(JobOfferRequirement::getLabel)
                    .collect(Collectors.toList());

            for (JobOfferRequirement currentRequirement : currentRequirements) {
                if (!incomingRequirementLabels.contains(currentRequirement.getLabel())) {
                    jobOfferRequirementRepository.delete(currentRequirement);
                }
            }

            for (JobOfferRequirement requirement : jobOffer.getRequirements()) {
                Optional<JobOfferRequirement> existingRequirement = jobOfferRequirementRepository.getJobOfferRequirementByLabelAndJobOfferOfferId(requirement.getLabel(), existingJobOffer.getOfferId());
                if (existingRequirement.isPresent()) {
                    JobOfferRequirement requirement1 = existingRequirement.get();
                    requirement1.setValue(requirement.getValue());
                }
                requirement.setJobOffer(existingJobOffer);
                jobOfferRequirementRepository.save(requirement);
            }
        }

        return updatedJobOffer;
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
        return jobOfferRepository.getJobOffersByStatusOrderByUpdateDateDesc(status);
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
