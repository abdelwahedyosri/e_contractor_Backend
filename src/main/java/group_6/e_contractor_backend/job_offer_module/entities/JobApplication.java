package group_6.e_contractor_backend.job_offer_module.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobApplicationStatus;
import group_6.e_contractor_backend.job_offer_module.enumerations.JobOfferRequirementType;
import group_6.e_contractor_backend.user.entity.CandidateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    @JoinColumn(name = "candidateId")
    private CandidateEntity student;


    @OneToMany(mappedBy = "jobApplication")
    private Set<JobApplicationFile> jobApplicationFiles;

    @JsonIgnore
    @OneToMany(mappedBy = "jobApplication")
    private Set<JobApplicationAppointment> jobApplicationAppointments;

    @JsonIgnore
    @OneToMany(mappedBy = "jobApplication")
    private Set<JobApplicationApointment> jobApplicationApointments;

    @OneToMany(mappedBy = "jobApplication")
    private Set<JobApplicationRequirement> requirements;

    @Column(length = 100)
    @Enumerated(EnumType.STRING)
    private JobApplicationStatus applicationStatus;

    private Long declinedBy;
    private LocalDateTime declineDate;
    private LocalDateTime acceptedDate;
    private LocalDateTime approvalDate;
    private Long createdBy;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;

}
