package group_6.e_contractor_backend.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import group_6.e_contractor_backend.job_offer_module.entities.JobOffer;
import group_6.e_contractor_backend.job_offer_module.entities.JobOfferSkill;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "company")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = true)
    private String companyLogo;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String website;

    @JsonIgnore
    @OneToMany(mappedBy = "employer")
    private Set<JobOffer> jobOffers;

    @JsonIgnore
    @OneToMany(mappedBy = "employer")
    private Set<JobOfferSkill> jobSkills;

    private String companyName;
}

