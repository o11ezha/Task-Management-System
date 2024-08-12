package com.o11ezha.controlpanel.security.userdetails;

import com.o11ezha.controlpanel.DAO.UserDAO;
import com.o11ezha.controlpanel.entity.UserEntity;
import com.o11ezha.controlpanel.exception.user.UserNotFoundException;
import com.o11ezha.controlpanel.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ControlPanelUserDetailsServices implements UserDetailsService {
    private final UserDAO userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserEntity user = UserMapper.INSTANCE.userDTOToUser(userDao.getUserByEmail(email));
            return ControlPanelUserDetails.buildUserDetails(user);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
