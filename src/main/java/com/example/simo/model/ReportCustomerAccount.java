package com.example.simo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class ReportCustomerAccount {

    @Id
    @NaturalId
    private String maYeuCau;

    private String kyBaoCao;

    private String loaiBaoCao;

    @OneToMany(mappedBy = "reportCustomerAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CustomerAccount> customerAccounts;

    @OneToMany(mappedBy = "reportCustomerAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<SuspectedFraudAccount> suspectedFraudAccounts;
}
