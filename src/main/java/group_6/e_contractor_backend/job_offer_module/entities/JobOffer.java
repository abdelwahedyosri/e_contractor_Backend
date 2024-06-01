package group_6.e_contractor_backend.job_offer_module.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Enumerated(EnumType.STRING)
    private JobOfferType type;

    @Enumerated(EnumType.STRING)
    private JobOfferStatus status;
    @Enumerated(EnumType.STRING)
    private JobWorkplaceType workspaceType;

    private String workplaceLocation;

    @Enumerated(EnumType.STRING)
    private JobEmploymentRegime employmentRegime;
    private String jobTitle;
    private String description;
    private Boolean allowSimpleApplications;

    @OneToMany(mappedBy = "jobOffer")
    private Set<JobOfferRequirement> requirements;

    @OneToMany(mappedBy = "jobOffer")
    private Set<JobApplication> jobApplications;

    private String keyWords;
    private String consultations;

    private Long createdBy;
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    private Long updatedBy;
    @Temporal(TemporalType.DATE)
    private Date updateDate;
    private Boolean isDeleted;
    private Long deletedBy;
    @Temporal(TemporalType.DATE)
    private Date deleteDate;
}
