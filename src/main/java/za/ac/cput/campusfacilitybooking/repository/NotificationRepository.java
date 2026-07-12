package za.ac.cput.campusfacilitybooking.repository;

/* Author: Nuyra Swanson (221290524)
     Date: 12 July 2026 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campusfacilitybooking.domain.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

}
