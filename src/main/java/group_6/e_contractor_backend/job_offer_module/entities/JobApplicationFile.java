package group_6.e_contractor_backend.job_offer_module.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class JobApplicationFile {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationFileId;
    private Boolean isResume;
    private String applicationFileType;
    private Boolean isStudentFile;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "applicationId")
    private JobApplication jobApplication;

    @ManyToOne
    @JoinColumn(name = "fileId")
    private JobFile jobFile;

    private Boolean isActive;

    private Long createdBy;
    private LocalDateTime creationDate;

    private Long deletedBy;
    private LocalDateTime deleteDate;
}
