package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "login")
public class Login {

    @Id
    private String loginId;

    private String registrarId;
    private String username;
    private String password;

    protected Login() {
    }

    public Login(String loginId, String registrarId,
                 String username, String password) {
        this.loginId = loginId;
        this.registrarId = registrarId;
        this.username = username;
        this.password = password;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getRegistrarId() {
        return registrarId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    @Override
    public String toString() {
        return "Login{" +
                "loginId='" + loginId + '\'' +
                ", registrarId='" + registrarId + '\'' +
                ", username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}