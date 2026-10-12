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

    private Login(Builder builder) {
        this.loginId = builder.loginId;
        this.registrarId = builder.registrarId;
        this.username = builder.username;
        this.password = builder.password;
    }

    public static class Builder {
        private String loginId;
        private String registrarId;
        private String username;
        private String password;

        public Builder setLoginId(String loginId) {
            this.loginId = loginId;
            return this;
        }

        public Builder setRegistrarId(String registrarId) {
            this.registrarId = registrarId;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Login build() {
            return new Login(this);
        }
    }
}