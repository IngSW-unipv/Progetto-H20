package carpool.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import carpool.data.Reservation;
import carpool.data.Review;
import carpool.data.User;
import carpool.data.Trip;
import carpool.services.ReservationService;

@Controller
public class ReservationController extends ControllerUtility{
	
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
		return "redirect:/ViaggiPrenotazioni";
	}
}
