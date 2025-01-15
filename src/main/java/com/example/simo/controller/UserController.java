package com.example.simo.controller;

import com.example.simo.dto.request.UserCreateRequest;
import com.example.simo.dto.response.ApiResponse;
import com.example.simo.dto.response.UserResponse;
import com.example.simo.model.User;
import com.example.simo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/user")
public class UserController {
    private final UserService userService;

    @PostMapping("create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateRequest request){
        UserResponse user =  userService.createUser(request);
        return ResponseEntity.ok().body(new ApiResponse(200, "Create user success", user));
    }
}
