package carpool.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	private TripRepository tripRepo;
	
	@Autowired 
	private ReservationRepository resRepo;
	
	@Autowired 
	private CarRepository carRepo;
	
	@Autowired
	private ReviewRepository reviewRepo;

		
	//Ogni richiesta POST fa prima un GET, e questa richiesta GET nel relativo controller GET
	//Quindi è nel relativo controller GET che bisogna creare gli oggetti come user, viaggio e altri per i relativi form
	//Altrimenti non può funzionare
	@GetMapping("/")
	public String home(@ModelAttribute User user, @ModelAttribute Reservation prenotazione, @ModelAttribute Trip viaggio, Model model) {
		
		//Aggiungo gli oggetti user e coordinate per poter usare la registrazione, creazione viaggi, login...
		model.addAttribute("user", new User());
		model.addAttribute("viaggio", new Trip());
		model.addAttribute("prenotazione", new Reservation());
		model.addAttribute("role", new Role());
		
		//Tutti i viaggi
		model.addAttribute("viaggi", tripRepo.findAll());
		
		/*
		var viaggioOrdered = (List<Trip>) tripRepo.findAll();
		Trip viaggiotest = new Trip ("partenzatest", 1.0, 0.1, "destinazionetest", 2.0, 0.2, 5);
		viaggioOrdered = Functions.setDistances(viaggioOrdered, viaggiotest);
		Collections.sort(viaggioOrdered, new Functions(viaggiotest, viaggioOrdered));
		for (Trip i : viaggioOrdered) {
			System.out.println("Partenza: " + i.getStart() + " Distanza dal viaggio di test: " + i.getDistance());
		}
        model.addAttribute("viaggioOrdered", viaggioOrdered);
        */
        
		//Aggiungo al model l'utente loggato e le sue prenotazioni
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

	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@GetMapping("/Profilo")
	public String profiloPage(Model model) {
		
		//elementi che servono per popolare la pagina di Profilo.
		User authUser = getUserDetails().getUser();
		model.addAttribute("CurrentUser", authUser);
		
		ArrayList<Car> carsAuthUser = (ArrayList<Car>) carRepo.findAllByUserId(authUser.getUserId());
		model.addAttribute("NewCar", new Car());
		model.addAttribute("CurrentCars", carsAuthUser);
		
		String newPassword ="you wish :^)";
		model.addAttribute("newPassword", newPassword);
		
		return "Profilo";
	}
	
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@GetMapping("/ViaggiPrenotazioni")
	public String viaggiprenotazioniPage(Model model) {
		
		//Prendo l'utente loggato
		User authUser = getUserDetails().getUser();
		
		//qui va controllo che utente è chi è, prendo suo id, lo butto dentro riga dopo per fare in modo da usare solo id di utente loggato
		ArrayList<Trip> viaggi = (ArrayList<Trip>) tripRepo.findByuserId(authUser.getUserId());
		model.addAttribute("byViaggioDate", Comparator.comparing(Trip::getTripDate));
		
		//per ogni viaggio costruisco suoi campi reservations
		for(Trip v: viaggi) {
			v.setReservations((ArrayList<Reservation>)resRepo.findByTripId(v.getTripId()));
			for(Reservation p: v.getReservations()) {
				p.setUser(userRepo.findById(p.getUserId()).orElse(new User()));
				if (p.getPaymentMade()) {
					p.setReview(reviewRepo.findByReservationId(p.getReservationId()));
				}
			}
		}
		
		ArrayList<Reservation> prenotazioni_utente_input = (ArrayList<Reservation>) resRepo.findByUserId(authUser.getUserId());
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
	

	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@GetMapping("/contacts")
	public String contacts() {
		return "contacts";
	}
	
	//Questo metodo serve per mostrare i viaggi sulla mappa nella home
		@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
		@RequestMapping(value="/mappa_prova", produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public List<InfoViaggio> mostraviaggimappa(@RequestParam Map<String, String> body) {

			//Coordinate di partenza e destinazione e data del viaggio in questione
		 	double partenzaTmpCoordX = 0; 
		 	double partenzaTmpCoordY = 0;
		 	double destinazioneTmpCoordX = 0; 
		 	double destinazioneTmpCoordY = 0;
		 	String dataRicerca = null;
		 	
		 	//Smonto l'oggetto in arrivo nei cinque attributi
		 	for (Map.Entry<String, String> entry : body.entrySet()) {
		 		
		 		if(entry.getKey().equals("partenza_lat"))
		 			partenzaTmpCoordX= Double.parseDouble(entry.getValue().toString());
		 		if(entry.getKey().equals("partenza_lon"))
		 			partenzaTmpCoordY= Double.parseDouble(entry.getValue().toString());
		 		if(entry.getKey().equals("destinazione_lat"))
		 			destinazioneTmpCoordX= Double.parseDouble(entry.getValue().toString());
		 		if(entry.getKey().equals("destinazione_lon"))
		 			destinazioneTmpCoordY= Double.parseDouble(entry.getValue().toString());
		 		if(entry.getKey().equals("data_ricerca"))
		 			dataRicerca = entry.getValue().toString();
			}
		 	
		 	//Prendo tutti i viaggi dopo la data specificata nella richiesta
		 	List<Trip> tripAfterDate = tripRepo.findAllTripsAfterDate(dataRicerca);
		 	
		 	//Serve per organizzare i viaggi in senso crescente di distanza dalle coordinate di partenza e destinazione
			Trip viaggiotest = new Trip ("partenzatest", partenzaTmpCoordX, partenzaTmpCoordY, "destinazionetest", destinazioneTmpCoordX, destinazioneTmpCoordY, 5);
			//Per ogni viaggio, imposto temporaneamente la sua distanza dal viaggio in questione
			List<Trip> orderedTrips = Functions.setDistances(tripAfterDate, viaggiotest);
			//Riordino tutti i viaggi in base alla distanza dal viaggio in questione
			Collections.sort(orderedTrips, new Functions(viaggiotest, orderedTrips));
			
			//Inizio a creare una lista di tipo InfoViaggio che contiene info su utente e auto
			List<InfoViaggio> infoViaggi = new ArrayList<InfoViaggio>();
			int media = 0;
			int contatore = 0;
			
			//Per ogni viaggio prendo il suo utente e l'auto usata per quel viaggio
			for (Trip t : orderedTrips) {
				//Prendo un viaggio solo se non è distante più di tot km dal viaggio cercato
				//Così evito di mostrare viaggi dall'altro capo del mondo se sono gli unici disponibili
				if (t.getDistanceKm() < 500) {
					User u = userRepo.getOne(t.getUserId());
					Car a = carRepo.getOne(t.getCarId());
					
					ArrayList<Trip> driverTrips = (ArrayList<Trip>) tripRepo.findByuserId(t.getUserId());
					for(Trip dt: driverTrips) {
						ArrayList<Reservation> ps = (ArrayList<Reservation>) resRepo.findByTripId(dt.getTripId());
						//Per ogni recensione di ogni viaggio di quell'utente prendo il voto della recensione
						for(Reservation p : ps) {
							Review rec = reviewRepo.findByReservationId(p.getReservationId());
							if(rec != null) {
								System.out.println(rec.getReservationId());
								media += rec.getScore();
								contatore += 1;
							}
						}
						
					}
					//Media fatta dai voti delle recensioni per questo
					if(contatore > 0) {
						media = media/contatore;
					}
					//Metto l'oggetto così creato nella lista di tipo InfoViaggio
					infoViaggi.add(new InfoViaggio(t, u, a, media));
					//Vado avanti finchè ho 10 viaggi
					if (infoViaggi.size() >= 10)
						break;
				}
			}
			
		   	return infoViaggi;
		}
	
}
