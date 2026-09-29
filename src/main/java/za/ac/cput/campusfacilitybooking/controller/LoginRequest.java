package za.ac.cput.campusfacilitybooking.controller;

public class LoginRequest {
    private String loginId;
    private String registrarId;
    private String username;
    private String password;

    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }
    public String getRegistrarId() { return registrarId; }
    public void setRegistrarId(String registrarId) { this.registrarId = registrarId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
