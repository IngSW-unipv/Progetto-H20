package carpool.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import carpool.data.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	
	@Query(
	    	value= "select * from reviews where reviews.reservation_id = :reservation_id",
	    	nativeQuery = true)
	Review findByReservationId(
	      @Param("reservation_id") Long reservation_id);
	
}