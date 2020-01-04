package com.wari.security;

import com.wari.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (userRepository.findByEmail(s) != null) {
            UserPrincipal userPrincipal = new UserPrincipal(userRepository.findByEmail(s));
            return userPrincipal;
        }
        throw  new UsernameNotFoundException(s +" not found");
    }
}
