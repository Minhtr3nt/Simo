package com.example.simo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

@Entity
@Getter
@Setter
public class CustomerAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NaturalId
    private String cif;
    private String soID;
    private int loaiID;
    private String tenKhachHang;
    private String ngaySinh;
    private int gioiTinh;
    private String maSoThue;
    private String soDienThoaiDangKyDichVu;
    private String diaChi;
    private String diaChiKiemSoatTruyCap;
    private String maSoNhanDangThietBiDiDong;
    private String soTaiKhoan;
    private int loaiTaiKhoan;
    private int trangThaiHoatDongTaiKhoan;
    private String ngayMoTaiKhoan;
    private int phuongThucMoTaiKhoan;
    private String ngayXacThucTaiQuay;
    private String quocTich;

    @ManyToOne
    @JoinColumn(name = "report_id")
    ReportCustomerAccount reportCustomerAccount;

}
