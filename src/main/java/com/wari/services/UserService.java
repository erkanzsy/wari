package com.wari.services;

import com.wari.model.User;
import com.wari.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder p;

    public List<User> findAllByRoleEqUser(){
        return userRepository.findByRole("USER");
    }

    public User findById(int id){
        return userRepository.findById(id);
    }

    public boolean save(User user){
        if (userRepository.findByEmail(user.getEmail()) == null){
            user.setAktifMi(true);
            user.setParola(p.encode(user.getParola()));
            user.setRole("USER");
            userRepository.save(user);
            return true;
        }else
            return false;

    }

    public void setDisableUser(int id) {
        User user = userRepository.findById(id);
        user.setAktifMi(false);
        userRepository.save(user);
    }

    public void setActiveUser(int id) {
        User user = userRepository.findById(id);
        user.setAktifMi(true);
        userRepository.save(user);
    }

    public void changeRole(int id) {
        User user = userRepository.findById(id);
        user.setRole("ADMIN");
        userRepository.save(user);
    }
}
