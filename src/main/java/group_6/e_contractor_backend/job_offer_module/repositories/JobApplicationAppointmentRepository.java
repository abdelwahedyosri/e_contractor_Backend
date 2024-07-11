package group_6.e_contractor_backend.job_offer_module.repositories;

import group_6.e_contractor_backend.job_offer_module.entities.*;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationAppointmentRepository extends JpaRepository<JobApplicationAppointment, Long>{
    List<JobApplicationAppointment> getJobApplicationAppointmentsByJobApplicationStudentCandidateId(Long studentId);

}
