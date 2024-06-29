package group_6.e_contractor_backend.job_offer_module.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    private String name;

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

