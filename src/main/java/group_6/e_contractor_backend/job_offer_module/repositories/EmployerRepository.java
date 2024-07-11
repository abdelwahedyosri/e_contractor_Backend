package group_6.e_contractor_backend.job_offer_module.repositories;

import group_6.e_contractor_backend.job_offer_module.entities.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
}
