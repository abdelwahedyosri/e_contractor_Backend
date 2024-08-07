package group_6.e_contractor_backend.job_offer_module.services;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import group_6.e_contractor_backend.job_offer_module.repositories.*;
import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.entity.CompanyEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.repository.ICandidateRepository;
import group_6.e_contractor_backend.user.repository.ICompanyRepository;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class JobApplicationServices implements JobApplicationService{

    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationAppointmentRepository jobApplicationAppointmentRepository;
    private final JobApplicationFileRepository jobApplicationFileRepository;
    private final JobFileRepository jobFileRepository ;
    private final JobOfferRepository jobOfferRepository ;
    private final ICandidateRepository candidateRepository ;
    private final JobApplicationRequirementRepository jobApplicationRequirementRepository;
    private final JavaMailSender mailSender;


    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    @PostConstruct
    public void init() {
        File uploadDirectory = new File(UPLOAD_DIR);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
    }

    @Override
    public JobApplication createJobApplication(JobApplication jobApplication,Long offerId,Long studentId) {
        LocalDateTime currentDate = LocalDateTime.now();
        JobOffer jobOffer = jobOfferRepository.findById(offerId).orElse(null);
        CandidateEntity student = candidateRepository.findById(studentId).orElse(null);
        jobApplication.setCreatedBy(1L);
        jobApplication.setCreationDate(currentDate);
        jobApplication.setUpdateDate(currentDate);
        jobApplication.setApplicationStatus(JobApplicationStatus.Sent);
        jobApplication.setJobOffer(jobOffer);
        jobApplication.setStudent(student);
        System.out.println("jobApplication");
        System.out.println(jobApplication);
        System.out.println("jobApplication.getJobApplicationFiles()");
        System.out.println(jobApplication.getJobApplicationFiles());
        JobApplication savedApplication = jobApplicationRepository.save(jobApplication);

        if (jobApplication.getJobApplicationFiles() != null && !jobApplication.getJobApplicationFiles().isEmpty()) {
            for (JobApplicationFile applicationFile : jobApplication.getJobApplicationFiles()) {
                System.out.println(applicationFile);
                JobFile jobFile = applicationFile.getJobFile();
                applicationFile.setJobFile(jobFile);
                applicationFile.setJobApplication(jobApplication);
                applicationFile.setCreatedBy(1L);
                applicationFile.setCreationDate(currentDate);
                jobApplicationFileRepository.save(applicationFile);
            }
        }

        if (jobApplication.getRequirements() != null && !jobApplication.getRequirements().isEmpty()) {
            for (JobApplicationRequirement requirement : jobApplication.getRequirements()) {
                System.out.println(requirement);
                JobOfferRequirement jobRequirement = requirement.getJobOfferRequirement();
                requirement.setJobOfferRequirement(jobRequirement);
                requirement.setJobApplication(jobApplication);
                jobApplicationRequirementRepository.save(requirement);
            }
        }

        if (Boolean.TRUE.equals(jobOffer.getReceiveApplicationsEmails())) {
            sendNewApplicationEmail(jobApplication);
        }

        return savedApplication;
    }

    private void sendNewApplicationEmail(JobApplication application) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(application.getJobOffer().getEmployer().getUser().getEmail());
            String subject = application.getJobOffer().getJobTitle() + ": New Application Received";
            helper.setSubject(subject);

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<p>Dear " + application.getJobOffer().getEmployer().getCompanyName() + ",</p>")
                    .append("<p>You have received a new application from " + application.getStudentName() + ".</p>")
                    .append("<p>Title: " + application.getStudentTitle() + "</p>")
                    .append("<p>Phone: " + application.getPhoneNumber() + "</p>")
                    .append("<p>Email: " + application.getEmail() + "</p>")
                    .append("<p><a href=\"http://localhost:4200/job-offers/"
                            + application.getJobOffer().getReference() + "/application/"
                            + application.getReference() + "\" style=\"padding: 10px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">View Application</a></p>");

            helper.setText(emailContent.toString(), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public List<JobApplication> listJobApplicationsByStatus(JobApplicationStatus status) {
        return null;
    }

    @Override
    public JobApplication updateJobApplication(JobApplication jobApplication) {
        return null;
    }

    @Override
    public JobApplication getJobApplicationByReference(String reference) {
        return jobApplicationRepository.getJobApplicationByReference(reference);
    }

    @Override
    public JobApplication deleteJobApplication(Long offerId) {
        return null;
    }

    @Override
    public JobApplicationAppointment createJobApplicationAppointment(JobApplicationAppointment jobApplicationAppointment, Long offerId) {
        return null;
    }

    @Override
    public List<JobApplicationAppointment> listJobApplicationAppointment() {
        return null;
    }

    @Override
    public List<JobApplicationAppointment> listJobApplicationAppointmentByStatus() {
        return null;
    }

    @Override
    public List<JobApplicationAppointment> listJobApplicationAppointmentByType() {
        return null;
    }

    @Override
    public JobFile uploadJobFile(MultipartFile file) {
        String fileToken = UUID.randomUUID().toString();
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
        String filePath = UPLOAD_DIR + File.separator + fileToken + "_" + originalFileName;


        try {
            // Save the file to the disk
            Files.copy(file.getInputStream(), Paths.get(filePath));

            JobFile jobFile = new JobFile();
            jobFile.setFileName(fileToken + "_" + file.getOriginalFilename());
            jobFile.setFileOriginalName(file.getOriginalFilename());
            jobFile.setFileSize(file.getSize());
            jobFile.setFileToken(fileToken);
            jobFile.setFilePathUrl(filePath);
            jobFile.setFileType(file.getContentType());
            jobFile.setFileExtension(fileExtension);
            jobFile.setIsActive(true);
            jobFile.setCreatedBy(1L);
            jobFile.setCreationDate(LocalDateTime.now());

            // Save the JobFile entity to the database
            return jobFileRepository.save(jobFile);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Resource downloadJobFile(String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("File not found " + fileName, e);
        }
    }

    @Override
    public JobApplicationFile createJobApplicationFile(JobApplicationFile jobApplicationFile, Long offerId) {
        return null;
    }

    @Override
    public List<JobApplicationFile> listJobApplicationFile() {
        return null;
    }

    @Override
    public List<JobApplicationFile> listAllJobApplicationFile() {
        return null;
    }
}
