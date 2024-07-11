package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.entity.CompanyEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.repository.ICompanyRepository;
import group_6.e_contractor_backend.user.repository.IUserRepository;
import group_6.e_contractor_backend.user.service.spec.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class CompanyService implements ICompanyService {

    private final IUserRepository userRepository;
    private final ICompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final Path root = Paths.get("src/main/java/group_6/e_contractor_backend/user/uploads/companies");

    @Transactional
    public void registerCompany(UserEntity userEntity, CompanyEntity companyEntity, MultipartFile companyLogo) {
        // Encode the password
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        // Save the user entity
        UserEntity savedUser = userRepository.save(userEntity);

        // Set the saved user to the company entity
        companyEntity.setUser(savedUser);

        // Save the logo file
        if (companyLogo != null && !companyLogo.isEmpty()) {
            try {
                Files.createDirectories(root);
                String filename = companyLogo.getOriginalFilename();
                Path filePath = root.resolve(filename);
                Files.copy(companyLogo.getInputStream(), filePath);
                companyEntity.setCompanyLogo(filePath.toString());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save logo file", e);
            }
        }

        // Save the company entity
        companyRepository.save(companyEntity);

        sendRegistrationEmail(companyEntity);
    }

    private void sendRegistrationEmail(CompanyEntity companyEntity) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(companyEntity.getUser().getEmail());
        message.setSubject("Registration Confirmation");
        message.setText("Dear " + companyEntity.getUser().getEmail() + ",\n\nThank you for registering!");

        mailSender.send(message);
    }
}
