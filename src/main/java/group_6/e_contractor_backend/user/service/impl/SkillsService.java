package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.repository.ISkillsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillsService {
    ISkillsRepository skillsRepository;
}
