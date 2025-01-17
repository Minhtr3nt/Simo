package com.example.simo.service.simo;

import com.example.simo.dto.request.UserAccountRequest;
import com.example.simo.exception.ErrorCode;
import com.example.simo.exception.SimoException;
import com.example.simo.model.IPAddress;
import com.example.simo.model.IPAddressStatus;
import com.example.simo.model.User;
import com.example.simo.repository.IPAddressRepository;
import com.example.simo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IPAddressService {

    private final IPAddressRepository ipAddressRepository;
    private final UserRepository userRepository;

    public boolean CheckIPAddress(HttpServletRequest request, UserAccountRequest userAccountRequest){

        User user = userRepository.findByUserName(userAccountRequest.getUserName())
                .orElseThrow(()-> new SimoException(ErrorCode.USER_NOT_FOUND));
        String ip = request.getHeader("X-Forwarded-For");

        if(ip==null || ip.isEmpty() || "unknow".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }

       Optional<IPAddress> ipAddress = ipAddressRepository
               .findByUser_UserNameAndIp(userAccountRequest.getUserName(), ip);

        if(ipAddress.isEmpty()){
            IPAddress newIPAddress = new IPAddress();
            newIPAddress.setIp(ip);
            newIPAddress.setUser(user);
            Optional<IPAddress> inUsedIp = ipAddressRepository.findByUser_UserNameAndStatus
                    (userAccountRequest.getUserName(), IPAddressStatus.IN_USED);

            if(inUsedIp.isEmpty()) {
                newIPAddress.setStatus(IPAddressStatus.IN_USED);
                ipAddressRepository.save(newIPAddress);
                return true;
            }else{
                newIPAddress.setStatus(IPAddressStatus.NONE_USED);
                ipAddressRepository.save(newIPAddress);
                throw new SimoException(ErrorCode.ACCOUNT_IN_USED);
            }
        }

        if(ipAddress.get().getStatus()== IPAddressStatus.IN_USED){
            return true;
        }
        throw new SimoException(ErrorCode.IP_INVALID);
    }
}
