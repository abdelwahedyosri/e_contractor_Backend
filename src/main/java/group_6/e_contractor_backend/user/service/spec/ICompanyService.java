package group_6.e_contractor_backend.user.service.spec;

import group_6.e_contractor_backend.user.entity.CompanyEntity;
import group_6.e_contractor_backend.user.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ICompanyService {
    void registerCompany(UserEntity userEntity, CompanyEntity companyEntity, MultipartFile companyLogo);
}
