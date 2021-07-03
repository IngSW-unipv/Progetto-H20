package carpool.controllers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import carpool.CustomUserDetails;
import carpool.data.Car;
import carpool.data.Reservation;
import carpool.data.Review;
import carpool.data.Role;
import carpool.data.User;
import carpool.data.Trip;
import carpool.infos.InfoViaggio;
import carpool.repos.CarRepository;
import carpool.repos.ReservationRepository;
import carpool.repos.ReviewRepository;
import carpool.repos.UserRepository;
import carpool.repos.TripRepository;
import functions.Functions;


@Controller
public class GeneralController extends ControllerUtility{
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TripRepository triprepo;
	
	@Autowired 
	private ReservationRepository resRepo;
	
	@Autowired 
	private CarRepository carRepo;
	
	@Autowired
	private ReviewRepository reviewRepo;

		
	//Ogni richiesta POST fa prima un GET, e questa richiesta GET finisce qua
	//Quindi è qua che bisogna creare gli oggetti user e coordinate per i relativi form
	@GetMapping("/")
	public String home(@ModelAttribute User user, @ModelAttribute Reservation prenotazione, @ModelAttribute Trip viaggio, Model model) {
		
		//Aggiungo gli oggetti user e coordinate per poter usare la registrazione, creazione viaggi, login...
		model.addAttribute("user", new User());
		model.addAttribute("viaggio", new Trip());
		model.addAttribute("prenotazione", new Reservation());
		model.addAttribute("role", new Role());
		
		//Curioso fenomeno, basta usare la variabile di tipo CoordinateRepository invece di new CoordinateService().findAll()
		//Anche se quel metodo findAll() comunque usa un CoordinateRepository, eppure restituisce null.

		model.addAttribute("viaggi", triprepo.findAll());
		
		
		var viaggioOrdered = (List<Trip>) triprepo.findAll();
		Trip viaggiotest = new Trip ("partenzatest", 1.0, 0.1, "destinazionetest", 2.0, 0.2, 5);
		viaggioOrdered = Functions.setDistances(viaggioOrdered, viaggiotest);
		Collections.sort(viaggioOrdered, new Functions(viaggiotest, viaggioOrdered));
		for (Trip i : viaggioOrdered) {
			System.out.println("Partenza: " + i.getStart() + " Distanza dal viaggio di test: " + i.getDistance());
		}
        model.addAttribute("viaggioOrdered", viaggioOrdered);
        
        
        var prenotazioni = (List<Reservation>) resRepo.findAll();
        List<Reservation> userPrenotazioni = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
        	CustomUserDetails user1 = (CustomUserDetails) auth.getPrincipal();
        	model.addAttribute("user1", user1.getUser());
        		for (Reservation i : prenotazioni) {
        			if (i.getUserId() == user1.getId()) {
        				userPrenotazioni.add(i);
        				System.out.println("Prenotazione fatta dall'utente: " + i.getUserId() + " sul viaggio: " + i.getTripId());
        			}
        		}
        }
        model.addAttribute("prenotazioni", prenotazioni);
		return "index";
	}
	
	//Questa funzione viene chiamata direttamente da Spring Security in caso di login fallito
	//Avendo un redirect, bisogna usare RedirectAttributes e non Model, se no si perdono i dati da restituire
	@GetMapping("/login-error")
	public String login(HttpServletRequest request, RedirectAttributes redirAttr) {
		HttpSession session = request.getSession(false);
		String errorMessage = null;
		if (session != null) {
			AuthenticationException ex = (AuthenticationException) session
					.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			if (ex != null) {
				//errorMessage = ex.getMessage();
				errorMessage = "Password o Email errati!";
				}
			}
		redirAttr.addFlashAttribute("errorMessage", errorMessage);
		return "redirect:/";
		}
	
	//Questo metodo serve per mostrare i viaggi sulla mappa nella home
	@RequestMapping(value="/mappa_prova", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<InfoViaggio> mostraviaggimappa(@RequestParam Map<String, String> body) {
	 	List<Trip> listaPercorsi;
	 	double partenzaTmpCoordX = 0; 
	 	double partenzaTmpCoordY = 0;
	 	double destinazioneTmpCoordX = 0; 
	 	double destinazioneTmpCoordY = 0;
	 	String dataRicerca = null;
	 	
	 	for (Map.Entry<String, String> entry : body.entrySet()) {
	 		
	 		if(entry.getKey().equals("partenza_lat"))
	 			partenzaTmpCoordX= Double.parseDouble(entry.getValue().toString());
	 		if(entry.getKey().equals("partenza_lon"))
	 			partenzaTmpCoordX= Double.parseDouble(entry.getValue().toString());
	 		if(entry.getKey().equals("destinazione_lat"))
	 			destinazioneTmpCoordX= Double.parseDouble(entry.getValue().toString());
	 		if(entry.getKey().equals("destinazione_lon"))
	 			destinazioneTmpCoordY= Double.parseDouble(entry.getValue().toString());
	 		if(entry.getKey().equals("data_ricerca"))
	 			dataRicerca = entry.getValue().toString();
		}
	 	
	 	// serve per filtrare i viaggi ricevuti in base a data di partenza specificata da utente
	 	List<Trip> tripAfterDate = triprepo.findAllTripsAfterDate(dataRicerca);
	 	
	 	// serve per organizzare i viaggi in senso crescente di distanza da coordinate di partenza e end
		Trip viaggiotest = new Trip ("partenzatest", partenzaTmpCoordX, partenzaTmpCoordY, "destinazionetest", destinazioneTmpCoordX, destinazioneTmpCoordY, 5);
		List<Trip> orderedTrips = Functions.setDistances(tripAfterDate, viaggiotest);
		Collections.sort(orderedTrips, new Functions(viaggiotest, orderedTrips));
		List<InfoViaggio> infoViaggi = new ArrayList<InfoViaggio>();
		int media=0;
		int contatore=0;
		for (Trip t : orderedTrips) {
			User u = userRepo.getOne(t.getUserId());
			Car a = carRepo.getOne(t.getCarId());
			
			ArrayList<Trip> driverTrips= (ArrayList<Trip>) triprepo.findByuserId(t.getUserId());
			for(Trip dt: driverTrips) {
				ArrayList<Reservation> ps = (ArrayList<Reservation>) resRepo.findByTripId(dt.getTripId());
				for(Reservation p : ps) {
					Review rec = reviewRepo.findByReservationId(p.getReservationId());
					if(rec != null) {
						System.out.println(rec.getReservationId());
						media+=rec.getScore();
						contatore+=1;
					}
				}
				
			}
			if(contatore>0) {
				media=media/contatore;
			}
			
			infoViaggi.add(new InfoViaggio(t, u, a, media));
			//per limitare numero di viaggi restituiti a 10
			if(infoViaggi.size()>=10)
				break;
		}
		
	   	return infoViaggi;
	}
	

	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@GetMapping("/pageProfilo")
	public String pageProfilo(HttpServletRequest request, RedirectAttributes redirAttr) {
		String page = "pageProfilo";
		redirAttr.addFlashAttribute("page", page);
		return "redirect:/";
		}
	

	
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@GetMapping("/contacts")
	public String contacts() {
		return "contacts";
		}
	
}
