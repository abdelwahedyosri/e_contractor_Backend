package group_6.e_contractor_backend.user.service.spec;

import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;

public interface ICandidateService {
    void registerCandidate(UserEntity userEntity, CandidateEntity candidateEntity);
}
