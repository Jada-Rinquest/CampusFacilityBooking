package za.ac.cput.campusfacilitybooking.domain;

/*Author: Milani Sani(230371574)
Date: 21 June 2026
 */

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    private String staffId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Staff() {
    }

    public Staff(String staffId, User user) {
        this.staffId = staffId;
        this.user = user;
    }

    public String getStaffId() {
        return staffId;
    }

    public User getUser() {
        return user;
    }
}