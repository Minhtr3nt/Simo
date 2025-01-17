package com.example.simo.dto.request;


import com.esotericsoftware.kryo.serializers.FieldSerializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountRequest {

    @NotNull(message = "NON_NULL")
    @Size(max = 36, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String cif;

    @NotNull(message = "NON_NULL")
    @Size(max = 15, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String soID;

    @NotNull(message = "NON_NULL")
    private int loaiID;

    @NotNull(message = "NON_NULL")
    @Size(max = 150, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String tenKhachHang;

    @NotNull(message = "NON_NULL")
    @Size(max = 10, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String ngaySinh;

    @NotNull(message = "NON_NULL")
    private int gioiTinh;

    @Size(max = 13,min = 10, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String maSoThue;

    @NotNull(message = "NON_NULL")
    @Size(max = 15, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String soDienThoaiDangKyDichVu;

    @Size(max = 300, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String diaChi;

    @NotNull(message = "NON_NULL")
    @Size(max = 60, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String diaChiKiemSoatTruyCap;

    @Size(max = 36, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String maSoNhanDangThietBiDiDong;

    @NotNull(message = "NON_NULL")
    @Size(max = 36, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String soTaiKhoan;

    private int loaiTaiKhoan;

    @NotNull(message = "NON_NULL")
    private int trangThaiHoatDongTaiKhoan;

    @NotNull(message = "NON_NULL")
    @Size(max = 10, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String ngayMoTaiKhoan;

    private int phuongThucMoTaiKhoan;

    @Size(max = 10, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String ngayXacThucTaiQuay;

    @NotNull(message = "NON_NULL")
    @Size(max = 36, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String quocTich;
}
