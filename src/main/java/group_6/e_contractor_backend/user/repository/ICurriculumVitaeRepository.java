package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.CompanyEntity;
import group_6.e_contractor_backend.user.entity.CurricilumVitaeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICurriculumVitaeRepository extends JpaRepository<CurricilumVitaeEntity, Long> {

}
