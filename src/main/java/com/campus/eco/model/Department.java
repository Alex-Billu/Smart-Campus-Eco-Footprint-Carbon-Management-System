package com.campus.eco.model;

import java.math.BigDecimal;

public class Department {
    private int deptId;
    private String deptName;
    private String deptCode;
    private String building;
    private BigDecimal floorAreaSqm;
    private int staffCount;

    public Department() {}

    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public String getDeptCode() { return deptCode; }
    public void setDeptCode(String deptCode) { this.deptCode = deptCode; }
    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }
    public BigDecimal getFloorAreaSqm() { return floorAreaSqm; }
    public void setFloorAreaSqm(BigDecimal floorAreaSqm) { this.floorAreaSqm = floorAreaSqm; }
    public int getStaffCount() { return staffCount; }
    public void setStaffCount(int staffCount) { this.staffCount = staffCount; }

    @Override
    public String toString() { return deptCode + " - " + deptName; }
}
