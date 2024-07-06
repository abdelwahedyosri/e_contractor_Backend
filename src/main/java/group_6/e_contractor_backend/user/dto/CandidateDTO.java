package group_6.e_contractor_backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String professionalTitle;
    private String location;
    private String photo;
    private String skills;
    private String dob;
    private String resumeCategory;
    private String resumeContent;
    private Double minRate;
    private String ProfilePhoto;

}
