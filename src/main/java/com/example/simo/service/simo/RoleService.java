package com.example.simo.service.simo;

import com.example.simo.dto.request.RoleCreateRequest;
import com.example.simo.dto.response.RoleResponse;
import com.example.simo.model.Role;
import com.example.simo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleResponse createRole(RoleCreateRequest request){
        Role role  = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        roleRepository.save(role);
        return modelMapper.map(role, RoleResponse.class);
    }

}
