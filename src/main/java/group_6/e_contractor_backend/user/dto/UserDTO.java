package group_6.e_contractor_backend.user.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDateTime dob;
    private Long phoneNumber;
    private String email;
    private Boolean isActive;
    private Boolean isApproved;
    private LocalDateTime creationDate;
    private String createdBy;
    private Boolean isDeleted;
    private String role; // Role assigned to the user
    private boolean twoWayVerificationEnabled;
}
