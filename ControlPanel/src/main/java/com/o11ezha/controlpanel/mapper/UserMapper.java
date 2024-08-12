package com.o11ezha.controlpanel.mapper;

import com.o11ezha.controlpanel.DTO.model.UserModel;
import com.o11ezha.controlpanel.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserModel userToUserModel(UserEntity user);
    UserEntity userDTOToUser(UserModel userDTO);

    @Mapping(target = "userId", ignore = true)
    UserModel userToUserModelWithoutId(UserEntity user);

    @Mapping(target = "userId", ignore = true)
    UserEntity userDTOToUserWithoutId(UserModel user);
}
