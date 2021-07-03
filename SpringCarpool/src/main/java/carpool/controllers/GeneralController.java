package carpool.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import carpool.CustomUserDetails;
import carpool.data.Automobile;
import carpool.data.Prenotazione;
import carpool.data.Recensione;
import carpool.data.Role;
import carpool.data.User;
import carpool.data.Viaggio;
import carpool.infos.InfoViaggio;
import carpool.repos.AutomobileRepository;
import carpool.repos.PrenotazioneRepository;
import carpool.repos.RecensioneRepository;
import carpool.repos.RoleRepository;
import carpool.repos.UserRepository;
import carpool.repos.ViaggioRepository;
//import carpool.services.iViaggioService;

import java.util.Comparator;

import functions.FileUploadUtil;
import functions.Functions;


@Controller
public class GeneralController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ViaggioRepository viaggioRepo;
	
	@Autowired 
	private PrenotazioneRepository prenotazioneRepo;
	
	@Autowired 
	private AutomobileRepository automobileRepo;
	
	@Autowired
	private RecensioneRepository recensioneRepo;

		
	//Ogni richiesta POST fa prima un GET, e questa richiesta GET finisce qua
	//Quindi è qua che bisogna creare gli oggetti user e coordinate per i relativi form
	@GetMapping("/")
	public String generalController(@ModelAttribute User user, @ModelAttribute Prenotazione prenotazione, @ModelAttribute Viaggio viaggio, Model model) {
		
		//Aggiungo gli oggetti user e coordinate per poter usare la registrazione, creazione viaggi, login...
		model.addAttribute("user", new User());
		model.addAttribute("viaggio", new Viaggio());
		model.addAttribute("prenotazione", new Prenotazione());
		model.addAttribute("role", new Role());
		
		//Curioso fenomeno, basta usare la variabile di tipo CoordinateRepository invece di new CoordinateService().findAll()
		//Anche se quel metodo findAll() comunque usa un CoordinateRepository, eppure restituisce null.

		model.addAttribute("viaggi", viaggioRepo.findAll());
		
		
		var viaggioOrdered = (List<Viaggio>) viaggioRepo.findAll();
		Viaggio viaggiotest = new Viaggio ("partenzatest", 1.0, 0.1, "destinazionetest", 2.0, 0.2, 5);
		viaggioOrdered = Functions.setDistances(viaggioOrdered, viaggiotest);
		Collections.sort(viaggioOrdered, new Functions(viaggiotest, viaggioOrdered));
		for (Viaggio i : viaggioOrdered) {
			System.out.println("Partenza: " + i.getPartenza() + " Distanza dal viaggio di test: " + i.getDistanza());
		}
        model.addAttribute("viaggioOrdered", viaggioOrdered);
        
        
        var prenotazioni = (List<Prenotazione>) prenotazioneRepo.findAll();
        List<Prenotazione> userPrenotazioni = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
        	CustomUserDetails user1 = (CustomUserDetails) auth.getPrincipal();
        	model.addAttribute("user1", user1.getUser());
        		for (Prenotazione i : prenotazioni) {
        			if (i.getID_user() == user1.getId()) {
        				userPrenotazioni.add(i);
        				System.out.println("Prenotazione fatta dall'utente: " + i.getID_user() + " sul viaggio: " + i.getID_viaggio());
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
	 	List<Viaggio> listaPercorsi;
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
	 	List<Viaggio> viaggiFiltratiPerData = viaggioRepo.findAllWithViaggioDateAfter(dataRicerca);
	 	
	 	// serve per organizzare i viaggi in senso crescente di distanza da coordinate di partenza e destinazione
		Viaggio viaggiotest = new Viaggio ("partenzatest", partenzaTmpCoordX, partenzaTmpCoordY, "destinazionetest", destinazioneTmpCoordX, destinazioneTmpCoordY, 5);
		List<Viaggio> viaggioOrdered = Functions.setDistances(viaggiFiltratiPerData, viaggiotest);
		Collections.sort(viaggioOrdered, new Functions(viaggiotest, viaggioOrdered));
		List<InfoViaggio> infoViaggi = new ArrayList();
		int media=0;
		int contatore=0;
		for (Viaggio v : viaggioOrdered) {
			User u = userRepo.getOne(v.getUserId());
			Automobile a = automobileRepo.getOne(v.getId_automobile());
			
			ArrayList<Viaggio> viaggi_autista= (ArrayList<Viaggio>) viaggioRepo.findByuserId(v.getUserId());
			for(Viaggio va: viaggi_autista) {
				ArrayList<Prenotazione> ps = (ArrayList<Prenotazione>) prenotazioneRepo.findByID_viaggio(va.getViaggioId());
				for(Prenotazione p : ps) {
					Recensione rec = recensioneRepo.findByPrenotazioneId(p.getID_prenotazione());
					if(rec != null) {
						System.out.println(rec.getID_prenotazione());
						media+=rec.getScore();
						contatore+=1;
					}
				}
				
			}
			if(contatore>0) {
				media=media/contatore;
			}
			
			infoViaggi.add(new InfoViaggio(v, u, a, media));
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
