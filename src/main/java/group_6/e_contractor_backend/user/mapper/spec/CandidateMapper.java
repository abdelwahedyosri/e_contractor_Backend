package group_6.e_contractor_backend.user.mapper.spec;

import group_6.e_contractor_backend.user.dto.CandidateDTO;
import group_6.e_contractor_backend.user.entity.CandidateEntity;

public interface CandidateMapper {
    CandidateDTO toDTO(CandidateEntity candidateEntity);
}
