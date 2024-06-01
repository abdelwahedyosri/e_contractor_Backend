package group_6.e_contractor_backend.job_offer_module.entities;

import group_6.e_contractor_backend.job_offer_module.enumerations.*;
import jakarta.persistence.*;

import java.util.Date;
@Entity
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
    private Date declineDate;
    @Temporal(TemporalType.DATE)
    private Date approvalDate;
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
