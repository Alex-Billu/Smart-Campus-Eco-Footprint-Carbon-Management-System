package com.campus.eco.model;

import java.math.BigDecimal;

public class TransportDetails {
    private int transportId;
    private int deptId;
    private String usageMonth;
    private String vehicleType;
    private BigDecimal distanceKm;
    private BigDecimal fuelLitres;
    private BigDecimal co2EmissionKg;

    public TransportDetails() {}

    public int getTransportId() { return transportId; }
    public void setTransportId(int transportId) { this.transportId = transportId; }
    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    public BigDecimal getFuelLitres() { return fuelLitres; }
    public void setFuelLitres(BigDecimal fuelLitres) { this.fuelLitres = fuelLitres; }
    public BigDecimal getCo2EmissionKg() { return co2EmissionKg; }
    public void setCo2EmissionKg(BigDecimal co2EmissionKg) { this.co2EmissionKg = co2EmissionKg; }
}
