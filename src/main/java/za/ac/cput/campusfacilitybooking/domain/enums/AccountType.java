package za.ac.cput.campusfacilitybooking.domain.enums;

/*
 * Whether an account belongs to a student or a staff member,
 * decided by the domain of the account's email address:
 *   students -> @mycput.ac.za (or @mycput.co.za)
 *   staff    -> @cput.ac.za
 */
public enum AccountType {
    STUDENT,
    STAFF;

    public static AccountType fromEmail(String email) {
        if (email == null) {
            return null;
        }
        String normalised = email.trim().toLowerCase();
        if (normalised.endsWith("@mycput.ac.za") || normalised.endsWith("@mycput.co.za")) {
            return STUDENT;
        }
        if (normalised.endsWith("@cput.ac.za")) {
            return STAFF;
        }
        return null;
    }
}
