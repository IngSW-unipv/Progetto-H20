package carpool.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import carpool.CustomUserDetails;
import carpool.data.Prenotazione;
import carpool.data.Recensione;
import carpool.data.User;
import carpool.data.Viaggio;
import carpool.repos.PrenotazioneRepository;
import carpool.repos.RecensioneRepository;
import carpool.repos.UserRepository;
import carpool.repos.ViaggioRepository;
import carpool.services.PrenotazioneService;

@Controller
public class PrenotazioneController {

	@Autowired
	private RecensioneRepository recensioneRepo;
	
	@Autowired 
	private PrenotazioneRepository prenotazioneRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ViaggioRepository viaggioRepo;
	
	@Autowired
	private PrenotazioneService prenotazioneService;
	
	
	@PostMapping("/cancella_prenotazione")
	public String cancellazione_prenotazione(Prenotazione prenotazione) {
		
		prenotazioneService.cancellaPrenotazioneService(prenotazione.getID_prenotazione());
		
		System.out.println("cancellata prenotazione: "+prenotazione.getID_prenotazione());
		return "redirect:/ViaggiPrenotazioni";
	}
	
	@PostMapping("/pagamento_ricevuto")
	public String ricevimento_pagamento(Prenotazione prenotazione) {
		
		prenotazioneService.pagamentoService(prenotazione.getID_prenotazione());
		
		System.out.println("effettuato pagamento: "+prenotazione.getID_prenotazione());
		return "redirect:/ViaggiPrenotazioni";
	}
	
	
	@PostMapping("/recensione_prenotazione")
	public String recensioneprenotazione (Recensione recensione, Prenotazione prenotazione) {
		
		prenotazioneService.recensioneService(recensione, prenotazione);
		
		System.out.println(prenotazione.getID_prenotazione());
		return "redirect:/";
	}
	
	
	@GetMapping("/ViaggiPrenotazioni")
	public String showForm(Model model) {
		
		//Prendo l'utente loggato
				CustomUserDetails user1 = (CustomUserDetails) SecurityContextHolder.getContext()
			            .getAuthentication()
			            .getPrincipal();
				User utente_autenticato = user1.getUser();
		
		//qui va controllo che utente è chi è, prendo suo id, lo butto dentro riga dopo per fare in modo da usare solo id di utente loggato
		ArrayList<Viaggio> viaggi = (ArrayList<Viaggio>) viaggioRepo.findByuserId(utente_autenticato.getID_user());
		model.addAttribute("byViaggioDate", Comparator.comparing(Viaggio::getViaggioDate));
		
		//per ogni viaggio costruisco suoi campi prenotazioni_associate
		for(Viaggio v: viaggi) {
			v.setPrenotazioni_associate((ArrayList<Prenotazione>)prenotazioneRepo.findByID_viaggio(v.getViaggioId()));
			for(Prenotazione p: v.getPrenotazioni_associate()) {
				p.setUsertemp(userRepo.findById(p.getID_user()).orElse(new User()));
				if (p.getPagamento_effettuato()) {
					p.setTmprecensione(recensioneRepo.findByPrenotazioneId(p.getID_prenotazione()));
				}
			}
		}
		
		ArrayList<Prenotazione> prenotazioni_utente_input = (ArrayList<Prenotazione>) prenotazioneRepo.findByID_user(utente_autenticato.getID_user());
		ArrayList<User> autisti_prenotazioni = new ArrayList<User>();
		ArrayList<Viaggio> viaggi_prenotazioni = new ArrayList<Viaggio>();
		ArrayList<Recensione> recensione_temporanea = new ArrayList<Recensione>();
		for(Prenotazione p : prenotazioni_utente_input) {
			autisti_prenotazioni.add(userRepo.findByID_prenotazione(p.getID_prenotazione()));
			viaggi_prenotazioni.add(viaggioRepo.findViaggioByPrenotazioneId(p.getID_prenotazione()));
			recensione_temporanea.add(recensioneRepo.findByPrenotazioneId(p.getID_prenotazione()));
		} 
		model.addAttribute("autisti", autisti_prenotazioni);
		model.addAttribute("viaggi_prenotazioni", viaggi_prenotazioni);
		model.addAttribute("prenotazioni_utente_input", prenotazioni_utente_input);
		model.addAttribute("recensioni", recensione_temporanea);
		model.addAttribute("recensione_input", new Recensione());
		model.addAttribute("prenotazione_input", new Prenotazione());

		
		
		model.addAttribute("viaggi", viaggi);
		return "ViaggiPrenotazioni";
	}
}
