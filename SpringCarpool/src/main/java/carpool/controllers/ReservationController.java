package carpool.controllers;

import java.util.ArrayList;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import carpool.data.Reservation;
import carpool.data.Review;
import carpool.data.User;
import carpool.data.Trip;
import carpool.repos.ReservationRepository;
import carpool.repos.ReviewRepository;
import carpool.repos.UserRepository;
import carpool.repos.TripRepository;
import carpool.services.ReservationService;

@Controller
public class ReservationController extends ControllerUtility{

	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired 
	private ReservationRepository reservationRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TripRepository tripRepo;
	
	@Autowired
	private ReservationService prenotazioneService;
	
	
	@PostMapping("/cancella_prenotazione")
	public String deleteReservation(Reservation reservation) {
		
		prenotazioneService.deleteReservation(reservation.getReservationId());
		
		System.out.println("cancellata prenotazione: "+reservation.getReservationId());
		return "redirect:/ViaggiPrenotazioni";
	}
	
	@PostMapping("/pagamento_ricevuto")
	public String paymentReceived(Reservation res) {
		
		prenotazioneService.setPaymentReceived(res.getReservationId());
		
		System.out.println("effettuato pagamento: "+res.getReservationId());
		return "redirect:/ViaggiPrenotazioni";
	}
	
	
	@PostMapping("/recensione_prenotazione")
	public String reviewReservation(Review review, Reservation res) {
		
		prenotazioneService.reviewReservation(review, res.getReservationId());
		System.out.println(res.getReservationId());
		return "redirect:/";
	}
	
	
	@GetMapping("/ViaggiPrenotazioni")
	public String showForm(Model model) {
		
		//Prendo l'utente loggato
		User authUser = getUserDetails().getUser();
		
		//qui va controllo che utente è chi è, prendo suo id, lo butto dentro riga dopo per fare in modo da usare solo id di utente loggato
		ArrayList<Trip> viaggi = (ArrayList<Trip>) tripRepo.findByuserId(authUser.getUserId());
		model.addAttribute("byViaggioDate", Comparator.comparing(Trip::getTripDate));
		
		//per ogni viaggio costruisco suoi campi reservations
		for(Trip v: viaggi) {
			v.setReservations((ArrayList<Reservation>)reservationRepo.findByTripId(v.getTripId()));
			for(Reservation p: v.getReservations()) {
				p.setUser(userRepo.findById(p.getUserId()).orElse(new User()));
				if (p.getPaymentMade()) {
					p.setReview(reviewRepo.findByReservationId(p.getReservationId()));
				}
			}
		}
		
		ArrayList<Reservation> prenotazioni_utente_input = (ArrayList<Reservation>) reservationRepo.findByUserId(authUser.getUserId());
		ArrayList<User> autisti_prenotazioni = new ArrayList<User>();
		ArrayList<Trip> viaggi_prenotazioni = new ArrayList<Trip>();
		ArrayList<Review> recensione_temporanea = new ArrayList<Review>();
		for(Reservation p : prenotazioni_utente_input) {
			autisti_prenotazioni.add(userRepo.findByReservationId(p.getReservationId()));
			viaggi_prenotazioni.add(tripRepo.findByReservationId(p.getReservationId()));
			recensione_temporanea.add(reviewRepo.findByReservationId(p.getReservationId()));
		} 
		model.addAttribute("autisti", autisti_prenotazioni);
		model.addAttribute("viaggi_prenotazioni", viaggi_prenotazioni);
		model.addAttribute("prenotazioni_utente_input", prenotazioni_utente_input);
		model.addAttribute("recensioni", recensione_temporanea);
		model.addAttribute("recensione_input", new Review());
		model.addAttribute("prenotazione_input", new Reservation());

		
		
		model.addAttribute("viaggi", viaggi);
		return "ViaggiPrenotazioni";
	}
}
