package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.repository.ICandidateRepository;
import group_6.e_contractor_backend.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final IUserRepository userRepository;
    private final ICandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerCandidate(UserEntity userEntity, CandidateEntity candidateEntity) {
        // Encode the password
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        // Save the user entity first
        UserEntity savedUser = userRepository.save(userEntity);

        // Set the saved user to the candidate entity
        candidateEntity.setUser(savedUser);

        // Save the candidate entity
        candidateRepository.save(candidateEntity);
    }
}
