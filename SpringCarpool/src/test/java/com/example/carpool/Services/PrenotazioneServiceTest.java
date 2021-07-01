/**
 * 
 */
package com.example.carpool.Services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import carpool.data.Prenotazione;
import carpool.data.Recensione;
import carpool.repos.PrenotazioneRepository;
import carpool.repos.RecensioneRepository;
import carpool.repos.ViaggioRepository;
import carpool.services.PrenotazioneService;

/**
 * @author Sviatoslav Pilyanskij
 */

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class PrenotazioneServiceTest {
	
	@MockBean
	private RecensioneRepository MockRecensioneRepo;
	
	@MockBean 
	private PrenotazioneRepository MockPrenotazioneRepo;
	
	//sarà usato per "fingersi" un oggeto di prenotazione
	@MockBean
	private Prenotazione MockPrenotazione; 
	
	@MockBean
	private ViaggioRepository MockViaggioRepo;
	
	@InjectMocks
	private PrenotazioneService MockPrenotaizoneService;
	
	private Long MockIdPrenotazione=(long) 1;
	
	/**
	 * Test method for {@link carpool.services.PrenotazioneService#cancellaPrenotazioneService(carpool.data.Prenotazione)}.
	 */
	@Test
	@DisplayName("test della cancellazione della Prenotazione")
	void testCancellaPrenotazioneService() {
		
		Prenotazione prenotazione = new Prenotazione();
		//ora in mockprenotazione si trova un oggetto "come se" fosse pescato da prenotazionerepo con un certo id
		Mockito.when(MockPrenotazioneRepo.getOne(MockIdPrenotazione)).thenReturn(prenotazione);
		MockPrenotaizoneService.cancellaPrenotazioneService(MockIdPrenotazione);
		assertTrue(prenotazione.getCancellata()==true);
	}

	/**
	 * Test method for {@link carpool.services.PrenotazioneService#pagamentoService(carpool.data.Prenotazione)}.
	 */
	@Test
	@DisplayName("test per il pagamento di una Prenotazione conclusa")
	void testPagamentoService() {
		Prenotazione prenotazione = new Prenotazione();
		Mockito.when(MockPrenotazioneRepo.getOne(MockIdPrenotazione)).thenReturn(prenotazione);
		MockPrenotaizoneService.pagamentoService(MockIdPrenotazione);
		assertTrue(prenotazione.getPagamento_effettuato()==true);
	}

	/**
	 * Test method for {@link carpool.services.PrenotazioneService#recensioneService(carpool.data.Recensione, carpool.data.Prenotazione)}.
	 */
	@Test
	@DisplayName("test per lo store della Recensione")
	void testRecensioneService() {
		Prenotazione prenotazione = new Prenotazione();
		Recensione recensione = new Recensione();
		Mockito.when(MockPrenotazioneRepo.getOne(MockIdPrenotazione)).thenReturn(prenotazione);
		MockPrenotaizoneService.recensioneService(recensione, prenotazione);
		assertTrue(recensione.getCreationDate().before(new Date())==true);
	}

}
