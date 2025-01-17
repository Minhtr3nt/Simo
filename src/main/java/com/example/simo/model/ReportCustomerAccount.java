package com.example.simo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Getter
@Setter
@Entity
public class ReportCustomerAccount {

    @Id
    private String maYeuCau;

    private String kyBaoCao;

    @OneToMany(mappedBy = "reportCustomerAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CustomerAccount> customerAccounts;
}
