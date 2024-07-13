package group_6.e_contractor_backend.job_offer_module.controllers;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferStatus;
import group_6.e_contractor_backend.job_offer_module.repositories.*;
import group_6.e_contractor_backend.job_offer_module.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("private/job-offer")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequiredArgsConstructor
public class JobOfferPrivateApiController {
    private final JobOfferService jobOfferService;
    private final JobOfferSkillService jobOfferSkillService;
    private final JobOfferRepository jobOfferRepository;
    private final JobOfferInterestRepository jobOfferInterestRepository;
    private final JobOfferSavingRepository jobOfferSavingRepository;
    private final JobOfferConsultationRepository jobOfferConsultationRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationApointmentRepository jobApplicationApointmentRepository;

    @GetMapping("reference/{reference}")
    public Map<String, Object> listApplicationsByJobReference(@PathVariable String reference) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByJobOfferReferenceOrderByUpdateDate(reference);
        JobOffer jobOffer = jobOfferService.getJobOfferByReference(reference);
        List<JobApplicationApointment> appointmentsList = jobApplicationApointmentRepository.getJobJobApplicationApointmentsByJobApplicationJobOfferReference(reference);

        response.put("applications", list);
        response.put("jobOffer", jobOffer);
        response.put("appointments", appointmentsList);
        return response;
    }
    @GetMapping("drafts")
    public Map<String, Object> listDrafts() {
        Map<String, Object> response = new HashMap<>();
        List<JobOffer> drafts = jobOfferService.listJobOffersByStatus(JobOfferStatus.Draft);
        response.put("drafts", drafts);
        return response;
    }

    @GetMapping("published")
    public Map<String, Object> listPublished() {
        Map<String, Object> response = new HashMap<>();
        List<JobOffer> list = jobOfferRepository.findAllByStatusNotOrderByUpdateDateDesc(JobOfferStatus.Draft);
        List<JobApplication> applications = jobApplicationRepository.findAll();
        response.put("published", list);
        response.put("applications", applications);
        return response;
    }

    @GetMapping("skills")
    public Map<String, Object> listSavedSkills() {
        Map<String, Object> response = new HashMap<>();
        List<JobOfferSkill> skills = jobOfferSkillService.listJobOfferSkills();
        response.put("skills", skills);
        return response;
    }

    @PostMapping("skill/{skillId}/delete")
    public JobOfferSkill deleteSavedSkill(@PathVariable Long skillId) {
        return jobOfferSkillService.deleteJobOfferSkill(skillId);
    }

    @PostMapping("skill/{skillId}/restore")
    public JobOfferSkill restoreSavedSkill(@PathVariable Long skillId) {
        return jobOfferSkillService.restoreJobOfferSkill(skillId);
    }
}
