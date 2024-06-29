package group_6.e_contractor_backend.job_offer_module.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobOfferConsultation {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultationId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "offerId")
    private JobOffer jobOffer;

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    private Long consultationsNumber;
}
