package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.dto.CandidateDTO;
import group_6.e_contractor_backend.user.entity.CandidateEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.mapper.CandidateMapperImpl;
import group_6.e_contractor_backend.user.repository.ICandidateRepository;
import group_6.e_contractor_backend.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateMapperImpl candidateMapper;
    private final IUserRepository userRepository;
    private final ICandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final Path root = Paths.get("src/main/java/group_6/e_contractor_backend/user/uploads/candidates");

    @Transactional
    public void registerCandidate(UserEntity userEntity, CandidateEntity candidateEntity, MultipartFile photo) {
        // Encode the password
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        // Save the user entity first
        UserEntity savedUser = userRepository.save(userEntity);

        // Set the saved user to the candidate entity
        candidateEntity.setUser(savedUser);
        System.out.println("photo"+photo);
        // Save the photo file
        if (photo != null && !photo.isEmpty()) {
            try {
                Files.createDirectories(root);
                String filename = photo.getOriginalFilename();
                Path filePath = root.resolve(filename);
                Files.copy(photo.getInputStream(), filePath);
                candidateEntity.setPhoto(filePath.toString());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save photo file", e);
            }
        }

        // Save the candidate entity
        candidateRepository.save(candidateEntity);

        sendRegistrationEmail(candidateEntity);
    }

    private void sendRegistrationEmail(CandidateEntity candidateEntity) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(candidateEntity.getUser().getEmail());
        message.setSubject("Registration Confirmation");
        message.setText("Dear " + candidateEntity.getUser().getEmail() + ",\n\nThank you for registering!");

        mailSender.send(message);
    }

    @Transactional(readOnly = true)
    public Page<CandidateDTO> getCandidates(String location, String skills, String jobType, Pageable pageable) {
        Page<CandidateEntity> candidates = candidateRepository.findByFilters(location, skills, jobType, pageable);
        List<CandidateDTO> candidateDTOs = candidates.getContent()
                .stream()
                .map(candidateMapper::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(candidateDTOs, pageable, candidates.getTotalElements());
    }
}
