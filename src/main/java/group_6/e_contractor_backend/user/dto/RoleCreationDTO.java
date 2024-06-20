package group_6.e_contractor_backend.user.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RoleCreationDTO {
    private String role;
    private String createdBy;
    private Set<Long> permissionIds;
}
