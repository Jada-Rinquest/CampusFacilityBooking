//Jada Rinquest 12/07/2026
//222871296

package za.ac.cput.campusfacilitybooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
}