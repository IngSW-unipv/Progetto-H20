package carpool.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
	
	public boolean registerUser(User user)
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
	
	public boolean editUserFirstName(String FirstName,User user)
	{
		user.setFirstName(FirstName);
	    userRepo.save(user);
	    return true;
	}
	
	public boolean editUserPassword(String Password,User user)
	{
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(Password);
	    user.setPassword(encodedPassword);
	    userRepo.save(user);
	    return true;
	}
	
	public boolean addUserRole(int roleId, User user)
	{
		//Per prima cosa prendo l'id dell'utente loggato
		//Prendo il ruolo che è dato come input
		var check = roleRepo.findById((long)roleId);
		if(!check.isPresent())
		{
			return false;
		}
		Role role = check.get();
		
		//Questo pezzo mostra i ruoli che l'utente loggato ha, ma è solo per test, si può levare
		var roles = user.getRoles();

		for (Role i : roles) {
			System.out.println(i.getName());
			}
		//Controllo se il ruolo che voglio aggiungere, esiste 
		//(un ruolo inesistente non si può aggiungere, il db lo impedisce, ma meglio controllare)
		//Funfact, un ruolo inesistente nel db non entra, ma in user.getRoles() (cioè in memoria del server) viene aggiunto
		//Quindi eventuali check in user.getRoles come quel getName prima, cercherebbero un ruolo inesistente
		//Se si chiude e rilancia l'applicazione, user.getRoles avrà solo i dati dal db
		var existingRoles = roleRepo.findAll();
		boolean roleExists = false;
		boolean roleAlreadyAssigned = false;
		for (Role i : existingRoles) {
			if (i.getId() == roleId) {
				roleExists = true;
				
				//Se il ruolo esiste, controllo se l'utente non ha già quel ruolo, per evitare che si possa inutilmente inserirlo più volte
				String nome_ruolo = role.getName();
				for (Role j : roles) {
					if (j.getName().equals(nome_ruolo))	
						roleAlreadyAssigned = true;
				}
			}
		}
		
		//Aggiungo tale ruolo all'array dell'utente
		if (roleExists && !roleAlreadyAssigned) {
			user.getRoles().add(role);
			//Salvo tutto, così facendo salvo pure il contenuto dell'array
			userRepo.save(user); 
		}
		


		//Funziona, ma durante una sessione, ogni volta aggiunge a ripetizione se aggiungi più volte
		//Questo perchè ogni volta aggiungi nell'array, e ogni volta lo salvi.
		//Dunque tolgo subito dopo il ruolo dall'array
		//Nah, con gli if di controllo, non reinserisce duplicati
		//user.getRoles().remove(role);	
	    return true;
	}
	
	public boolean removeUserRole(int ruolo,User user)
	{
		//Per prima cosa prendo l'id dell'utente loggato
		//Prendo il ruolo che è dato come input
		var check = roleRepo.findById((long)ruolo);
		if(!check.isPresent())
		{
			return false;
		}
		Role role = check.get();
		
		user.removeRole(role);
		userRepo.saveAndFlush(user);
		roleRepo.saveAndFlush(role);
		return true;
	}
	
	public String getRoleName(int roleId)
	{
		var check = roleRepo.findById((long)roleId);
		if(!check.isPresent())
		{
			return null;
		}
		Role role = check.get();
		return role.getName();
	}
}
