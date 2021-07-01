package carpool.repos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import carpool.data.Viaggio;

public interface ViaggioRepository extends JpaRepository<Viaggio, Long> {
	
	@Query("SELECT u FROM Viaggio u WHERE u.viaggioId = ?1")
	public Viaggio findByviaggioId(Long viaggioId);
	
	@Modifying
    @Query(
      value = 
        "insert into prenotazioni (user_id, viaggio_id) values (:userId, :viaggioId)",
      nativeQuery = true)
    public void insertPrenotazione(@Param("userId") Long userId, @Param("viaggioId") Long viaggioId);
	
	
	/*
	 * query per ottenere tutti viaggi il cui campo "viaggio_date" è dopo una certa data passata come parametro 
	*/
	
    @Query(
    	value= "select * from viaggio where viaggio.viaggio_date >= :dataRicerca",
    	nativeQuery = true)
    List<Viaggio> findAllWithViaggioDateAfter(
      @Param("dataRicerca") String dataRicerca);

	public ArrayList<Viaggio> findByuserId(Long userId);
	
	
	@Query(
	    	value= "select * from viaggio where viaggio.viaggio_id = (select id_viaggio from prenotazione where id_prenotazione = :id_prenotazione)",
	    	nativeQuery = true)
	Viaggio findViaggioByPrenotazioneId(
	      @Param("id_prenotazione") Long id_prenotazione);
}
