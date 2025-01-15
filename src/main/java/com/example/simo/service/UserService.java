package com.example.simo.service;

import com.example.simo.dto.request.UserCreateRequest;
import com.example.simo.dto.response.UserResponse;
import com.example.simo.model.User;

import com.example.simo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserResponse createUser(UserCreateRequest request){

        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setConsumerKey(request.getConsumerKey());
        user.setSecretKey(request.getSecretKey());

        userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }
}
