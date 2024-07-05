package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICompanyRepository extends JpaRepository<CompanyEntity, Long> {
}
