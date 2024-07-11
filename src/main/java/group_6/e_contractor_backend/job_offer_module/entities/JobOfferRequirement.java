package group_6.e_contractor_backend.job_offer_module.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferRequirementType;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobWorkplaceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobOfferRequirement {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requirementId;

    private String label;
    private String value;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "offerId")
    private JobOffer jobOffer;

    @JsonIgnore
    @OneToMany(mappedBy = "jobOfferRequirement")
    private Set<JobApplicationRequirement> applicationsRequirements;
}
