package group_6.e_contractor_backend.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import group_6.e_contractor_backend.job_offer_module.entities.JobApplication;
import group_6.e_contractor_backend.job_offer_module.entities.JobOfferConsultation;
import group_6.e_contractor_backend.job_offer_module.entities.JobOfferInterest;
import group_6.e_contractor_backend.job_offer_module.entities.JobOfferSaving;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "candidate")
public class CandidateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long candidateId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = true)
    private String dob;

    @Column(nullable = true)
    private String location;


    @Column(nullable = true)
    private String region;

    @Column(nullable = true)
    private String professionalTitle;

    @Column(nullable = true)
    private String photo;

    @Column(nullable = true)
    private String video;

    @Column(nullable = true)
    private Double minRate;

    @Column(nullable = true)
    private String resumeCategory;

    @Column(nullable = true)
    private String resumeContent;

    @Column(nullable = true)
    private String skills;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private Set<JobApplication> jobApplications;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private Set<JobOfferInterest> jobInterests;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private Set<JobOfferConsultation> jobConsultations;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private Set<JobOfferSaving> jobSavings;

}
