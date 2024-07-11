package group_6.e_contractor_backend.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "curricilum_vitae")
public class CurricilumVitaeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cvId;
    @ManyToOne
    UserEntity user;
    @OneToMany
    Set<SkillsEntity> skills;
    @OneToMany
    Set<WorkExperienceEntity> workExperience;
    @OneToMany
    Set<EducationEntity> educations;
}
