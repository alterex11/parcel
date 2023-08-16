package com.mynt.exam.parcel.model;


import jakarta.persistence.*;

@Entity
@Table(name = "parcelcostrule")
public class ParcelCostRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conditionType;
    private String conditionOperator;
    private double conditionValue;
    private double costMultiplier;

    private String multiplierType;
    private String remarks;

    // Constructors, getters, and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getConditionOperator() {
        return conditionOperator;
    }

    public void setConditionOperator(String conditionOperator) {
        this.conditionOperator = conditionOperator;
    }

    public double getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(double conditionValue) {
        this.conditionValue = conditionValue;
    }

    public double getCostMultiplier() {
        return costMultiplier;
    }

    public void setCostMultiplier(double costMultiplier) {
        this.costMultiplier = costMultiplier;
    }

    public void setMultiplierType(String multiplierType) {
        this.multiplierType = multiplierType;
    }
    public String getMultiplierType() {
        return multiplierType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}