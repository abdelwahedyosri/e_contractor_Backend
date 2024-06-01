package group_6.e_contractor_backend.job_offer_module.entities;

import jakarta.persistence.*;

import java.util.Date;
@Entity
public class JobApplicationFile {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationFileId;
    private Boolean isCv;
    private String file_name;
    private String original_file_name;
    private String file_token;
    private String file_path_url;
    @ManyToOne
    @JoinColumn(name = "applicationId")
    private JobApplication jobApplication;
    private Boolean isActive;

    private Long createdBy;
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    private Long deletedBy;
    @Temporal(TemporalType.DATE)
    private Date deleteDate;
}
