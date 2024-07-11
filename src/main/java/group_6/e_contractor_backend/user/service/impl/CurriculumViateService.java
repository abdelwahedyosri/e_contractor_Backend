package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.repository.ICurriculumVitaeRepository;
import group_6.e_contractor_backend.user.service.spec.ICurriculumVitaeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurriculumViateService implements ICurriculumVitaeService {
    ICurriculumVitaeRepository curriculumVitaeRepository;
}
