package group_6.e_contractor_backend.job_offer_module.services;

import group_6.e_contractor_backend.job_offer_module.entities.Employer;
import group_6.e_contractor_backend.job_offer_module.entities.JobOfferSkill;
import group_6.e_contractor_backend.job_offer_module.repositories.EmployerRepository;
import group_6.e_contractor_backend.job_offer_module.repositories.JobOfferSkillRepository;
import group_6.e_contractor_backend.user.entity.CompanyEntity;
import group_6.e_contractor_backend.user.repository.ICompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobOfferSkillServices implements JobOfferSkillService {
    private final JobOfferSkillRepository jobOfferSkillRepository;
    private final ICompanyRepository companyRepository;

    @Override
    public JobOfferSkill createJobOfferSkill(String skill,Long employerId) {
        JobOfferSkill jobOfferSkill = new JobOfferSkill();
        jobOfferSkill.setLabel(skill.trim()); // Trim to remove leading/trailing whitespaces
        Optional<JobOfferSkill> existingSkill = jobOfferSkillRepository.getJobOfferSkillByLabel(jobOfferSkill.getLabel());

        if (existingSkill.isPresent()) {
            return null;
        }

        jobOfferSkill.setCreationDate(LocalDateTime.now());
        jobOfferSkill.setCreatedBy(1L);
        jobOfferSkill.setIsDeleted(false);
        CompanyEntity employer = companyRepository.findById(employerId).orElse(null);
        jobOfferSkill.setEmployer(employer);

        return jobOfferSkillRepository.save(jobOfferSkill);
    }

    @Override
    public JobOfferSkill restoreJobOfferSkill(JobOfferSkill jobOfferSkill) {
        Optional<JobOfferSkill> existingSkill = jobOfferSkillRepository.getJobOfferSkillByLabel(jobOfferSkill.getLabel());

        if (existingSkill.isPresent()) {
            JobOfferSkill skill = existingSkill.get();
            if (skill.getIsDeleted()) {
                skill.setIsDeleted(false);
                return jobOfferSkillRepository.save(skill);
            }
            return null;
        }
        return null;
    }

    @Override
    public List<JobOfferSkill> listJobOfferSkills() {
        return jobOfferSkillRepository.findAllByOrderByLabelAsc();
    }

    @Override
    public List<JobOfferSkill> listJobOfferSkillsByStatus(String status) {
        if ("deleted".equalsIgnoreCase(status)) {
            return jobOfferSkillRepository.getJobOfferSkillByIsDeleted(true);
        } else {
            return jobOfferSkillRepository.getJobOfferSkillByIsDeleted(false);
        }
    }

    @Override
    public JobOfferSkill updateJobOfferSkill(JobOfferSkill jobOfferSkill) {
        return null;
    }

    @Override
    public JobOfferSkill deleteJobOfferSkill(Long skillId) {
        JobOfferSkill skill = jobOfferSkillRepository.getJobOfferSkillBySkillId(skillId);
        skill.setIsDeleted(true);
        jobOfferSkillRepository.save(skill);
        return skill;
    }

    @Override
    public JobOfferSkill restoreJobOfferSkill(Long skillId) {
        JobOfferSkill skill = jobOfferSkillRepository.getJobOfferSkillBySkillId(skillId);
        skill.setIsDeleted(false);
        jobOfferSkillRepository.save(skill);
        return skill;
    }
}
