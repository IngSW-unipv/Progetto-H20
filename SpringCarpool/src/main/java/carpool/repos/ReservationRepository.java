package carpool.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import carpool.data.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {	
	@Query("SELECT u FROM Reservation u WHERE u.userId = ?1")
	public List<Reservation> findByUserId(Long userId);
		
	@Query("SELECT u FROM Reservation u WHERE u.tripId = ?1")
	public List<Reservation> findByTripId(Long tripId);

}