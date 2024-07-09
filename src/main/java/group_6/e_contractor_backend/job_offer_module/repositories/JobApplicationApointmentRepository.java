package group_6.e_contractor_backend.job_offer_module.repositories;

import group_6.e_contractor_backend.job_offer_module.entities.JobApplicationApointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationApointmentRepository extends JpaRepository<JobApplicationApointment, Long> {
    List<JobApplicationApointment> getJobJobApplicationApointmentsByJobApplicationStudentStudentId(Long studentId);
    List<JobApplicationApointment> getJobJobApplicationApointmentsByJobApplicationJobOfferEmployerEmployerId(Long employerId);

    List<JobApplicationApointment> getJobJobApplicationApointmentsByJobApplicationReferenceOrderByStartDateDesc(String reference);
    List<JobApplicationApointment> getJobJobApplicationApointmentsByJobApplicationJobOfferReference(String reference);


}