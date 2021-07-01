
package carpool.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import carpool.CustomUserDetails;
import carpool.data.Prenotazione;
import carpool.data.Viaggio;
import carpool.repos.PrenotazioneRepository;
import carpool.repos.ViaggioRepository;
import functions.Functions;

@Service

public class ViaggioService{

	@Autowired
	private ViaggioRepository viaggioRepo;

	@Autowired
	private PrenotazioneRepository prenotazioneRepo;


	public void viaggioRegisterService(Viaggio viaggio) {
		//Imposto la data in cui il viaggio è stato creato
		Date creationDate = new Date();
		viaggio.setcreationDate(creationDate);

		//Imposto la data scelta dall'utente in cui ci sarà il viaggio
		LocalDate datePart = LocalDate.parse(viaggio.getDatatemp());
		LocalTime timePart = LocalTime.parse(viaggio.getOratemp());
		LocalDateTime dt = LocalDateTime.of(datePart, timePart);
		Date data_formatted= Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
		viaggio.setViaggioDate(data_formatted);

		//Imposto nel viaggio l'id dell'utente che ha creato il viaggio
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails auth2 = (CustomUserDetails) auth.getPrincipal();
		Long userId = auth2.getId();
		viaggio.setUserId(userId);

		//All'inizio ci sono 0 posti prenotati, lo imposto
		viaggio.setPostiPrenotati(0);
		//Imposto la lunghezza del viaggio
		viaggio.setDistanza(Functions.distance(viaggio.getPartenzaX(), viaggio.getPartenzaY(), viaggio.getDestinazioneX(), viaggio.getDestinazioneY()));
		viaggio.setCancellato(false);
		viaggio.setConcluso(false);
		//Imposto il prezzo totale del viaggio in base alla distanza, da stabilire bene il fattore

		//Salvo il viaggio nel DB
		viaggioRepo.save(viaggio);
		System.out.println("Creato un viaggio con id = " + userId);
		System.out.println("Data di creazione del viaggio = " + creationDate);

	}

	public void viaggioDeleteService(long viaggioId) {

		var prenotazioni = prenotazioneRepo.findByID_viaggio(viaggioId);

		//Poi flaggo ogni prenotazione come cancellata e la salvo nel repo
		for (Prenotazione i : prenotazioni) {
			i.setCancellata(true);
			prenotazioneRepo.save(i);

			//mandare mail per informare della cancellazione?
		}
		//Infine flaggo il viaggio stesso come cancellato e lo salvo nel repo
		Viaggio viaggio = viaggioRepo.findByviaggioId(viaggioId);
		viaggio.setCancellato(true);
		viaggioRepo.save(viaggio);

	}

	public void PrenotazioneViaggioService(Prenotazione prenotazione) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails auth2 = (CustomUserDetails) auth.getPrincipal();
		Long userId = auth2.getId();
		prenotazione.setID_user(userId);

		//Il viaggio che si vuole prenotare esiste?
		Viaggio check = viaggioRepo.findByviaggioId(prenotazione.getID_viaggio());
		System.out.println(prenotazione.getID_viaggio());
		if (check != null) {
			//La prenotazione inserita è su un viaggio creato dall'utente?

			if (userId != check.getUserId() ) {
				//La prenotazione esiste già?
				List<Prenotazione> prenotazioni = prenotazioneRepo.findAll();
				boolean exist = false;
				for (Prenotazione i : prenotazioni) {
					if (i.getID_user() == prenotazione.getID_user() && i.getID_viaggio() == prenotazione.getID_viaggio() ) {
						exist = true;
						System.out.println(exist);
					}
				}	
				//Controllo anche se il viaggio ha ancora posti liberi prenotabili
				if ((!exist) && (check.getPostiPrenotati() + prenotazione.getNumero_posti() <= check.getPostiTotali())) {
					check.setPostiPrenotati(check.getPostiPrenotati() + prenotazione.getNumero_posti());
					//Aggiorno il viaggio con i posti ora non più liberi
					//Aggiungo alla prenotazione la data di creazione
					Date creationDate = new Date();
					prenotazione.setCreation_date(creationDate);
					prenotazione.setCancellata(false);
					prenotazione.setPagamento_effettuato(false);
					prenotazioneRepo.save(prenotazione);
					System.out.println("Viaggio prenotato: " + prenotazione.getID_viaggio());
				} else {
					System.out.println("Esiste già questa prenotazione. Posti totali: " + check.getPostiTotali() + " Posti prenotati: " + check.getPostiPrenotati());  
				}
			}
			else {
				System.out.println("Non puoi prenotare il tuo stesso viaggio ");
			}
		} else {
			System.out.println("Il viaggio prenotato non esiste ");

		}

	}

	public void deleteViaggioService(Viaggio viaggio) {
		ArrayList<Prenotazione> prenotazioni = (ArrayList<Prenotazione>) prenotazioneRepo.findByID_viaggio(viaggio.getViaggioId());
		//Poi flaggo ogni prenotazione come cancellata e la salvo nel repo
		for (Prenotazione i : prenotazioni) {
			i.setCancellata(true);
			prenotazioneRepo.save(i);

			//mandare mail per informare della cancellazione?
		}
		//Infine flaggo il viaggio stesso come cancellato e lo salvo nel repo
		viaggio=viaggioRepo.getOne(viaggio.getViaggioId());
		viaggio.setCancellato(true);
		viaggioRepo.save(viaggio);


	}

	public void terminaViaggioService(Viaggio viaggio) {
		ArrayList<Prenotazione> prenotazioni = (ArrayList<Prenotazione>) prenotazioneRepo.findByID_viaggio(viaggio.getViaggioId());

		//mandare mail per informare della cancellazione?

		//flaggo il viaggio come concluso e lo salvo nel repo
		viaggio=viaggioRepo.getOne(viaggio.getViaggioId());
		viaggio.setConcluso(true);
		viaggioRepo.save(viaggio);

	}
}


