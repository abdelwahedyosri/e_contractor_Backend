package group_6.e_contractor_backend.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(nullable = false)
    private String role;

    @Column
    private String roleDescription;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private Boolean isDeleted;
}
