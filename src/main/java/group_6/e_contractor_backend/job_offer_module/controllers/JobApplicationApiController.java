package group_6.e_contractor_backend.job_offer_module.controllers;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationAppointmentStatus;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationAppointmentType;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationStatus;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferStatus;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationApointmentRepository;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationAppointmentRepository;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationFileRepository;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationRepository;
import group_6.e_contractor_backend.job_offer_module.services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("job-application")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequiredArgsConstructor
public class JobApplicationApiController {
    private final JobApplicationService jobApplicationService;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationAppointmentRepository jobApplicationAppointmentRepository;
    private final JobApplicationFileRepository jobApplicationFileRepository;
    private final JobApplicationApointmentRepository jobApplicationApointmentRepository;

    @PostMapping("appointment/create/{applicationId}")
    public JobApplicationApointment createApplicationAppointment(@RequestBody JobApplicationApointment jobApplicationApointment,@PathVariable Long applicationId){
        JobApplication application = jobApplicationRepository.getById(applicationId);

        jobApplicationApointment.setCreatedBy(1L);
        jobApplicationApointment.setCreationDate(LocalDateTime.now());
        jobApplicationApointment.setUpdateDate(LocalDateTime.now());
        jobApplicationApointment.setUpdatedBy(1L);
        jobApplicationApointment.setAppointmentStatus(JobApplicationAppointmentStatus.Booked);
        jobApplicationApointment.setJobApplication(application);
        JobApplicationApointment saved = jobApplicationApointmentRepository.save(jobApplicationApointment);
        application.setUpdateDate(LocalDateTime.now());
        jobApplicationRepository.save(application);
        return saved;
    }
    @PostMapping("create/{offerId}/{studentId}")
    public JobApplication createJobApplication(@RequestBody JobApplication jobApplication,@PathVariable Long offerId, @PathVariable Long studentId){
        return  jobApplicationService.createJobApplication(jobApplication,offerId,studentId);
    }

    @PostMapping("respond/{applicationId}/{status}")
    public JobApplication respondJobApplication(@PathVariable Long applicationId, @PathVariable String status){
        JobApplication application = jobApplicationRepository.getById(applicationId);
        application.setApplicationStatus(JobApplicationStatus.valueOf(status));
        application.setUpdateDate(LocalDateTime.now());
        System.out.println("status");
        System.out.println(status);

        if (JobApplicationStatus.valueOf(status).equals(JobApplicationStatus.Declined)) {
            application.setDeclineDate(LocalDateTime.now());
            application.setDeclinedBy(1L);
        }
        if (JobApplicationStatus.valueOf(status).equals(JobApplicationStatus.Rejected)) {
            application.setDeclineDate(LocalDateTime.now());
            application.setDeclinedBy(1L);
        }
        if (JobApplicationStatus.valueOf(status).equals(JobApplicationStatus.Approved)) {
            application.setApprovalDate(LocalDateTime.now());
        }
        if (JobApplicationStatus.valueOf(status).equals(JobApplicationStatus.Accepted)) {
            application.setAcceptedDate(LocalDateTime.now());
        }
        jobApplicationRepository.save(application);
        return jobApplicationRepository.save(application);
    }

    @PostMapping("appointment/update-status/{appointmentId}/{status}")
    public ResponseEntity<JobApplicationApointment> updateJobApplicationApointmentStatus(@PathVariable Long appointmentId, @PathVariable String status) {
        Optional<JobApplicationApointment> jobApplicationApointmentOptional = jobApplicationApointmentRepository.findById(appointmentId);

        if (!jobApplicationApointmentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        JobApplicationApointment jobApplicationApointment = jobApplicationApointmentOptional.get();

        if (JobApplicationAppointmentStatus.valueOf(status).equals(JobApplicationAppointmentStatus.Approved)) {
            jobApplicationApointment.setApprovalDate(LocalDateTime.now());
        }
        if (JobApplicationAppointmentStatus.valueOf(status).equals(JobApplicationAppointmentStatus.Declined)) {
            jobApplicationApointment.setDeclineDate(LocalDateTime.now());
        }

        jobApplicationApointment.setAppointmentStatus(JobApplicationAppointmentStatus.valueOf(status));
        jobApplicationApointment.setUpdateDate(LocalDateTime.now());
        jobApplicationApointment.setUpdatedBy(1L);

        JobApplication application = jobApplicationApointment.getJobApplication();
        application.setUpdateDate(LocalDateTime.now());
        jobApplicationRepository.save(application);

        JobApplicationApointment updatedAppointment = jobApplicationApointmentRepository.save(jobApplicationApointment);
        return ResponseEntity.ok(updatedAppointment);
    }

    @PutMapping("update-appointment/{appointmentId}/{status}")
    public ResponseEntity<JobApplicationApointment> updateJobApplicationApointment(@RequestBody JobApplicationApointment jobApplicationApointment ,@PathVariable Long appointmentId,@PathVariable String status) {

        if (JobApplicationAppointmentStatus.valueOf(status).equals(JobApplicationAppointmentStatus.Approved)) {
            jobApplicationApointment.setApprovalDate(LocalDateTime.now());
        }
        if (JobApplicationAppointmentStatus.valueOf(status).equals(JobApplicationAppointmentStatus.Declined)) {
            jobApplicationApointment.setDeclineDate(LocalDateTime.now());
        }
        if(status.isEmpty()){
            jobApplicationApointment.setAppointmentStatus(JobApplicationAppointmentStatus.valueOf(status));
        }

        jobApplicationApointment.setUpdateDate(LocalDateTime.now());
        jobApplicationApointment.setUpdatedBy(1L);
        JobApplication application = jobApplicationApointment.getJobApplication();
        jobApplicationApointment.setUpdateDate(LocalDateTime.now());
        jobApplicationRepository.save(application);

        JobApplicationApointment updatedAppointment = jobApplicationApointmentRepository.save(jobApplicationApointment);
        return ResponseEntity.ok(updatedAppointment);
    }

    @PostMapping("meet-update/{status}")
    public Map<String, Object> commentOnAppointment(@RequestBody JobApplicationApointment jobApplicationApointment, @PathVariable String status){

        System.out.println("status");
        System.out.println(status);

        // Check if the status is a valid enum constant
        JobApplicationAppointmentStatus appointmentStatus = null;
        try {
            appointmentStatus = JobApplicationAppointmentStatus.valueOf(status);
            if (appointmentStatus.equals(JobApplicationAppointmentStatus.Approved)) {
                jobApplicationApointment.setApprovalDate(LocalDateTime.now());
            }
            if (appointmentStatus.equals(JobApplicationAppointmentStatus.Declined)) {
                jobApplicationApointment.setDeclineDate(LocalDateTime.now());
            }

            System.out.println("appointmentStatus");
            System.out.println(appointmentStatus);

            jobApplicationApointment.setAppointmentStatus(appointmentStatus);

            jobApplicationApointment.setUpdateDate(LocalDateTime.now());
            jobApplicationApointment.setUpdatedBy(1L);
            JobApplication application = jobApplicationApointment.getJobApplication();
            jobApplicationApointment.setUpdateDate(LocalDateTime.now());
            jobApplicationRepository.save(application);
            JobApplicationApointment updatedAppointment = jobApplicationApointmentRepository.save(jobApplicationApointment);
            Map<String, Object> response = new HashMap<>();
            response.put("appointment", updatedAppointment);
            return response;
        } catch (IllegalArgumentException e) {
            jobApplicationApointment.setUpdateDate(LocalDateTime.now());
            jobApplicationApointment.setUpdatedBy(1L);
            JobApplication application = jobApplicationApointment.getJobApplication();
            jobApplicationApointment.setUpdateDate(LocalDateTime.now());
            jobApplicationRepository.save(application);
            JobApplicationApointment updatedAppointment = jobApplicationApointmentRepository.save(jobApplicationApointment);
            Map<String, Object> response = new HashMap<>();
            response.put("appointment", updatedAppointment);
            return response;

        }


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


    @GetMapping("display-file/{fileName:.+}")
    public ResponseEntity<Resource> displayApplicationFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = jobApplicationService.downloadJobFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, determineContentType(resource))
                .body(resource);
    }

    @GetMapping("url-file/{fileName:.+}")
    public ResponseEntity<String> displayApplicationFile(@PathVariable String fileName) {
        String fileUrl = "/uploads/" + fileName;
        return ResponseEntity.ok(fileUrl);
    }

    private String determineContentType(Resource resource) {
        try {
            return Files.probeContentType(resource.getFile().toPath());
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }
    @GetMapping("reference/{reference}")
    public ResponseEntity<Map<String, Object>> getJobApplicationByReference(@PathVariable String reference) {
        JobApplication jobApplication = jobApplicationService.getJobApplicationByReference(reference);
        List<JobApplicationApointment> list = jobApplicationApointmentRepository.getJobJobApplicationApointmentsByJobApplicationReferenceOrderByStartDateDesc(reference);

        Map<String, Object> response = new HashMap<>();
        response.put("jobApplication", jobApplication);
        response.put("jobApplicationAppointments", list);

        return ResponseEntity.ok(response);
    }

    @GetMapping("all/student/{studentId}")
    public Map<String, Object> listStudentAllApplications(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByStudentCandidateId(studentId);
        response.put("applications", list);
        return response;
    }

    @GetMapping("all/employer/{employerId}")
    public Map<String, Object> listEmployerAllApplications(@PathVariable Long employerId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByJobOfferEmployerCompanyIdOrderByUpdateDate(employerId);
        response.put("applications", list);
        return response;
    }

    @GetMapping("inprogress/student/{studentId}")
    public Map<String, Object> listStudentInProgressApplications(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationStatus> statuses = Arrays.asList(JobApplicationStatus.Sent, JobApplicationStatus.Accepted);
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByStudentCandidateIdAndApplicationStatusIn(studentId,statuses);
        response.put("applications", list);
        return response;
    }

    @GetMapping("applied/student/{studentId}")
    public Map<String, Object> listStudentAppliedApplications(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationStatus> statuses = Arrays.asList(JobApplicationStatus.Approved, JobApplicationStatus.Declined, JobApplicationStatus.Expired, JobApplicationStatus.Rejected);
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByStudentCandidateIdAndApplicationStatusIn(studentId,statuses);
        response.put("applications", list);
        return response;
    }

    @GetMapping("appointments/student/{studentId}")
    public Map<String, Object> listStudentApplicationsAppointments(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationApointment> list = jobApplicationApointmentRepository.getJobJobApplicationApointmentsByJobApplicationStudentCandidateId(studentId);
        response.put("appointments", list);
        return response;
    }

    @GetMapping("appointments/employer/{employerId}")
    public Map<String, Object> listEmployerApplicationsAppointments(@PathVariable Long employerId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationApointment> list = jobApplicationApointmentRepository.getJobJobApplicationApointmentsByJobApplicationJobOfferEmployerCompanyId(employerId);
        response.put("appointments", list);
        return response;
    }

    @GetMapping("files/student/{studentId}")
    public Map<String, Object> listStudentApplicationsFiles(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationFile> list = jobApplicationFileRepository.getJobApplicationFilesByJobApplicationStudentCandidateId(studentId);
        response.put("files", list);
        return response;
    }

}
