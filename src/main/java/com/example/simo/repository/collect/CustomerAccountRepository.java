package com.example.simo.repository.collect;

import com.example.simo.model.CustomerAccount;
import com.example.simo.model.ReportCustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, String> {
    Set<CustomerAccount> findByReportCustomerAccount(ReportCustomerAccount report);
}
