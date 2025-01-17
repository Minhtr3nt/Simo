package com.example.simo.mapper;

import com.example.simo.dto.request.CustomerAccountRequest;
import com.example.simo.dto.request.SuspectedFraudAccountRequest;
import com.example.simo.model.CustomerAccount;
import com.example.simo.model.SuspectedFraudAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerAccountMapper {
    CustomerAccount toCustomerAccount(CustomerAccountRequest request);
}
