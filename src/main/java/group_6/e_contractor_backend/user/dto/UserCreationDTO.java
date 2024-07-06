package group_6.e_contractor_backend.user.dto;

import group_6.e_contractor_backend.user.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String gender;
    private String email;
    private Long phoneNumber;
    private String dob;
    private String location;
    private RoleEntity role;
    private String avatar;
    private String password;
    private Boolean isActive;
    private String CreatedBy;

}
