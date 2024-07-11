package group_6.e_contractor_backend.job_offer_module.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationAppointment {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "applicationId")
    private JobApplication jobApplication;

    private String title;
    private String description;
    private String meetLink;

    @Enumerated(EnumType.STRING)
    private JobApplicationAppointmentType appointmentType;
    private String dotColor;

    @Enumerated(EnumType.STRING)
    private JobApplicationAppointmentStatus appointmentStatus;

    private String appointmentLocation;
    private String appointmentAddress;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String appointmentDuration;
    private String employerComment;
    private String studentComment;
    private LocalDateTime declineDate;
    private LocalDateTime approvalDate;
    private Long createdBy;
    private LocalDateTime creationDate;
    private Long updatedBy;
    private LocalDateTime updateDate;

    private Boolean isDeleted;
    private Long deletedBy;
    private LocalDateTime deleteDate;

}
