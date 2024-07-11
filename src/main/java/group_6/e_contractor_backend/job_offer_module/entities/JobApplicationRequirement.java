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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationRequirement {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationRequirementId;

    private String label;
    private String value;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "applicationId")
    private JobApplication jobApplication;

    @ManyToOne
    @JoinColumn(name = "requirementId")
    private JobOfferRequirement jobOfferRequirement;
}
