package com.campus.eco.model;

public class Admin {
    private int adminId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;

    public Admin() {}
    public Admin(String username, String passwordHash) { this.username = username; this.passwordHash = passwordHash; }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
