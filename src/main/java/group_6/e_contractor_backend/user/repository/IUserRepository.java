package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByFirstName(String firstName);
    List<UserEntity> findByLastName(String lastName);
    List<UserEntity> findByIsActive(Boolean isActive);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailAndPassword(String email, String password);
    @Query("SELECT u FROM UserEntity u WHERE " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<UserEntity> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT u FROM UserEntity u WHERE u.role.role NOT IN (:excludedRoles)")
    Page<UserEntity> findByRolesNotIn(@Param("excludedRoles") List<String> excludedRoles, Pageable pageable);

    UserEntity getUserEntityByUsername(String username);
}
