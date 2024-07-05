package group_6.e_contractor_backend.job_offer_module.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobOffer {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;
    private String reference;
    private String jobTitle;
    @Column(length = 2000)
    private String description;
    @Column(length = 2000)
    private String tasksDescription;

    @Enumerated(EnumType.STRING)
    private JobOfferType type;

    @Enumerated(EnumType.STRING)
    private JobWorkplaceType workspaceType;

    @Enumerated(EnumType.STRING)
    private JobContractType jobContract;

    @Enumerated(EnumType.STRING)
    private JobOfferStatus status;

    private String country;
    private String city;
    private String location;

    private Long renumeration;
    private String renumerationPeriod;
    private String renumerationCurrency;
    private String category;
    private Long openPositions;

    private LocalDate deadline;
    private LocalDate publishingDate;

    private String educationLevel;
    private String experience;
    @Column(length = 2000)
    private String skills;
    private String language;
    private Boolean allowSimpleApplications;


    @OneToMany(mappedBy = "jobOffer")
    private Set<JobOfferRequirement> requirements;

    @OneToMany(mappedBy = "jobOffer")
    private Set<JobOfferConsultation> jobConsultations;

    @OneToMany(mappedBy = "jobOffer")
    private Set<JobOfferInterest> jobInterests;

    @OneToMany(mappedBy = "jobOffer")
    private Set<JobOfferSaving> jobSavings;

    @JsonIgnore
    @OneToMany(mappedBy = "jobOffer")
    private Set<JobApplication> jobApplications;

    @ManyToOne
    @JoinColumn(name = "employerId")
    private Employer employer;

    private String consultations;
    private Long createdBy;
    private LocalDate creationDate;
    private Long updatedBy;
    private LocalDate updateDate;
    private Boolean isDeleted;
    private Long deletedBy;
    private LocalDate deleteDate;
}
