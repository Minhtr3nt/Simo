package com.example.simo.controller.simo;

import com.example.simo.dto.request.RoleCreateRequest;
import com.example.simo.dto.response.ApiResponse;
import com.example.simo.dto.response.RoleResponse;
import com.example.simo.service.simo.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/role")
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createRole(@RequestBody RoleCreateRequest request){
        RoleResponse roleResponse = roleService.createRole(request);
        return ResponseEntity.ok().body(new ApiResponse(200, "Create role success", roleResponse));
    }
}
