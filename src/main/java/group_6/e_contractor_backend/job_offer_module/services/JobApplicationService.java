package group_6.e_contractor_backend.job_offer_module.services;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobApplicationService {
    JobApplication createJobApplication(JobApplication jobApplication,Long offerId,Long studentId);
    List<JobApplication> listJobApplicationsByStatus(JobApplicationStatus status);
    JobApplication updateJobApplication(JobApplication jobApplication);
    JobApplication deleteJobApplication(Long offerId);
    JobApplicationAppointment createJobApplicationAppointment(JobApplicationAppointment jobApplicationAppointment, Long offerId);
    List<JobApplicationAppointment> listJobApplicationAppointment();
    List<JobApplicationAppointment> listJobApplicationAppointmentByStatus();
    List<JobApplicationAppointment> listJobApplicationAppointmentByType();
    JobFile uploadJobFile(MultipartFile file);
    Resource downloadJobFile(String fileName);

    JobApplicationFile createJobApplicationFile(JobApplicationFile jobApplicationFile, Long offerId);
    List<JobApplicationFile> listJobApplicationFile();
    List<JobApplicationFile> listAllJobApplicationFile();
}
