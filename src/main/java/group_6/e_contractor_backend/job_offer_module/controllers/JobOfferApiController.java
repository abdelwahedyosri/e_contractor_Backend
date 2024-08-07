package group_6.e_contractor_backend.job_offer_module.controllers;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferStatus;
import group_6.e_contractor_backend.job_offer_module.repositories.*;
import group_6.e_contractor_backend.job_offer_module.services.*;
import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.repository.ICandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("job-offer")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequiredArgsConstructor
public class JobOfferApiController {

    private final JobOfferService jobOfferService;
    private final JobOfferSkillService jobOfferSkillService;
    private final JobOfferRepository jobOfferRepository;
    private final JobOfferInterestRepository jobOfferInterestRepository;
    private final JobOfferSavingRepository jobOfferSavingRepository;
    private final JobOfferConsultationRepository jobOfferConsultationRepository;
    private final ICandidateRepository candidateRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationApointmentRepository jobApplicationApointmentRepository;


    @PostMapping("create/{employerId}")
    public JobOffer createJobOffer(@RequestBody JobOffer jobOffer,@PathVariable Long employerId){
        return  jobOfferService.createJobOffer(jobOffer,employerId);
    }

    @GetMapping("reference/{reference}")
    public JobOffer getJobOfferByReference(@PathVariable String reference) {
        return jobOfferService.getJobOfferByReference(reference);
    }

    @PostMapping("update")
    public JobOffer updateJobOffer(@RequestBody JobOffer jobOffer){
        return  jobOfferService.updateJobOffer(jobOffer);
    }

    @GetMapping("drafts/{employerId}")
    public Map<String, Object> listDrafts(@PathVariable Long employerId) {
        Map<String, Object> response = new HashMap<>();
        List<JobOffer> drafts = jobOfferRepository.findByEmployerCompanyIdAndStatusOrderByUpdateDateDesc(employerId,JobOfferStatus.Draft);
        response.put("drafts", drafts);
        return response;
    }

    @GetMapping("published")
    public Map<String, Object> listPublished() {
        Map<String, Object> response = new HashMap<>();
        List<JobOffer> list = jobOfferService.listJobOffersByStatus(JobOfferStatus.Published);
        List<JobApplication> applications = jobApplicationRepository.findAll();
        response.put("published", list);
        response.put("applications", applications);
        return response;
    }

    @GetMapping("published/{employerId}")
    public Map<String, Object> listEmployerPublished(@PathVariable Long employerId) {
        Map<String, Object> response = new HashMap<>();
        List<JobOffer> list = jobOfferRepository.findByEmployerCompanyIdAndStatusOrderByUpdateDateDesc(employerId,JobOfferStatus.Published);
        List<JobApplication> applications = jobApplicationRepository.getJobApplicationsByJobOfferEmployerCompanyIdOrderByUpdateDate(employerId);
        response.put("published", list);
        response.put("applications", applications);
        return response;
    }

    @GetMapping("expired/{employerId}")
    public Map<String, Object> listEmployerExpired(@PathVariable Long employerId) {
        Map<String, Object> response = new HashMap<>();
        List<JobOffer> list = jobOfferRepository.findByEmployerCompanyIdAndStatusOrderByUpdateDateDesc(employerId,JobOfferStatus.Expired);
        List<JobApplication> applications = jobApplicationRepository.getJobApplicationsByJobOfferEmployerCompanyIdOrderByUpdateDate(employerId);
        response.put("expired", list);
        response.put("applications", applications);
        return response;
    }



    @GetMapping("applications/{offerId}")
    public Map<String, Object> listApplications(@PathVariable Long offerId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByJobOfferOfferId(offerId);
        response.put("applications", list);
        return response;
    }

    @GetMapping("applications/reference/{reference}")
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

    @GetMapping("skills/{status}")
    public Map<String, Object> listSavedSkills(@PathVariable String status) {
        Map<String, Object> response = new HashMap<>();
        List<JobOfferSkill> skills = jobOfferSkillService.listJobOfferSkillsByStatus(status);
        response.put("skills", skills);
        return response;
    }

    @PostMapping("interest/add/{offerId}/{studentId}")
    public JobOfferInterest interestJobOffer(@RequestBody JobOfferInterest interest, @PathVariable Long offerId, @PathVariable Long studentId) {
        JobOffer jobOffer = jobOfferRepository.findById(offerId).orElse(null);
        CandidateEntity student = candidateRepository.findById(studentId).orElse(null);

        if (jobOffer != null && student != null) {
            interest.setJobOffer(jobOffer);
            interest.setStudent(student);
            jobOffer.getJobInterests().add(interest);
            student.getJobInterests().add(interest);
            JobOfferInterest savedInterest = jobOfferInterestRepository.save(interest);
            return savedInterest;
        } else {
            return null;
        }
    }

    @PostMapping("extend-deadline/{offerId}")
    public JobOffer extendDeadline(@RequestBody JobOffer jobOffer, @PathVariable Long offerId) {
        JobOffer fetchedJobOffer = jobOfferRepository.findById(offerId).orElse(null);

        if (fetchedJobOffer != null) {
            jobOffer.setUpdateDate(LocalDateTime.now());
            jobOffer.setStatus(JobOfferStatus.Published);

            JobOffer updatedJobOffer = jobOfferRepository.save(jobOffer);
            return updatedJobOffer;
        } else {
            return null;
        }
    }

    @DeleteMapping("interest/remove/{interestId}")
    public ResponseEntity<Void> removeInterestJobOffer(@PathVariable Long interestId) {
        JobOfferInterest existingInterest = jobOfferInterestRepository.findById(interestId).orElse(null);

        if (existingInterest != null) {
            jobOfferInterestRepository.delete(existingInterest);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("saving/add/{offerId}/{studentId}")
    public JobOfferSaving interestJobOffer(@RequestBody JobOfferSaving saving, @PathVariable Long offerId, @PathVariable Long studentId) {
        JobOffer jobOffer = jobOfferRepository.findById(offerId).orElse(null);
        CandidateEntity student = candidateRepository.findById(studentId).orElse(null);

        if (jobOffer != null && student != null) {
            saving.setJobOffer(jobOffer);
            saving.setStudent(student);
            jobOffer.getJobSavings().add(saving);
            student.getJobSavings().add(saving);
            JobOfferSaving savedSaving = jobOfferSavingRepository.save(saving);
            return savedSaving;
        } else {
            return null;
        }
    }

    @DeleteMapping("saving/remove/{savingId}")
    public ResponseEntity<Void> removeSavingJobOffer(@PathVariable Long savingId) {
        JobOfferSaving existingSaving = jobOfferSavingRepository.findById(savingId).orElse(null);

        if (existingSaving != null) {
            jobOfferSavingRepository.delete(existingSaving);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("consultation/add/{offerId}/{studentId}")
    public JobOfferConsultation interestJobOffer(@RequestBody JobOfferConsultation consultation, @PathVariable Long offerId, @PathVariable Long studentId) {

            JobOffer jobOffer = jobOfferRepository.findById(offerId).orElse(null);
            CandidateEntity student = candidateRepository.findById(studentId).orElse(null);

            if (jobOffer != null && student != null) {
                consultation.setJobOffer(jobOffer);
                consultation.setStudent(student);
                consultation.setConsultationsNumber(consultation.getConsultationsNumber());
                if (consultation.getConsultationId()!=null) {
                    JobOfferConsultation savedConsultation = jobOfferConsultationRepository.save(consultation);
                    return savedConsultation;
                }else{
                jobOffer.getJobConsultations().add(consultation);
                student.getJobConsultations().add(consultation);
                JobOfferConsultation savedConsultation = jobOfferConsultationRepository.save(consultation);
                return savedConsultation;}
            } else {
                return null;

        }
    }

}
