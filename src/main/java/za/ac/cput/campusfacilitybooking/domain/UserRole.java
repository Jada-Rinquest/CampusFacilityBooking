package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    private String userRoleId;

    private String userId;
    private String roleId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    protected UserRole() {
    }

    public UserRole(String userRoleId, String userId, String roleId) {
        this.userRoleId = userRoleId;
        this.userId = userId;
        this.roleId = roleId;
    }

    public String getUserRoleId() {
        return userRoleId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoleId() {
        return roleId;
    }
}
