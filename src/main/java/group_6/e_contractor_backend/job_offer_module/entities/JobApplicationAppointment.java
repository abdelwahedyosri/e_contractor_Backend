package group_6.e_contractor_backend.job_offer_module.entities;

import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private JobApplicationAppointmentType appointment_type;
    private String dotColor;
    @Enumerated(EnumType.STRING)
    private JobApplicationAppointmentStatus appointment_status;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    private String appointment_duration;
    private String employer_comment;
    private String student_comment;
    @Temporal(TemporalType.DATE)
    private LocalDate declineDate;
    @Temporal(TemporalType.DATE)
    private LocalDate approvalDate;
    private Long createdBy;
    @Temporal(TemporalType.DATE)
    private LocalDate creationDate;
    private Long updatedBy;
    @Temporal(TemporalType.DATE)
    private LocalDate updateDate;

    private Boolean isDeleted;
    private Long deletedBy;
    @Temporal(TemporalType.DATE)
    private LocalDate deleteDate;

}
