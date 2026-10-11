package com.campus.eco.model;

public class GreenPoints {
    private int pointId;
    private int deptId;
    private String usageMonth;
    private int points;
    private String reason;

    public GreenPoints() {}

    public int getPointId() { return pointId; }
    public void setPointId(int pointId) { this.pointId = pointId; }
    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
