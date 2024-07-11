package group_6.e_contractor_backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreationDTO {
    private String role;
    private String createdBy;
    private Set<Long> permissionIds;
}
