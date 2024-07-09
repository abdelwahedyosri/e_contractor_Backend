package group_6.e_contractor_backend.job_offer_module.controllers;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationStatus;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationApointmentRepository;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationAppointmentRepository;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationFileRepository;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationRepository;
import group_6.e_contractor_backend.job_offer_module.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("private/job-application")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class JobApplicationPrivateApiController {
    private final JobApplicationService jobApplicationService;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationFileRepository jobApplicationFileRepository;
    private final JobApplicationApointmentRepository jobApplicationApointmentRepository;

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
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByStudentStudentId(studentId);
        response.put("applications", list);
        return response;
    }

    @GetMapping("all/employer/{employerId}")
    public Map<String, Object> listEmployerAllApplications(@PathVariable Long employerId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByJobOfferEmployerEmployerIdOrderByUpdateDate(employerId);
        response.put("applications", list);
        return response;
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
        List<JobApplicationStatus> statuses = Arrays.asList(JobApplicationStatus.Approved, JobApplicationStatus.Declined, JobApplicationStatus.Expired, JobApplicationStatus.Rejected);
        List<JobApplication> list = jobApplicationRepository.getJobApplicationsByStudentStudentIdAndApplicationStatusIn(studentId,statuses);
        response.put("applications", list);
        return response;
    }

    @GetMapping("appointments/student/{studentId}")
    public Map<String, Object> listStudentApplicationsAppointments(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationApointment> list = jobApplicationApointmentRepository.getJobJobApplicationApointmentsByJobApplicationStudentStudentId(studentId);
        response.put("appointments", list);
        return response;
    }

    @GetMapping("appointments/employer/{employerId}")
    public Map<String, Object> listEmployerApplicationsAppointments(@PathVariable Long employerId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationApointment> list = jobApplicationApointmentRepository.getJobJobApplicationApointmentsByJobApplicationJobOfferEmployerEmployerId(employerId);
        response.put("appointments", list);
        return response;
    }

    @GetMapping("files/student/{studentId}")
    public Map<String, Object> listStudentApplicationsFiles(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationFile> list = jobApplicationFileRepository.getJobApplicationFilesByJobApplicationStudentStudentId(studentId);
        response.put("files", list);
        return response;
    }

}
