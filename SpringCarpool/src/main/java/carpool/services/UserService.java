package carpool.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import carpool.CustomUserDetails;
import carpool.data.Role;
import carpool.data.User;
import carpool.repos.RoleRepository;
import carpool.repos.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	public boolean userRegisterService(User user)
	{
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);
	    user.setEnabled(true);
	    User check = userRepo.findByEmail(user.getEmail());
	    if (check == null) {
	    	try {
	    		Role role = null;
	    		var ruoli_esistenti = roleRepo.findAll();
	    		for (Role i : ruoli_esistenti) {
	    			if (i.getName().equals("PASSEGGERO")) { 
	    				role = i;
	    				}
	    			}
	    		user.getRoles().add(role);
	    		userRepo.save(user);
	    		System.out.println("Nuovo utente registrato! ");
	  
	    		return true;
	    	}
	    	catch (Exception e) {
	    		System.out.println("userRegister Eccezione! ");
	    		return false;
	    		}
	    	}
	    else {
	    	System.out.println("userRegister: Utente già esistente");
	    	return false;
	    }
	}
	
	public boolean editFirstNameService(String FirstName)
	{
		CustomUserDetails user1 = (CustomUserDetails) SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getPrincipal();
		
		
		User user2 = user1.getUser();
		user2.setFirstName(FirstName);
	    userRepo.save(user2);
	    return true;
	}
	
	public boolean editPasswordService(String Password)
	{
		CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getPrincipal();
		User user1 = user.getUser();
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(Password);
	    user1.setPassword(encodedPassword);
	    userRepo.save(user1);
	    return true;
	}
	
	public boolean addRoleService(int ruolo)
	{
		//Per prima cosa prendo l'id dell'utente loggato
		var auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user1 = (CustomUserDetails) auth.getPrincipal();
		User user = user1.getUser();
		//Prendo il ruolo che è dato come input
		Role role = roleRepo.getRoleById(ruolo);
		
		//Questo pezzo mostra i ruoli che l'utente loggato ha, ma è solo per test, si può levare
		var lista = user.getRoles();
		if (lista != null) {
			for (Role i : lista) {
				System.out.println(i.getName());
				}
			}
		//Controllo se il ruolo che voglio aggiungere, esiste 
		//(un ruolo inesistente non si può aggiungere, il db lo impedisce, ma meglio controllare)
		//Funfact, un ruolo inesistente nel db non entra, ma in user.getRoles() (cioè in memoria del server) viene aggiunto
		//Quindi eventuali check in user.getRoles come quel getName prima, cercherebbero un ruolo inesistente
		//Se si chiude e rilancia l'applicazione, user.getRoles avrà solo i dati dal db
		var ruoli_esistenti = roleRepo.findAll();
		boolean role_exist = false;
		boolean role_already_assigned = false;
		for (Role i : ruoli_esistenti) {
			if (i.getId() == ruolo) {
				role_exist = true;
				
				//Se il ruolo esiste, controllo se l'utente non ha già quel ruolo, per evitare che si possa inutilmente inserirlo più volte
				String nome_ruolo = role.getName();
				for (Role j : lista) {
					if (j.getName().equals(nome_ruolo))	
						role_already_assigned = true;
				}
			}
		}
		
		//Aggiungo tale ruolo all'array dell'utente
		if (role_exist && !role_already_assigned) {
			user.getRoles().add(role);
			//Salvo tutto, così facendo salvo pure il contenuto dell'array
			userRepo.save(user); 
		}
		
		//Questo pezzo refresha le autorizzazioni se no tocca riavviare perchè il ruolo venga riconosciuto
		//Aggiungo il ruolo pure qua
		List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
		updatedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
		SecurityContextHolder.getContext().setAuthentication(newAuth);

		//Funziona, ma durante una sessione, ogni volta aggiunge a ripetizione se aggiungi più volte
		//Questo perchè ogni volta aggiungi nell'array, e ogni volta lo salvi.
		//Dunque tolgo subito dopo il ruolo dall'array
		//Nah, con gli if di controllo, non reinserisce duplicati
		//user.getRoles().remove(role);	
	    return true;
	}
	
	public boolean removeRoleService(int ruolo)
	{
		//Per prima cosa prendo l'id dell'utente loggato
		CustomUserDetails user1 = (CustomUserDetails) SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();
		User user = user1.getUser();
		//Prendo il ruolo che è dato come input
		Role role = roleRepo.getRoleById(ruolo);
		
		user.removeRole(role);
		userRepo.saveAndFlush(user);
		roleRepo.saveAndFlush(role);
		return true;
	}
}
