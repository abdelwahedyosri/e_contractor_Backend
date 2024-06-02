package group_6.e_contractor_backend.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_image")
public class UserImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String userImageName;

    @Column
    private String userImageAlt;

    @Column(nullable = false)
    private Boolean isMain;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private Boolean isDeleted;
}
