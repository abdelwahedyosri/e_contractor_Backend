package group_6.e_contractor_backend.job_offer_module.services;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import group_6.e_contractor_backend.job_offer_module.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class JobApplicationServices implements JobApplicationService{

    private final JobApplicationRepository jobOfferRepository;
    private final JobApplicationAppointmentRepository jobApplicationAppointmentRepository;
    private final JobApplicationFileRepository jobApplicationFileRepository;
    @Override
    public JobApplication createJobApplication(JobApplication jobApplication) {
        return null;
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
    public JobApplication deleteJobApplication(Long offerId) {
        return null;
    }

    @Override
    public JobApplicationAppointment createJobApplicationAppointment(JobApplicationAppointment jobApplicationAppointment, Long offerId) {
        return null;
    }

    @Override
    public JobApplicationFile createJobApplicationFile(JobApplicationFile jobApplicationFile, Long offerId) {
        return null;
    }
}
