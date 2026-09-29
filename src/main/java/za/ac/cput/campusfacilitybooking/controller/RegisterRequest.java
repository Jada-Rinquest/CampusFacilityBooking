package za.ac.cput.campusfacilitybooking.controller;

import java.time.LocalDate;

public class RegisterRequest {
    private String registrarId;
    private String email;
    private LocalDate dateRegistered;

    // Getters and Setters
    public String getRegistrarId() { return registrarId; }
    public void setRegistrarId(String registrarId) { this.registrarId = registrarId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDateRegistered() { return dateRegistered; }
    public void setDateRegistered(LocalDate dateRegistered) { this.dateRegistered = dateRegistered; }
}
