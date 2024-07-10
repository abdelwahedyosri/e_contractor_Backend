package group_6.e_contractor_backend.job_offer_module.controllers;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationStatus;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationApointmentRepository;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationAppointmentRepository;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationFileRepository;
import group_6.e_contractor_backend.job_offer_module.repositories.JobApplicationRepository;
import group_6.e_contractor_backend.job_offer_module.services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
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


    @GetMapping("appointment/{appointmentId}")
    public ResponseEntity<Map<String, Object>> getJobApplicationByReference(@PathVariable Long appointmentId) {
        Optional<JobApplicationApointment> appointment = jobApplicationApointmentRepository.findById(appointmentId);

        Map<String, Object> response = new HashMap<>();
        response.put("appointment",appointment);
        return ResponseEntity.ok(response);
    }

    @GetMapping("list")
    public Map<String, Object> listApplications() {
        Map<String, Object> response = new HashMap<>();
        List<JobApplication> list = jobApplicationRepository.findALLByOrderByUpdateDate();
        response.put("applications", list);
        List<JobApplicationApointment> appointments = jobApplicationApointmentRepository.findAll();
        response.put("appointments", appointments);
        return response;
    }

    @GetMapping("appointments")
    public Map<String, Object> listAppointments() {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationApointment> list = jobApplicationApointmentRepository.findAll();
        response.put("appointments", list);
        return response;
    }

    @GetMapping("files")
    public Map<String, Object> listFiles() {
        Map<String, Object> response = new HashMap<>();
        List<JobApplicationFile> list = jobApplicationFileRepository.findAll();
        response.put("files", list);
        return response;
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

    private String determineContentType(Resource resource) {
        try {
            return Files.probeContentType(resource.getFile().toPath());
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }


}
