package carpool.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import carpool.data.Prenotazione;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {	
	@Query("SELECT u FROM Prenotazione u WHERE u.ID_user = ?1")
	public List<Prenotazione> findByID_user(Long ID_user);
	
	public Optional<Prenotazione> findById(Long ID_prenotazione);
	
	@Query("SELECT u FROM Prenotazione u WHERE u.ID_viaggio = ?1")
	public List<Prenotazione> findByID_viaggio(Long ID_viaggio);
	
	
}