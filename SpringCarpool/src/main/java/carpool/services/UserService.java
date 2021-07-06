package carpool.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	private String defaultRoleName = "PASSEGGERO";
	
	//TODO TEST
	public boolean registerUser(User user)
	{
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(user.getPassword());
	    user.setPassword(encodedPassword);
	    user.setEnabled(true);
	    User check = userRepo.findByEmail(user.getEmail());
	    if (check != null) {
	    	System.out.println("userRegister: Utente già esistente");
	    	return false;
	    }

		Role role = null;
		var existingRoles = roleRepo.findAll();
		for (Role r : existingRoles) {
			if (r.getName().equals(defaultRoleName)) { 
				role = r;
			}
		}
		user.addRole(role);
		userRepo.save(user);
		System.out.println("Nuovo utente registrato! ");
  
    	return true;
	}
	
	public void editUser(User userUpdated, User userToUpdate) {
		//Aggiorno i campi modificabili
		userToUpdate.setEmail(userUpdated.getEmail());
		userToUpdate.setFirstName(userUpdated.getFirstName());
		userToUpdate.setLastName(userUpdated.getLastName());
		userToUpdate.setPhoneNumber(userUpdated.getPhoneNumber());
		
		//Dunque salvo l'user aggiornato nel DB
		userRepo.save(userToUpdate);
	}
	
	public boolean editUserPassword(String Password, User userToUpdate) {
		
		if(Password.equals("you wish :^)"))
			return true;
		if(userToUpdate!=null) {
		    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		    String encodedPassword = passwordEncoder.encode(Password);
		    userToUpdate.setPassword(encodedPassword);
		    userRepo.save(userToUpdate);
		    return true;
		}
		return false;
	}
	
	//TODO TEST ?
	public boolean addUserRole(int roleId, long userId)
	{
		var checkRole = roleRepo.findById(roleId);
		var checkUser = userRepo.findById(userId);
		if(checkRole.isPresent() && checkUser.isPresent())
		{
			return addUserRole(checkRole.get(), checkUser.get());
		}
		return false;
	}
	
	private boolean addUserRole(Role role, User user)
	{
		//Controllo se il ruolo che voglio aggiungere, esiste 
		//(un ruolo inesistente non si può aggiungere, il db lo impedisce, ma meglio controllare)
		//Funfact, un ruolo inesistente nel db non entra, ma in user.getRoles() (cioè in memoria del server) viene aggiunto
		//Quindi eventuali check in user.getRoles come quel getName prima, cercherebbero un ruolo inesistente
		//Se si chiude e rilancia l'applicazione, user.getRoles avrà solo i dati dal db
		var existingRoles = roleRepo.findAll();
		for (Role r : existingRoles) {
			if (r.getName().equals(role.getName())) {

				//Il ruolo esiste
				if(!user.hasRole(role))
				{
					user.addRole(role);
					//Salvo tutto, così facendo salvo pure il contenuto dell'array
					userRepo.save(user); 
					return true;
				}
			}
		}
		return false;
	}

	public boolean removeUserRole(int roleId, long userId)
	{		
		var checkRole = roleRepo.findById(roleId);
		var checkUser = userRepo.findById(userId);
		if(checkRole.isPresent() && checkUser.isPresent())
		{
			removeUserRole(checkRole.get(), checkUser.get());
			return true;
		}
		return false;
	}
	
	private void removeUserRole(Role role, User user)
	{
		user.removeRole(role);
		userRepo.saveAndFlush(user);
		roleRepo.saveAndFlush(role);
	}
	
	public String getRoleName(int roleId)
	{
		var check = roleRepo.findById(roleId);
		if(!check.isPresent())
		{
			return null;
		}
		Role role = check.get();
		return role.getName();
	}
}
