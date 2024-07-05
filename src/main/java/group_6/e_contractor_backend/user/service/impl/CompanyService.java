package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.entity.CompanyEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import group_6.e_contractor_backend.user.repository.ICompanyRepository;
import group_6.e_contractor_backend.user.repository.IUserRepository;
import group_6.e_contractor_backend.user.service.spec.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService implements ICompanyService {

    private final IUserRepository userRepository;
    private final ICompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerCompany(UserEntity userEntity, CompanyEntity companyEntity) {
        // Encode the password
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        // Save the user entity
        userRepository.save(userEntity);

        // Set the user entity in the company entity
        companyEntity.setUser(userEntity);

        // Save the company entity
        companyRepository.save(companyEntity);
    }
}

