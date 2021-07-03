package carpool.repos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import carpool.data.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
		
	@Modifying
    @Query(
      value = 
        "insert into reservations (user_id, trip_id) values (:userId, :tripId)",
      nativeQuery = true)
    public void insertReservation(@Param("userId") Long userId, @Param("tripId") Long tripId);
	
	
	/*
	 * query per ottenere tutti viaggi il cui campo "viaggio_date" è dopo una certa data passata come parametro 
	*/
	
    @Query(
    	value= "select * from trips where trip_date >= :dataRicerca",
    	nativeQuery = true)
    List<Trip> findAllTripsAfterDate(
    		@Param("dataRicerca") String dataRicerca);

	public ArrayList<Trip> findByuserId(Long userId);
	
	
	@Query(
	    	value= "select * from trips where trip_id = (select trip_id from reservations where reservation_id = :id_prenotazione)",
	    	nativeQuery = true)
	Trip findByReservationId(
	      @Param("id_prenotazione") Long id_prenotazione);
}
