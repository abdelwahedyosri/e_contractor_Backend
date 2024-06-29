package group_6.e_contractor_backend.job_offer_module.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class JobFile {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private String fileName;
    private String fileOriginalName;
    private Long fileSize;
    private String fileToken;
    private String filePathUrl;
    private String fileType;
    private String fileExtension;

    @JsonIgnore
    @OneToMany(mappedBy = "jobFile")
    private Set<JobApplicationFile> jobApplicationFiles;

    private Boolean isActive;

    private Long createdBy;
    @Temporal(TemporalType.DATE)
    private LocalDate creationDate;

    private Long deletedBy;
    @Temporal(TemporalType.DATE)
    private LocalDate deleteDate;
}
