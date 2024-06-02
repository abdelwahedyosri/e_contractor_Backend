package group_6.e_contractor_backend.user.service.spec;

import group_6.e_contractor_backend.user.dto.UserImageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserImageService {
    List<UserImageDTO> findAll();
    Optional<UserImageDTO> findById(Long id);
    UserImageDTO save(UserImageDTO userImageDTO);
    void deleteById(Long id);
    Page<UserImageDTO> findAllWithIsMain(Boolean isMain, Pageable pageable);
}
