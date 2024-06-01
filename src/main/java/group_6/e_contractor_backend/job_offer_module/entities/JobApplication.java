package group_6.e_contractor_backend.job_offer_module.entities;

import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationStatus;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferRequirementType;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
public class JobApplication {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "offerid")
    private JobOffer jobOffer;

    @OneToMany(mappedBy = "jobApplication")
    private Set<JobApplicationFile> jobApplicationFiles;

    @OneToMany(mappedBy = "jobApplication")
    private Set<JobApplicationAppointment> jobApplicationAppointments;

    @Enumerated(EnumType.STRING)
    private JobApplicationStatus applicationStatus;
    private Long createdBy;
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    private Long declinedBy;
    @Temporal(TemporalType.DATE)
    private Date declineDate;
    @Temporal(TemporalType.DATE)
    private Date acceptedDate;
    @Temporal(TemporalType.DATE)
    private Date approvalDate;
    @Temporal(TemporalType.DATE)
    private Date updateDate;

}
