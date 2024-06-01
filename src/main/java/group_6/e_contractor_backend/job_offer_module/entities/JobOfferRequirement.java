package group_6.e_contractor_backend.job_offer_module.entities;

import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferRequirementType;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobWorkplaceType;
import jakarta.persistence.*;
@Entity
public class JobOfferRequirement {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requirementId;

    private String requirement_value;
    @Enumerated(EnumType.STRING)
    private JobOfferRequirementType requirementType;

    @ManyToOne
    @JoinColumn(name = "offerId")
    private JobOffer jobOffer;
}
