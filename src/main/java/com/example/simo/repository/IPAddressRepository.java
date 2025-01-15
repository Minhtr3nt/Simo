package com.example.simo.repository;

import com.example.simo.model.IPAddress;
import com.example.simo.model.IPAddressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPAddressRepository extends JpaRepository<IPAddress, Long> {

    IPAddress findByIp(String ip);
    Optional<IPAddress> findByUser_UserNameAndIp(String userName, String ip);

    Optional<IPAddress> findByUser_UserNameAndStatus(String userName, IPAddressStatus status);

}
