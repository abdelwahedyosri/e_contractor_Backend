package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.SkillsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISkillsRepository extends JpaRepository<SkillsEntity, Long> {
}
