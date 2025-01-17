package com.example.simo.dto.request;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Data
@Getter
@Setter
public class SuspectedFraudAccountRequest {
    @NotNull(message = "NON_NULL")
    @Size(max = 36, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String cif;
    @NotNull(message = "NON_NULL")
    @Size(max = 36, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String soTaiKhoan;
    @NotNull(message = "NON_NULL")
    @Size(max = 150, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String tenKhachHang;
    @NotNull(message = "NON_NULL")
    @Size(max = 36, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private int trangThaiHoatDongTaiKhoan;
    @NotNull(message = "NON_NULL")
    private int nghiNgo;
    @Size(max = 500, message = "NOT_VALID_CHARACTERS_AMOUNT")
    private String ghiChu;
}
