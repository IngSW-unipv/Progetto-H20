package carpool.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import carpool.data.Reservation;
import carpool.data.Trip;
import carpool.services.TripService;

@Controller
public class TripController extends ControllerUtility{
	
	@Autowired
	private TripService tripService;
	
	@PreAuthorize("hasAnyAuthority('AUTISTA')")
	@PostMapping("/registra-viaggio")
	public String registerTrip(Trip trip) {
		
		tripService.registerTrip(trip, getUserDetails().getId());
		return "redirect:/";
	}
	
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/prenota-viaggio")
	public String bookTrip(Reservation reservation) {
		
		tripService.bookTrip(reservation, getUserDetails().getId());
		
	    return "redirect:/";
	}
	
	@PreAuthorize("hasAnyAuthority('AUTISTA')")
	@PostMapping("/cancella-viaggio")
	public String deleteTrip(Trip trip) {
		
		tripService.deleteTrip(trip);
		
		System.out.println("cancellato viaggio: "+trip.getTripId());
		return "redirect:/ViaggiPrenotazioni";
	}
	
	@PostMapping("/concludi-viaggio")
	public String endTrip(Trip viaggio) {
		
		tripService.endTrip(viaggio);		
				
		System.out.println("concluso viaggio: "+viaggio.getTripId());
		return "redirect:/ViaggiPrenotazioni";
	}
}
