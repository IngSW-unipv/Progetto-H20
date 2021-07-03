package carpool.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import carpool.data.Car;

public interface CarRepository extends JpaRepository<Car, Long>{

}
