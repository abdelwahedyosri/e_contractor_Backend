package group_6.e_contractor_backend.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleDTO {
    private Long roleId;
    private String role;
    private String roleDescription;
    private LocalDateTime creationDate;
    private String createdBy;
    private Boolean isDeleted;
}
