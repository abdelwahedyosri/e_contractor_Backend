package group_6.e_contractor_backend.job_offer_module.enumerations;

import jakarta.persistence.Enumerated;


public enum JobApplicationAppointmentStatus {
    Approved,
    Declined,
    Ongoing,
    Finished,
    Canceled,
    Booked,
    Absent_Employer,
    Absent_Student
}
