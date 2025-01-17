package com.example.simo.repository.collect;

import com.example.simo.model.ReportCustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportCustomerAccount, String> {

}
