package com.example.simo.repository.collect;

import com.example.simo.model.SuspectedFraudAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuspectedFraudAccountRepository extends JpaRepository<SuspectedFraudAccount, String> {
}
