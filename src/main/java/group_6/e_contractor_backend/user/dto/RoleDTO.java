package group_6.e_contractor_backend.user.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class RoleDTO {
    private Long roleId;
    private String role;
    private String roleDescription;
    private String createdBy;
    private LocalDateTime creationDate;
    private Boolean isDeleted;
    private Set<PermissionDTO> permissions;
}
