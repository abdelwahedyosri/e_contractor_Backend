package group_6.e_contractor_backend.job_offer_module.services;

import group_6.e_contractor_backend.job_offer_module.entities.JobOfferSkill;

import java.util.List;
import java.util.Optional;

public interface JobOfferSkillService  {
    JobOfferSkill createJobOfferSkill(String skill,Long employerId);
    JobOfferSkill restoreJobOfferSkill(JobOfferSkill jobOfferSkill);

    List<JobOfferSkill> listJobOfferSkillsByStatus(String status);
    List<JobOfferSkill> listJobOfferSkills();
    JobOfferSkill updateJobOfferSkill(JobOfferSkill jobOfferSkill);
    JobOfferSkill deleteJobOfferSkill(Long skillId);
    JobOfferSkill restoreJobOfferSkill(Long skillId);
}

