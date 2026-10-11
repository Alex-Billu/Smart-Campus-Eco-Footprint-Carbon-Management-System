package com.campus.eco.model;

public class QRTracking {
    private int qrId;
    private Integer deptId;
    private String resourceType;
    private String encodedData;

    public QRTracking() {}

    public int getQrId() { return qrId; }
    public void setQrId(int qrId) { this.qrId = qrId; }
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getEncodedData() { return encodedData; }
    public void setEncodedData(String encodedData) { this.encodedData = encodedData; }
}
