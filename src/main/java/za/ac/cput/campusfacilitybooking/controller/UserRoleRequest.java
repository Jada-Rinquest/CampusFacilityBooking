package za.ac.cput.campusfacilitybooking.controller;

import za.ac.cput.campusfacilitybooking.domain.enums.Role;

public class UserRoleRequest {
    private String userRoleId;
    private String userId;
    private Role role;

    // Getters and Setters
    public String getUserRoleId() { return userRoleId; }
    public void setUserRoleId(String userRoleId) { this.userRoleId = userRoleId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
