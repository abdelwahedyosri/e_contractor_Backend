package group_6.e_contractor_backend.job_offer_module.entities;

import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferRequirementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobOfferSkill {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;
    private String label;

    private Long createdBy;
    private LocalDate creationDate;
    private Boolean isDeleted;

}
