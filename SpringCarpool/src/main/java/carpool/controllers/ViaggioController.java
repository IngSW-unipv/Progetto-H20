package carpool.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import carpool.CustomUserDetails;
import carpool.data.Prenotazione;
import carpool.data.Viaggio;
import carpool.repos.PrenotazioneRepository;
import carpool.repos.ViaggioRepository;
import carpool.services.ViaggioService;
import functions.Functions;

@Controller
public class ViaggioController {
	
	@Autowired
	private ViaggioService viaggioService;
	
	@PreAuthorize("hasAnyAuthority('AUTISTA')")
	@PostMapping("/viaggio_register")
	public String viaggioregister(Viaggio viaggio) { 
		viaggioService.viaggioRegisterService(viaggio);
		String pagePrenota = "pagePrenota";
		return "redirect:/";
	}
	
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/viaggio_prenotazione")
	public String viaggioprenotazione(Prenotazione prenotazione) {
		
		viaggioService.PrenotazioneViaggioService(prenotazione);
		
	    return "redirect:/";
	}
	
	@PreAuthorize("hasAnyAuthority('AUTISTA')")
	@PostMapping("/cancella_viaggio")
	public String cancellazione_viaggio(Viaggio viaggio) {
		
		viaggioService.deleteViaggioService(viaggio);
		
		System.out.println("cancellato viaggio: "+viaggio.getViaggioId());
		return "redirect:/ViaggiPrenotazioni";
	}
	
	@PostMapping("/concludi_viaggio")
	public String conclusione_viaggio(Viaggio viaggio) {
		
		viaggioService.terminaViaggioService(viaggio);		
		
		
		System.out.println("concluso viaggio: "+viaggio.getViaggioId());
		return "redirect:/ViaggiPrenotazioni";
	}
}
