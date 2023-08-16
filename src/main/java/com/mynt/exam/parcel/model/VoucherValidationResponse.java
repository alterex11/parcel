package com.mynt.exam.parcel.model;

import java.time.LocalDate;

public class VoucherValidationResponse {
    private String code;
    private int discount;
    private LocalDate expiry;

    // Constructors, getters, and setters

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }
}