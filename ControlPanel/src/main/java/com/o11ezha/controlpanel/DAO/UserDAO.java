package com.o11ezha.controlpanel.DAO;

import com.o11ezha.controlpanel.DTO.model.UserModel;
import com.o11ezha.controlpanel.entity.UserEntity;
import com.o11ezha.controlpanel.exception.user.UserNotFoundException;
import com.o11ezha.controlpanel.mapper.UserMapper;
import com.o11ezha.controlpanel.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserDAO {
    private final UserRepo userRepo;

    public UserModel getUserByEmail(String email) throws UserNotFoundException {
        UserEntity user = userRepo.getByEmail(email).orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с email '%s' не найден", email)));
        return UserMapper.INSTANCE.userToUserModel(user);
    }

    public UserModel getUserById(UUID userId) throws UserNotFoundException {
        UserEntity user = userRepo.getByUserId(userId).orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с id '%s' не найден", userId)));
        return UserMapper.INSTANCE.userToUserModel(user);
    }

    public void createUser(UserModel userModel) {
        UserEntity user =  UserMapper.INSTANCE.userDTOToUserWithoutId(userModel);
        userRepo.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepo.getByEmail(email).isPresent();
    }

}
