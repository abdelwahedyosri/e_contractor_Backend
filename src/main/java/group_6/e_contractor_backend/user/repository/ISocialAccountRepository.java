package group_6.e_contractor_backend.user.repository;

import group_6.e_contractor_backend.user.entity.SocialAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISocialAccountRepository extends JpaRepository<SocialAccountEntity, Long> {
    SocialAccountEntity findByProviderAndProviderUserId(String provider, String providerUserId);
}