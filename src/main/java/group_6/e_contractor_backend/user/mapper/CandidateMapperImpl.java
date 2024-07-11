package group_6.e_contractor_backend.user.mapper;

import group_6.e_contractor_backend.user.dto.CandidateDTO;
import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.mapper.spec.CandidateMapper;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapperImpl implements CandidateMapper {

    @Override
    public CandidateDTO toDTO(CandidateEntity candidateEntity) {
        CandidateDTO dto = new CandidateDTO();
        dto.setFirstName(candidateEntity.getFirstName());
        dto.setLastName(candidateEntity.getLastName());
        dto.setProfessionalTitle(candidateEntity.getProfessionalTitle());
        dto.setLocation(candidateEntity.getLocation());
        dto.setDob(candidateEntity.getDob());
        dto.setProfilePhoto(candidateEntity.getPhoto());
        dto.setSkills(candidateEntity.getSkills());
        dto.setMinRate(candidateEntity.getMinRate());
        dto.setResumeCategory(candidateEntity.getResumeCategory());
        dto.setResumeContent(candidateEntity.getResumeContent());
        return dto;
    }
}
