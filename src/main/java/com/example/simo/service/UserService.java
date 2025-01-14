package com.example.simo.service;

import com.example.simo.model.Key;
import com.example.simo.model.User;
import com.example.simo.repository.KeyRepository;

import com.example.simo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String userName, String password){
        User user = new User();
        if(userRepository.findByUserName(userName)!=null){
            throw new RuntimeException("Username existed");
        }
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
}
