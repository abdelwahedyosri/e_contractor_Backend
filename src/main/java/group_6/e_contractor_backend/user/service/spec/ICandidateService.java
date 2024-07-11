package group_6.e_contractor_backend.user.service.spec;

import group_6.e_contractor_backend.user.dto.CandidateDTO;
import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICandidateService {
    void registerCandidate(UserEntity userEntity, CandidateEntity candidateEntity);
    Page<CandidateDTO> getCandidates(String location, String skills, String jobType, Pageable pageable);
}
