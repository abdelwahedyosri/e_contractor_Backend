package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.UserImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserImageRepository extends JpaRepository<UserImageEntity, Long> {
    Page<UserImageEntity> findByIsMain(Boolean isMain, Pageable pageable);
}
