package group_6.e_contractor_backend.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private Long phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private String dob;

    @Column(nullable = true)
    private String location;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private Boolean isActive;

    @Column(nullable = true)
    private LocalDateTime creationDate;

    @Column(nullable = true)
    private String createdBy;

    @Column(nullable = true)
    private Boolean isDeleted;

    @Column(nullable = true)
    private Boolean twoWayVerificationEnabled;

    @Column(nullable = true)
    private String avatar;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role != null ? Collections.singleton(new SimpleGrantedAuthority(role.getRole())) : Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive != null && isActive;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SocialAccountEntity> socialAccounts;

    @Column(nullable = true)
    private LocalDateTime lastLogin;
}
