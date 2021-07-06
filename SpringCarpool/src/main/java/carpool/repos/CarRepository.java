package carpool.repos;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import carpool.data.Car;

public interface CarRepository extends JpaRepository<Car, Long>{
	@Query(
	    	value= "select * from cars where cars.user_id = :userId",
	    	nativeQuery = true)
	ArrayList<Car> findAllByUserId(
	      @Param("userId") Long userId);
	
	@Query(
	    	value= "select * from cars where cars.user_id = :userId and cars.default_car=true",
	    	nativeQuery = true)
	Car findDefaultByUserId(
	      @Param("userId") Long userId);
	
}
