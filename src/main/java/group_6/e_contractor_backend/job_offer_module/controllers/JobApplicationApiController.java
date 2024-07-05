package group_6.e_contractor_backend.job_offer_module.controllers;

import group_6.e_contractor_backend.job_offer_module.entities.JobApplication;
import group_6.e_contractor_backend.job_offer_module.entities.JobApplicationAppointment;
import group_6.e_contractor_backend.job_offer_module.entities.JobFile;
import group_6.e_contractor_backend.job_offer_module.entities.JobOffer;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationStatus;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationAppointmentRepository;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationRepository;
import group_6.e_contractor_backend.job_offer_module.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("job-application")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class JobApplicationApiController {
    private final JobApplicationService jobApplicationService;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationAppointmentRepository jobApplicationAppointmentRepository;

    @PostMapping("create/{offerId}/{studentId}")
    public JobApplication createJobApplication(@RequestBody JobApplication jobApplication,@PathVariable Long offerId, @PathVariable Long studentId){
        return  jobApplicationService.createJobApplication(jobApplication,offerId,studentId);
    }

    @PostMapping("upload-file")
    public JobFile uploadApplicationFile(@RequestParam("file") MultipartFile file) {
        return jobApplicationService.uploadJobFile(file);
    }

    @GetMapping("download-file/{fileName:.+}")
    public ResponseEntity<Resource> downloadApplicationFile(@PathVariable String fileName) {
        Resource resource = jobApplicationService.downloadJobFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("reference/{reference}")
    public JobApplication getJobOfferByReference(@PathVariable String reference) {
        return jobApplicationService.getJobApplicationByReference(reference);
    }

    @GetMapping("inprogress/student/{studentId}")
    public Map<String, Object> listStudentInProgressApplications(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationStatus> statuses = Arrays.asList(JobApplicationStatus.Sent, JobApplicationStatus.Accepted);
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByStudentStudentIdAndApplicationStatusIn(studentId,statuses);
        response.put("applications", list);
        return response;
    }

    @GetMapping("applied/student/{studentId}")
    public Map<String, Object> listStudentAppliedApplications(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationStatus> statuses = Arrays.asList(JobApplicationStatus.Approved, JobApplicationStatus.Declined, JobApplicationStatus.Expired);
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByStudentStudentIdAndApplicationStatusIn(studentId,statuses);
        response.put("applications", list);
        return response;
    }

    @GetMapping("appointments/student/{studentId}")
    public Map<String, Object> listStudentApplicationsAppointments(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationAppointment> list = jobApplicationAppointmentRepository.getJobApplicationAppointmentsByJobApplicationStudentStudentId(studentId);
        response.put("appointments", list);
        return response;
    }

    @GetMapping("files/{studentId}")
    public Map<String, Object> listStudentApplicationsFiles(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByStudentStudentId(studentId);
        response.put("files", list);
        return response;
    }

}
