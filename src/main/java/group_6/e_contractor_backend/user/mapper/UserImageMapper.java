package group_6.e_contractor_backend.user.mapper;

import group_6.e_contractor_backend.user.dto.UserImageDTO;
import group_6.e_contractor_backend.user.entity.UserImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserImageMapper {
    UserImageMapper INSTANCE = Mappers.getMapper(UserImageMapper.class);

    UserImageDTO toUserImageDTO(UserImageEntity userImage);

    UserImageEntity toUserImage(UserImageDTO userImageDTO);
}

