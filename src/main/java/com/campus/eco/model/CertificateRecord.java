package com.campus.eco.model;

public class CertificateRecord {
    private int certId;
    private Integer deptId;
    private String certType;
    private String certMonth;
    private String title;
    private String description;

    public CertificateRecord() {}

    public int getCertId() { return certId; }
    public void setCertId(int certId) { this.certId = certId; }
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getCertType() { return certType; }
    public void setCertType(String certType) { this.certType = certType; }
    public String getCertMonth() { return certMonth; }
    public void setCertMonth(String certMonth) { this.certMonth = certMonth; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
