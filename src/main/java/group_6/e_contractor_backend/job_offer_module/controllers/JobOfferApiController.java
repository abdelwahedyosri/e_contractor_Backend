package group_6.e_contractor_backend.job_offer_module.controllers;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferStatus;
import group_6.e_contractor_backend.job_offer_module.repositories.*;
import group_6.e_contractor_backend.job_offer_module.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("job-offer")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class JobOfferApiController {

    private final JobOfferService jobOfferService;
    private final JobOfferSkillService jobOfferSkillService;

    @PostMapping("create")
    public JobOffer createJobOffer(@RequestBody JobOffer jobOffer){
        return  jobOfferService.createJobOffer(jobOffer);
    }

    @GetMapping("reference/{reference}")
    public JobOffer getJobOfferByReference(@PathVariable String reference) {
        return jobOfferService.getJobOfferByReference(reference);
    }

    @PostMapping("update")
    public JobOffer updateJobOffer(@RequestBody JobOffer jobOffer){
        return  jobOfferService.updateJobOffer(jobOffer);
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
        List<JobOffer> list = jobOfferService.listJobOffersByStatus(JobOfferStatus.Published);
        response.put("published", list);
        return response;
    }
    @GetMapping("skills/{status}")
    public Map<String, Object> listSavedSkills(@PathVariable String status) {
        Map<String, Object> response = new HashMap<>();
        List<JobOfferSkill> skills = jobOfferSkillService.listJobOfferSkillsByStatus(status);
        response.put("skills", skills);
        return response;
    }
}