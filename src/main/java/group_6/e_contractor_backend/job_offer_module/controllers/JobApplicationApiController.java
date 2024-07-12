package group_6.e_contractor_backend.job_offer_module.controllers;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationAppointmentStatus;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationAppointmentType;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationStatus;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferStatus;
import group_6.e_contractor_backend.job_offer_module.repositories.*;
import group_6.e_contractor_backend.job_offer_module.services.*;
import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.repository.ICandidateRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final JavaMailSender mailSender;
    private final JobOfferRepository jobOfferRepository;
    private final ICandidateRepository candidateRepository;


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

        sendNewAppointmentBookedEmail(jobApplicationApointment);
        return saved;
    }
    private void sendNewAppointmentBookedEmail(JobApplicationApointment appointment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(appointment.getJobApplication().getEmail());
            String subject = appointment.getJobApplication().getJobOffer().getJobTitle() + ": New Appointment Booked";
            helper.setSubject(subject);

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<p>Dear " + appointment.getJobApplication().getStudentName() + ",</p>")
                    .append(appointment.getJobApplication().getJobOffer().getEmployer().getCompanyName() + "<p> have booked new appointment with you.</p>")
                    .append("<p>Title: " + appointment.getTitle() + "</p>")
                    .append("<p>Type: " + appointment.getAppointmentType() + "</p>")
                    .append("<p>Mode: " + appointment.getAppointmentLocation() + "</p>")
                    .append("<p>Address/Meet link: " + appointment.getAppointmentAddress() + appointment.getMeetLink() + "</p>")
                    .append("<p><a href=\"http://localhost:4200/discover-job-offers/application/"
                            + appointment.getJobApplication().getReference() + "\" style=\"padding: 10px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">View Application</a></p>");

            helper.setText(emailContent.toString(), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
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

    @GetMapping("job-offers-kpis/employer/{employerId}")
    public Map<String, Object> JobOffersKpisForEmployer(@PathVariable Long employerId) {
        Map<String, Object> response = new HashMap<>();

        // Fetch job offers by employerId
        List<JobOffer> jobOffers = jobOfferRepository.getJobOffersByEmployerCompanyId(employerId);

        // Calculate total job offers
        int totalJobOffers = jobOffers.size();

        if (totalJobOffers == 0) {
            response.put("totalJobOffers", 0);
            response.put("totalOpenPositions", 0);
            response.put("averageRenumeration", 0.0);
            response.put("totalFiles", 0);
            response.put("totalConsultations", 0);
            response.put("totalConsultationsCount", 0);
            response.put("totalAppointments", 0);
            response.put("totalInterests", 0);
            response.put("totalSavings", 0);
            response.put("totalApplications", 0);
            response.put("averageApplicationsPerOffer", 0.0);
            response.put("averageConsultationsPerOffer", 0.0);
            response.put("averageInterestsPerOffer", 0.0);
            response.put("averageSavingsPerOffer", 0.0);
            response.put("jobOffersByStatus", Collections.emptyList());
            response.put("topJobOffersByApplications", Collections.emptyList());
            response.put("topJobOffersByInteractions", Collections.emptyList());

            return response;
        }

        // Calculate total open positions
        long totalOpenPositions = jobOffers.stream().mapToLong(JobOffer::getOpenPositions).sum();

        // Calculate average renumeration
        double averageRenumeration = jobOffers.stream()
                .filter(offer -> offer.getRenumeration() != null)
                .mapToLong(JobOffer::getRenumeration)
                .average()
                .orElse(0.0);

        // Calculate total files
        long totalFiles = jobOffers.stream()
                .flatMap(offer -> offer.getJobApplications().stream())
                .flatMap(application -> application.getJobApplicationFiles().stream())
                .count();

        // Calculate average applications per offer
        double averageApplicationsPerOffer = jobOffers.stream()
                .mapToLong(offer -> offer.getJobApplications().size())
                .average()
                .orElse(0.0);

        // Calculate average consultations per offer
        double averageConsultationsPerOffer = jobOffers.stream()
                .mapToLong(offer -> offer.getJobConsultations().size())
                .average()
                .orElse(0.0);

        // Calculate average interests per offer
        double averageInterestsPerOffer = jobOffers.stream()
                .mapToLong(offer -> offer.getJobInterests().size())
                .average()
                .orElse(0.0);

        // Calculate average savings per offer
        double averageSavingsPerOffer = jobOffers.stream()
                .mapToLong(offer -> offer.getJobSavings().size())
                .average()
                .orElse(0.0);

        // Calculate total job consultations
        long totalConsultations = jobOffers.stream()
                .flatMap(offer -> offer.getJobConsultations().stream())
                .count();

        // Calculate total consultations count
        long totalConsultationsCount = jobOffers.stream()
                .flatMap(offer -> offer.getJobConsultations().stream())
                .mapToLong(JobOfferConsultation::getConsultationsNumber)
                .sum();

        long totalAppointments = jobOffers.stream()
                .peek(offer -> System.out.println("Job Offer: " + offer)) // Log job offer
                .flatMap(offer -> offer.getJobApplications().stream())
                .peek(application -> System.out.println("Job Application: " + application)) // Log job application
                .flatMap(application -> application.getJobApplicationAppointments().stream())
                .peek(appointment -> System.out.println("Appointment: " + appointment)) // Log appointment
                .count();

        System.out.println("Total Appointments: " + totalAppointments);




        // Calculate total job interests
        long totalInterests = jobOffers.stream()
                .flatMap(offer -> offer.getJobInterests().stream())
                .count();

        // Calculate total job savings
        long totalSavings = jobOffers.stream()
                .flatMap(offer -> offer.getJobSavings().stream())
                .count();

        // Calculate total applications
        long totalApplications = jobOffers.stream()
                .flatMap(offer -> offer.getJobApplications().stream())
                .count();

// Calculate job offers by statuses
        Map<JobOfferStatus, Long> jobOffersByStatus = jobOffers.stream()
                .collect(Collectors.groupingBy(JobOffer::getStatus, Collectors.counting()));

        List<Map<String, Object>> jobOffersStatusList = jobOffersByStatus.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> statusMap = new HashMap<>();
                    statusMap.put("status", entry.getKey().toString());
                    statusMap.put("count", entry.getValue());
                    return statusMap;
                })
                .collect(Collectors.toList());

        // Fetch top 3 job offers with the most applications
        List<JobOffer> topJobOffersByApplications = jobOffers.stream()
                .sorted((o1, o2) -> Integer.compare(o2.getJobApplications().size(), o1.getJobApplications().size()))
                .limit(3)
                .collect(Collectors.toList());

        List<Map<String, Object>> topJobOffersList = topJobOffersByApplications.stream()
                .map(offer -> {
                    Map<String, Object> offerMap = new HashMap<>();
                    offerMap.put("jobOfferId", offer.getOfferId());
                    offerMap.put("title", offer.getJobTitle());
                    offerMap.put("applicationsCount", offer.getJobApplications().size());
                    return offerMap;
                })
                .collect(Collectors.toList());

        Set<Long> candidateIds = getCandidateIdsWithInteractions(jobOffers);

        // Fetch candidates based on the identified candidate IDs
        List<CandidateEntity> candidates = candidateRepository.findAllById(candidateIds);



        // Fetch top 3 job offers with the most total interactions
        List<JobOffer> topJobOffersByInteractions = jobOffers.stream()
                .sorted((o1, o2) -> {
                    long o1Interactions = (long) o1.getJobApplications().size()
                            + o1.getJobConsultations().stream().mapToLong(JobOfferConsultation::getConsultationsNumber).sum()
                            + o1.getJobSavings().size()
                            + o1.getJobInterests().size();

                    long o2Interactions = (long) o2.getJobApplications().size()
                            + o2.getJobConsultations().stream().mapToLong(JobOfferConsultation::getConsultationsNumber).sum()
                            + o2.getJobSavings().size()
                            + o2.getJobInterests().size();

                    return Long.compare(o2Interactions, o1Interactions);
                })
                .limit(3)
                .collect(Collectors.toList());

        List<Map<String, Object>> topJobOffersInteractionsList = topJobOffersByInteractions.stream()
                .map(offer -> {
                    Map<String, Object> offerMap = new HashMap<>();
                    offerMap.put("jobOfferId", offer.getOfferId());
                    offerMap.put("title", offer.getJobTitle());
                    offerMap.put("reference", offer.getReference());
                    offerMap.put("applicationsCount", offer.getJobApplications().size());
                    offerMap.put("consultations", offer.getJobConsultations().size());
                    offerMap.put("consultationsCount", offer.getJobConsultations().stream().mapToLong(JobOfferConsultation::getConsultationsNumber).sum());
                    offerMap.put("savingsCount", offer.getJobSavings().size());
                    offerMap.put("interestsCount", offer.getJobInterests().size());
                    offerMap.put("totalInteractions",
                            (long) offer.getJobApplications().size()
                                    + offer.getJobConsultations().stream().mapToLong(JobOfferConsultation::getConsultationsNumber).sum()
                                    + offer.getJobSavings().size()
                                    + offer.getJobInterests().size());
                    return offerMap;
                })
                .collect(Collectors.toList());


        List<CandidateEntity> students = candidateRepository.findAll();

        List<Map<String, Object>> candidateInteractions = candidates.stream()
                .map(candidate -> {
                    List<JobApplication> jobApplications = new ArrayList<>(candidate.getJobApplications()); // Convert Set to List
                    // Collect interaction details for each candidate
                    Map<String, Object> interactionDetails = Map.of(
                            "candidateId", candidate.getCandidateId(),
                            "title", candidate.getProfessionalTitle(),
                            "region", candidate.getRegion(),
                            "candidateName", candidate.getFirstName() + " " + candidate.getLastName(),
                            "totalApplications", jobApplications.stream()
                                    .filter(interaction -> jobOffers.contains(((JobApplication) interaction).getJobOffer()))
                                    .count(),
                            "totalSavings", candidate.getJobSavings().stream()
                                    .filter(interaction -> jobOffers.contains(((JobOfferSaving) interaction).getJobOffer()))
                                    .count(),
                            "totalInterests", candidate.getJobInterests().stream()
                                    .filter(interaction -> jobOffers.contains(((JobOfferInterest) interaction).getJobOffer()))
                                    .count(),
                            "totalConsultations", candidate.getJobConsultations().stream()
                                    .filter(interaction -> jobOffers.contains(((JobOfferConsultation) interaction).getJobOffer()))
                                    .count()
                            // Add more interactions as needed
                    );
                    return interactionDetails;
                })
                .collect(Collectors.toList());

        response.put("candidateInteractions", candidateInteractions);
        response.put("totalStudents", students.size());
        //  the response
        response.put("topJobOffersByInteractions", topJobOffersInteractionsList);
        response.put("totalJobOffers", totalJobOffers);
        response.put("totalOpenPositions", totalOpenPositions);
        response.put("averageRenumeration", averageRenumeration);
        response.put("totalFiles", totalFiles);
        response.put("averageApplicationsPerOffer", averageApplicationsPerOffer);
        response.put("averageConsultationsPerOffer", averageConsultationsPerOffer);
        response.put("averageInterestsPerOffer", averageInterestsPerOffer);
        response.put("averageSavingsPerOffer", averageSavingsPerOffer);
        response.put("totalConsultations", totalConsultations);
        response.put("totalConsultationsCount", totalConsultationsCount);
        response.put("totalInterests", totalInterests);
        response.put("totalSavings", totalSavings);
        response.put("totalApplications", totalApplications);
        response.put("totalAppointments", totalAppointments);
        response.put("jobOffersByStatus", jobOffersStatusList);
        response.put("topJobOffersByApplications", topJobOffersList);
        response.put("topJobOffersByInteractions", topJobOffersInteractionsList);


        return response;
    }

    private Set<Long> getCandidateIdsWithInteractions(List<JobOffer> employerJobOffers) {
        // Collect candidate IDs who have interacted with employer's job offers
        return employerJobOffers.stream()
                .flatMap(jobOffer -> Stream.of(
                        jobOffer.getJobApplications().stream().map(JobApplication::getStudent),
                        jobOffer.getJobSavings().stream().map(JobOfferSaving::getStudent),
                        jobOffer.getJobInterests().stream().map(JobOfferInterest::getStudent),
                        jobOffer.getJobConsultations().stream().map(JobOfferConsultation::getStudent)
                        // Add more interactions as needed
                ))
                .flatMap(Function.identity()) // Use Function.identity() to flatten the streams
                .map(CandidateEntity::getCandidateId)
                .collect(Collectors.toSet());
    }

    @GetMapping("job-offer-kpis/{reference}")
    public Map<String, Object> JobOffersKpisByReference(@PathVariable String reference) {

        Map<String, Object> response = new HashMap<>();

        // Fetch job offer by employerId and reference
        JobOffer jobOffer = jobOfferRepository.getJobOfferByReference(reference);

        if (jobOffer == null) {
            response.put("totalFiles", 0);
            response.put("totalConsultations", 0);
            response.put("totalConsultationsCount", 0);
            response.put("totalInterests", 0);
            response.put("totalAppointments", 0);
            response.put("totalSavings", 0);
            response.put("totalApplications", 0);

            return response;
        }

        // Initialize response metrics
        int totalJobOffers = 1; // Only one job offer
        long totalOpenPositions = jobOffer.getOpenPositions();

        double averageRenumeration = jobOffer.getRenumeration() != null ? jobOffer.getRenumeration() : 0.0;

        // Calculate total files
        long totalFiles = jobOffer.getJobApplications().stream()
                .flatMap(application -> application.getJobApplicationFiles().stream())
                .count();

        // Calculate total applications
        long totalApplications = jobOffer.getJobApplications().size();

        // Calculate total consultations
        long totalConsultations = jobOffer.getJobConsultations().size();

        long totalConsultationsCount = jobOffer.getJobConsultations().stream()
                .mapToLong(JobOfferConsultation::getConsultationsNumber)
                .sum();

        long totalAppointments = jobOffer.getJobApplications().stream()
                .flatMap(application -> application.getJobApplicationAppointments().stream())
                .count();


        // Calculate total job interests
        long totalInterests = jobOffer.getJobInterests().size();

        // Calculate total job savings
        long totalSavings = jobOffer.getJobSavings().size();

        // Add job offers by status
        Map<JobApplicationStatus, Long> jobApplicationsByStatus = jobOffer.getJobApplications().stream()
                .collect(Collectors.groupingBy(application -> application.getApplicationStatus(), Collectors.counting()));

        List<Map<String, Object>> jobApplicationsStatusList = jobApplicationsByStatus.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> statusMap = new HashMap<>();
                    statusMap.put("status", entry.getKey().toString());
                    statusMap.put("count", entry.getValue());
                    return statusMap;
                })
                .collect(Collectors.toList());

        // Add metrics to response
        response.put("totalFiles", totalFiles);
        response.put("totalApplications", totalApplications);
        response.put("totalAppointments", totalAppointments);
        response.put("totalConsultations", totalConsultations);
        response.put("totalConsultationsCount", totalConsultationsCount);
        response.put("totalInterests", totalInterests);
        response.put("totalSavings", totalSavings);

        // Add job offers by status and top offers
        response.put("jobApplicationsByStatus", jobApplicationsStatusList);

        return response;
    }


    @GetMapping("job-offers-kpis/student/{studentId}")
    public Map<String, Object> JobApplicationsKpisForStudent(@PathVariable Long studentId) {
        Map<String, Object> response = new HashMap<>();

        // Fetch candidate by studentId
        Optional<CandidateEntity> candidateOpt = candidateRepository.findById(studentId);
        if (!candidateOpt.isPresent()) {
            response.put("error", "Candidate not found");
            return response;
        }

        CandidateEntity candidate = candidateOpt.get();

        // Get job applications for the candidate
        Set<JobApplication> jobApplications = candidate.getJobApplications();

        // Calculate total applications
        long totalApplications = jobApplications.size();

        // Calculate total files
        long totalFiles = jobApplications.stream()
                .flatMap(application -> application.getJobApplicationFiles().stream())
                .count();

        // Calculate total appointments
        long totalAppointments = jobApplications.stream()
                .flatMap(application -> application.getJobApplicationAppointments().stream())
                .count();

        // Calculate total interests
        long totalInterests = candidate.getJobInterests().size();

        // Calculate total consultations
        long totalConsultations = candidate.getJobConsultations().size();

        // Calculate total savings
        long totalSavings = candidate.getJobSavings().size();

        // Calculate consultations count
        long totalConsultationsCount = candidate.getJobConsultations().stream()
                .mapToLong(JobOfferConsultation::getConsultationsNumber)
                .sum();

        response.put("totalApplications", totalApplications);
        response.put("totalFiles", totalFiles);
        response.put("totalAppointments", totalAppointments);
        response.put("totalInterests", totalInterests);
        response.put("totalConsultations", totalConsultations);
        response.put("totalSavings", totalSavings);
        response.put("totalConsultationsCount", totalConsultationsCount);

        return response;
    }


}
