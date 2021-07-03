package carpool.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import carpool.data.User;
import carpool.services.UserService;

@Controller
public class UserController extends ControllerUtility{

	@Autowired
	private UserService userService;

	@PostMapping("/user_register")
	public String userRegister(User user, RedirectAttributes atts) {
		if(!userService.registerUser(user))
		{
			atts.addAttribute("hasErrors", true);
		}
		return "redirect:/";
	}
	
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/user_edit_FirstName")
	public String userEdit(String FirstName) {
		userService.editUserFirstName(FirstName, getUserDetails().getUser());
	    return "redirect:/";
	}
	
	@PostMapping("/user_edit_Password")
	public String userEditPassword(String Password) {
		userService.editUserPassword(Password, getUserDetails().getUser());
	    return "redirect:/";
	}

	@PostMapping("/ruolo_add")
	public String ruoloAdd(int roleId) {
		userService.addUserRole(roleId,getUserDetails().getUser());
		
		//Questo pezzo refresha le autorizzazioni se no tocca riavviare perchè il ruolo venga riconosciuto
		//Aggiungo il ruolo pure qua
		refreshAuthenticationRoles(userService.getRoleName(roleId));
		
		return "redirect:/";
	}
	
	@PostMapping("/ruolo_remove")
	public String ruoloRemove(int ruolo) {
		userService.removeUserRole(ruolo, getUserDetails().getUser());
		return "redirect:/";
	}

	
}
