package group_6.e_contractor_backend.user.service.impl;

import group_6.e_contractor_backend.user.dto.UserImageDTO;
import group_6.e_contractor_backend.user.entity.UserImageEntity;
import group_6.e_contractor_backend.user.mapper.UserImageMapper;
import group_6.e_contractor_backend.user.repository.IUserImageRepository;
import group_6.e_contractor_backend.user.service.spec.IUserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserImageService implements IUserImageService {

    @Autowired
    private IUserImageRepository userImageRepository;

    @Autowired
    private UserImageMapper userImageMapper;

    @Override
    public List<UserImageDTO> findAll() {
        return userImageRepository.findAll().stream()
                .map(userImageMapper::toUserImageDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserImageDTO> findById(Long id) {
        return userImageRepository.findById(id)
                .map(userImageMapper::toUserImageDTO);
    }

    @Override
    public UserImageDTO save(UserImageDTO userImageDTO) {
        UserImageEntity userImage = userImageMapper.toUserImage(userImageDTO);
        UserImageEntity savedUserImage = userImageRepository.save(userImage);
        return userImageMapper.toUserImageDTO(savedUserImage);
    }

    @Override
    public void deleteById(Long id) {
        userImageRepository.deleteById(id);
    }


    @Override
    public Page<UserImageDTO> findAllWithIsMain(Boolean isMain, Pageable pageable) {
        if (isMain == null) {
            return userImageRepository.findAll(pageable)
                    .map(userImageMapper::toUserImageDTO);
        } else {
            return userImageRepository.findByIsMain(isMain, pageable)
                    .map(userImageMapper::toUserImageDTO);
        }
    }
}
