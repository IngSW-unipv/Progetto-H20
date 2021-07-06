package carpool.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

	@PostMapping("/ruolo_add")
	public String ruoloAdd(int roleId) {
		userService.addUserRole(roleId,getUserDetails().getUser().getUserId());
		
		//Questo pezzo refresha le autorizzazioni se no tocca riavviare perchè il ruolo venga riconosciuto
		//Aggiungo il ruolo pure qua
		refreshAuthenticationRoles(userService.getRoleName(roleId));
		
		return "redirect:/";
	}
	
	@PostMapping("/ruolo_remove")
	public String ruoloRemove(int ruolo) {
		userService.removeUserRole(ruolo, getUserDetails().getUser().getUserId());
		return "redirect:/";
	}

	
}
