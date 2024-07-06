package group_6.e_contractor_backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long roleId;
    private String role;
    private String roleDescription;
    private String createdBy;
    private LocalDateTime creationDate;
    private Boolean isDeleted;
    private Set<PermissionDTO> permissions;
}
