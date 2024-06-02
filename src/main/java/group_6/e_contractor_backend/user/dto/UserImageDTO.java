package group_6.e_contractor_backend.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserImageDTO {
    private Long userImageId;
    private String userImageName;
    private String userImageAlt;
    private Boolean isMain;
    private LocalDateTime creationDate;
    private String createdBy;
    private Boolean isDeleted;
}