/* Author: Ayren Villet (223120030)
     Date: 12 July 2026 */
package za.ac.cput.campusfacilitybooking.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, String>{
}

