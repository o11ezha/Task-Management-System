package com.o11ezha.controlpanel.service.user;

import com.o11ezha.controlpanel.DTO.model.UserModel;
import com.o11ezha.controlpanel.DTO.request.RegistrationRequest;
import com.o11ezha.controlpanel.exception.user.InvalidRegistrationDataException;
import com.o11ezha.controlpanel.exception.user.RegistrationException;
import com.o11ezha.controlpanel.exception.user.UserAlreadyExistsException;
import com.o11ezha.controlpanel.exception.user.UserNotFoundException;

public interface UserService {
    void registerUser(RegistrationRequest registrationRequest) throws UserAlreadyExistsException, InvalidRegistrationDataException, RegistrationException, UserNotFoundException;

    UserModel getUserByEmail(String email) throws UserNotFoundException;
}
