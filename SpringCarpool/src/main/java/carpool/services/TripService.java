
package carpool.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import carpool.data.Reservation;
import carpool.data.Trip;
import carpool.repos.ReservationRepository;
import carpool.repos.TripRepository;
import functions.Functions;

@Service
public class TripService{

	@Autowired
	private TripRepository tripRepo;

	@Autowired
	private ReservationRepository reservationRepo;


	public void registerTrip(Trip trip, Long userId) {
		//Imposto la data in cui il viaggio è stato creato
		Date creationDate = new Date();
		trip.setCreationDate(creationDate);

		//Imposto la data scelta dall'utente in cui ci sarà il viaggio
		LocalDate datePart = LocalDate.parse(trip.getDateString());
		LocalTime timePart = LocalTime.parse(trip.getTimeString());
		LocalDateTime dt = LocalDateTime.of(datePart, timePart);
		Date data_formatted= Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
		trip.setTripDate(data_formatted);

		//Imposto nel viaggio l'id dell'utente che ha creato il viaggio
		trip.setUserId(userId);

		//All'inizio ci sono 0 posti prenotati, lo imposto
		trip.setReservedSeats(0);
		//Imposto la lunghezza del viaggio
		trip.setDistance(Functions.distance(trip.getStartX(), trip.getStartY(), trip.getEndX(), trip.getEndY()));
		trip.setDeleted(false);
		trip.setEnded(false);
		//Imposto il prezzo totale del viaggio in base alla distanza, da stabilire bene il fattore

		//Salvo il viaggio nel DB
		tripRepo.save(trip);
		System.out.println("Creato un viaggio con id = " + userId);
		System.out.println("Data di creazione del viaggio = " + creationDate);

	}

	public boolean bookTrip(Reservation res, Long userId) {
		res.setUserId(userId);

		//Il viaggio che si vuole prenotare esiste?
		var check = tripRepo.findById(res.getTripId());
		System.out.println(res.getTripId());
		if (!check.isPresent()) {
			System.out.println("Il viaggio prenotato non esiste ");
			return false;
		}
		
		//La prenotazione inserita è su un viaggio creato dal guidatore?
		Trip trip = check.get();
		if (userId == trip.getUserId() ) {
			System.out.println("Non puoi prenotare il tuo stesso viaggio "); 
			return false;
		}
		
		//La prenotazione esiste già?
		//cerca tutte le prenotazioni per questo viaggio
		List<Reservation> reservations = reservationRepo.findByTripId(res.getTripId());
		//controllo se non c'è una prenotazione da parte di uno stesso utente e non cancellata
		for (Reservation p : reservations) {
			if (p.getUserId() == userId && !p.getDeleted()) {
				
				System.out.println("Esiste già questa prenotazione. Posti totali: " + 
				trip.getTotalSeats() + " Posti prenotati: " + trip.getReservedSeats());
				return false;
			}
		}	
		//Aggiorno il viaggio se ci sono posti disponibili
		if (trip.addReservedSeatsIfAvailable(res.getReservedSeats())) {
			//Aggiungo alla prenotazione la data di creazione
			Date creationDate = new Date();
			res.setCreationDate(creationDate);
			res.setDeleted(false);
			res.setPaymentMade(false);
			reservationRepo.save(res);
			System.out.println("Viaggio prenotato: " + res.getTripId());
			return true;
		} 
		return false;
	}

	public void deleteTrip(Trip trip) {
		ArrayList<Reservation> prenotazioni = (ArrayList<Reservation>) reservationRepo.findByTripId(trip.getTripId());
		//Poi flaggo ogni prenotazione come cancellata e la salvo nel repo
		for (Reservation i : prenotazioni) {
			i.setDeleted(true);
			reservationRepo.save(i);

			//mandare mail per informare della cancellazione?
		}
		//Infine flaggo il viaggio stesso come cancellato e lo salvo nel repo
		var check = tripRepo.findById(trip.getTripId());
		if (check.isPresent()) {
		Trip foundTrip = check.get();
		foundTrip.setDeleted(true);
		tripRepo.save(foundTrip);
		}
	}
	
	public void deleteTripById(long tripId) {

		var prenotazioni = reservationRepo.findByTripId(tripId);

		//Poi flaggo ogni prenotazione come cancellata e la salvo nel repo
		for (Reservation i : prenotazioni) {
			i.setDeleted(true);
			reservationRepo.save(i);

			//mandare mail per informare della cancellazione?
		}
		//Infine flaggo il viaggio stesso come cancellato e lo salvo nel repo
		var check = tripRepo.findById(tripId);
		if(check.isPresent())
		{
			Trip trip = check.get();
			trip.setDeleted(true);
			tripRepo.save(trip);
		}
	}

	public void endTrip(Trip viaggio) {
		//mandare mail per informare della cancellazione?
		
		//flaggo il viaggio come concluso e lo salvo nel repo
		var check = tripRepo.findById(viaggio.getTripId());
		if(check.isPresent())
		{
			Trip trip = check.get();
			viaggio.setEnded(true);
			tripRepo.save(trip);
		}	
	}
	
	public void deleteTripsBeforeDate(Date date)
	{
		//Prendo tutti i viaggi dal db, magari poi si può fare una query per il db per pigliare solo quelli con una certa data
		var trips = tripRepo.findAll();
		for (Trip t : trips) {
			//Prima controllo se il viaggio non è già concluso o cancellato
			if (t.getEnded() == false || t.getDeleted() == false) {
				//Se la data del viaggio è prima di quella di oggi, allora è scaduto
				//Lo imposto come concluso e lo salvo nel DB
				if (t.getTripDate().compareTo(date) < 0) {
					System.out.println("Scheduled: impostato come Concluso il viaggio con id: " + t.getTripId());
					t.setEnded(true);
					tripRepo.save(t);
				}
			}
		}
	}
}


