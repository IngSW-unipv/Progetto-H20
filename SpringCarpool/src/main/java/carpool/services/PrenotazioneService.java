package carpool.services;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import carpool.data.Prenotazione;
import carpool.data.Recensione;
import carpool.data.Viaggio;
import carpool.repos.PrenotazioneRepository;
import carpool.repos.RecensioneRepository;
import carpool.repos.UserRepository;
import carpool.repos.ViaggioRepository;

@Service
public class PrenotazioneService {
	@Autowired
	private RecensioneRepository recensioneRepo;
	
	@Autowired 
	private PrenotazioneRepository prenotazioneRepo;
	
	@Autowired
	private ViaggioRepository viaggioRepo;

	public void cancellaPrenotazioneService(long l) {
		
		Prenotazione prenotazioneCancellata = prenotazioneRepo.getOne(l);
		prenotazioneCancellata.setCancellata(true);
		int postiPrenotati = prenotazioneCancellata.getNumero_posti();
		long bagaglioPrenotato = prenotazioneCancellata.getBagagliaio_prenotato();
		Viaggio viaggioEdit = viaggioRepo.getOne(prenotazioneCancellata.getID_viaggio());
		if (viaggioEdit !=null) {
			viaggioEdit.setPostiPrenotati(viaggioEdit.getPostiPrenotati()-postiPrenotati);
			viaggioEdit.setTotale_bagaglio_prenotato(viaggioEdit.getTotale_bagaglio_prenotato()-bagaglioPrenotato);
		}
		prenotazioneRepo.save(prenotazioneCancellata);
		viaggioRepo.save(viaggioEdit);
		
	}

	public void pagamentoService(long l) {

		Prenotazione prenotazionePagata = prenotazioneRepo.getOne(l);
		prenotazionePagata.setPagamento_effettuato(true);
		prenotazioneRepo.save(prenotazionePagata);
		
	}

	public void recensioneService(Recensione recensione, Prenotazione prenotazione) {
		//Imposto la data in cui la recensione è stato creata
		Date creationDate = new Date();
		recensione.setCreationDate(creationDate);
		recensioneRepo.save(recensione);
	}
}
