package group_6.e_contractor_backend.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @Column(nullable = true)
    private String roleDescription;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "role")
    private Set<UserEntity> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> permissions = new HashSet<>();
}
