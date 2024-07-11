package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.job_offer_module.entities.JobApplicationApointment;
import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICompanyRepository extends JpaRepository<CompanyEntity, Long> {
    CompanyEntity getCompanyEntityByUserUsername(String username);

}
