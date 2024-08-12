package com.o11ezha.controlpanel.service.user;

import com.o11ezha.controlpanel.DAO.UserDAO;
import com.o11ezha.controlpanel.DTO.model.UserModel;
import com.o11ezha.controlpanel.DTO.request.RegistrationRequest;
import com.o11ezha.controlpanel.exception.user.InvalidRegistrationDataException;
import com.o11ezha.controlpanel.exception.user.RegistrationException;
import com.o11ezha.controlpanel.exception.user.UserAlreadyExistsException;
import com.o11ezha.controlpanel.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(RegistrationRequest registrationRequest) throws UserAlreadyExistsException, InvalidRegistrationDataException, RegistrationException {
        if (registrationRequest.getEmail() == null || registrationRequest.getPassword() == null) {
            throw new InvalidRegistrationDataException("Почта или пароль пустые.");
        }

        if (userDAO.existsByEmail(registrationRequest.getEmail())) {
            throw new UserAlreadyExistsException(String.format("Пользователь с почтой '%s' уже существует.", registrationRequest.getEmail()));
        }

        try {
            registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            UserModel userModel = UserModel.builder()
                    .email(registrationRequest.getEmail())
                    .password(registrationRequest.getPassword())
                    .fullName(registrationRequest.getFullName())
                    .build();
            userDAO.createUser(userModel);
        }
        catch (DataAccessException e) {
            throw new RegistrationException("Проблема с подключением к Базе Данных", e);
        } catch (Exception e) {
            throw new RegistrationException("Произошла следующая ошибка при регистрации: ", e);
        }
    }

    @Override
    public UserModel getUserByEmail(String email) throws UserNotFoundException {
        return userDAO.getUserByEmail(email);
    }
}
