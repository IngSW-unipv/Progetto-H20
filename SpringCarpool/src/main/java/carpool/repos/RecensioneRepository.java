package carpool.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import carpool.data.Recensione;

public interface RecensioneRepository extends JpaRepository<Recensione, Long>{
	
	@Query(
	    	value= "select * from recensione where recensione.id_prenotazione = :id_prenotazione",
	    	nativeQuery = true)
	Recensione findByPrenotazioneId(
	      @Param("id_prenotazione") Long id_prenotazione);
	
}