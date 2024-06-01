package group_6.e_contractor_backend.job_offer_module.services;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;

import java.util.List;

public interface JobApplicationService {
    JobApplication createJobApplication(JobApplication jobApplication);
    List<JobApplication> listJobApplicationsByStatus(JobApplicationStatus status);
    JobApplication updateJobApplication(JobApplication jobApplication);
    JobApplication deleteJobApplication(Long offerId);
    JobApplicationAppointment createJobApplicationAppointment(JobApplicationAppointment jobApplicationAppointment, Long offerId);
    List<JobApplicationAppointment> listJobApplicationAppointment();
    List<JobApplicationAppointment> listJobApplicationAppointmentByStatus();
    List<JobApplicationAppointment> listJobApplicationAppointmentByType();
    JobApplicationFile createJobApplicationFile(JobApplicationFile jobApplicationFile, Long offerId);
    List<JobApplicationFile> listJobApplicationFile();
    List<JobApplicationFile> listAllJobApplicationFile();
}
