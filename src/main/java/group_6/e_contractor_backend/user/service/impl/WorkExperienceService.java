package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.repository.IWorkExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkExperienceService {
    IWorkExperienceRepository workExperienceRepository;
}
