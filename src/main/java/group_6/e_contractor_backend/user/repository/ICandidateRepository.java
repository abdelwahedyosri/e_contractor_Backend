package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.CandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ICandidateRepository extends JpaRepository<CandidateEntity, Long> {

    @Query("SELECT c FROM CandidateEntity c WHERE " +
            "(:location IS NULL OR c.location LIKE %:location%) AND " +
            "(:skills IS NULL OR c.skills LIKE %:skills%) AND " +
            "(:jobType IS NULL OR c.resumeCategory LIKE %:jobType%)")
    Page<CandidateEntity> findByFilters(@Param("location") String location,
                                        @Param("skills") String skills,
                                        @Param("jobType") String jobType,
                                        Pageable pageable);
}
