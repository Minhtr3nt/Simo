package com.example.simo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Getter
@Setter
@Entity
public class SuspectedFraudAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NaturalId
    private String cif;
    private String soTaiKhoan;
    private String tenKhachHang;
    private int trangThaiHoatDongTaiKhoan;
    private int nghiNgo;
    private String ghiChu;
    private String threadName;

    @ManyToOne
    @JoinColumn(name = "report_id", referencedColumnName = "maYeuCau")
    private ReportCustomerAccount reportCustomerAccount;
}
