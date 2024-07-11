package group_6.e_contractor_backend.job_offer_module.repositories;
import group_6.e_contractor_backend.job_offer_module.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobOfferRequirementRepository extends JpaRepository<JobOfferRequirement, Long>{
    Optional<JobOfferRequirement> getJobOfferRequirementByLabelAndJobOfferOfferId(String label,Long offerId);
    List<JobOfferRequirement> getJobOfferRequirementsByJobOfferOfferId(Long offerId);

}
