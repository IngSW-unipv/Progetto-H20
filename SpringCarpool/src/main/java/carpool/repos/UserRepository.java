package carpool.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import carpool.data.User;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.email = ?1")
	public User findByEmail(String email);

	//devo trovare autista del viaggio relativo alla prenotazione, specificando suo id utente
	@Query(
	    	value= "SELECT * FROM users WHERE id = (SELECT user_id FROM viaggio WHERE viaggio_id = (SELECT id_viaggio FROM prenotazione WHERE id_prenotazione = :id_prenotazione))",
	    	nativeQuery = true)
	User findByID_prenotazione(@Param("id_prenotazione") Long id_prenotazione);
	
	

}