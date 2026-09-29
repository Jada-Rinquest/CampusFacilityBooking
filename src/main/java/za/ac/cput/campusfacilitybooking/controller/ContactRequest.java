package za.ac.cput.campusfacilitybooking.controller;

public class ContactRequest {
    private String contactId;
    private String contact;
    private String description;
    private String userId;

    // Getters and Setters
    public String getContactId() { return contactId; }
    public void setContactId(String contactId) { this.contactId = contactId; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
