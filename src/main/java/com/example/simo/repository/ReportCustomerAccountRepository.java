package com.example.simo.repository;

import com.example.simo.model.ReportCustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCustomerAccountRepository extends JpaRepository<ReportCustomerAccount, String> {
}
