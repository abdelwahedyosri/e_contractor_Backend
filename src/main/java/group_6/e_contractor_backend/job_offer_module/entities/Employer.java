package group_6.e_contractor_backend.job_offer_module.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Employer {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employerId;

    private String companyName;

    @JsonIgnore
    @OneToMany(mappedBy = "employer")
    private Set<JobOffer> jobOffers;

    @JsonIgnore
    @OneToMany(mappedBy = "employer")
    private Set<JobOfferSkill> jobSkills;
}
