package com.mynt.exam.parcel.model;

public class Parcel {
    private double weight;
    private double height;
    private double width;
    private double length;
    private String voucherCode; // Add this field
    public Parcel(double weight, double height, double width, double length,String voucherCode) {
        this.weight = weight;
        this.height = height;
        this.width = width;
        this.length = length;
        this.voucherCode = voucherCode;
    }
    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }
    public String getVoucherCode() {
        return voucherCode;
    }
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getVolume() {
        return height * width * length;
    }
}
