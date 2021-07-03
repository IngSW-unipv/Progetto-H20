/**
 * 
 */
package carpool.Services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import carpool.data.Reservation;
import carpool.data.Review;
import carpool.repos.ReservationRepository;
import carpool.repos.ReviewRepository;
import carpool.repos.TripRepository;
import carpool.services.ReservationService;

/**
 * @author Sviatoslav Pilyanskij
 */

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ReservationServiceTest {
	
	@MockBean
	private ReviewRepository MockRecensioneRepo;
	
	@MockBean 
	private ReservationRepository MockPrenotazioneRepo;
	
	//sarà usato per "fingersi" un oggeto di prenotazione
	@MockBean
	private Reservation MockPrenotazione; 
	
	@MockBean
	private TripRepository MockViaggioRepo;
	
	@InjectMocks
	private ReservationService MockPrenotaizoneService;
	
	private Long MockIdPrenotazione=(long) 12;
	
	/**
	 * Test method for {@link carpool.services.ReservationService#deleteReservation(carpool.data.Reservation)}.
	 */
	@Test
	@DisplayName("test della cancellazione della Prenotazione")
	void testCancellaPrenotazioneService() {
		
		Reservation prenotazione = new Reservation();
		//ora in mockprenotazione si trova un oggetto "come se" fosse pescato da prenotazionerepo con un certo id
		Mockito.when(MockPrenotazioneRepo.findById(MockIdPrenotazione)).thenReturn(Optional.of(prenotazione));
		MockPrenotaizoneService.deleteReservation(MockIdPrenotazione);
		assertTrue(prenotazione.getDeleted()==true);
	}

	/**
	 * Test method for {@link carpool.services.ReservationService#setPaymentReceived(carpool.data.Reservation)}.
	 */
	@Test
	@DisplayName("test per il pagamento di una Prenotazione conclusa")
	void testPagamentoService() {
		Reservation prenotazione = new Reservation();
		Mockito.when(MockPrenotazioneRepo.findById(MockIdPrenotazione)).thenReturn(Optional.of(prenotazione));
		MockPrenotaizoneService.setPaymentReceived(MockIdPrenotazione);
		assertTrue(prenotazione.getPaymentMade()==true);
	}

	/**
	 * Test method for {@link carpool.services.ReservationService#reviewReservation(carpool.data.Review, carpool.data.Reservation)}.
	 */
	@Test
	@DisplayName("test della data di creazione di una recensione")
	void testRecensioneService() {
		Reservation prenotazione = new Reservation();
		Review recensione = new Review();
		Mockito.when(MockPrenotazioneRepo.findById(MockIdPrenotazione)).thenReturn(Optional.of(prenotazione));
		MockPrenotaizoneService.reviewReservation(recensione, MockIdPrenotazione);
		assertTrue((recensione.getCreationDate().before(new Date()) || recensione.getCreationDate().equals(new Date()))==true);
	}

}
