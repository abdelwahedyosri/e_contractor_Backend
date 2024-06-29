package group_6.e_contractor_backend.job_offer_module.entities;

import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationStatus;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferRequirementType;
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
public class JobApplication {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;
    private String reference;

    private String email;
    private Long phoneNumber;
    private String studentAddress;
    private String studentTitle;
    private String studentName;

    @ManyToOne
    @JoinColumn(name = "offerId")
    private JobOffer jobOffer;

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;


    @OneToMany(mappedBy = "jobApplication")
    private Set<JobApplicationFile> jobApplicationFiles;

    @OneToMany(mappedBy = "jobApplication")
    private Set<JobApplicationAppointment> jobApplicationAppointments;

    @Enumerated(EnumType.STRING)
    private JobApplicationStatus applicationStatus;

    private Long declinedBy;
    @Temporal(TemporalType.DATE)
    private LocalDate declineDate;
    @Temporal(TemporalType.DATE)
    private LocalDate acceptedDate;
    @Temporal(TemporalType.DATE)
    private LocalDate approvalDate;
    private Long createdBy;
    @Temporal(TemporalType.DATE)
    private LocalDate creationDate;
    @Temporal(TemporalType.DATE)
    private LocalDate updateDate;

}
