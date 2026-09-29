//Angelia Van der Westhuizen 12/07/2026
//221420649

package za.ac.cput.campusfacilitybooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.TimeSlot;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, String> {
}
