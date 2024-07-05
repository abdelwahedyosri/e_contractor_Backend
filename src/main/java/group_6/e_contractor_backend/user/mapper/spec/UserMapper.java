package group_6.e_contractor_backend.user.mapper.spec;

import group_6.e_contractor_backend.user.dto.UserCreationDTO;
import group_6.e_contractor_backend.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


public interface UserMapper {


    UserEntity toEntity(UserCreationDTO userCreationDTO);

}

