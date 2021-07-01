package carpool.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import carpool.data.Automobile;

public interface AutomobileRepository extends JpaRepository<Automobile, Long>{

}
