package carpool.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import carpool.data.User;
import carpool.services.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/user_register")
	public String userRegister(User user, RedirectAttributes atts) {
		if(!userService.userRegisterService(user))
		{
			atts.addAttribute("hasErrors", true);
		}
		return "redirect:/";
	}
	
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/user_edit_FirstName")
	public String userEdit(String FirstName) {
		userService.editFirstNameService(FirstName);
	    return "redirect:/";
	}
	
	@PostMapping("/user_edit_Password")
	public String userEditPassword(String Password) {
		userService.editPasswordService(Password);
	    return "redirect:/";
	}

	@PostMapping("/ruolo_add")
	public String ruoloAdd(int ruolo) {
		userService.addRoleService(ruolo);
		return "redirect:/";
	}
	
	@PostMapping("/ruolo_remove")
	public String ruoloRemove(int ruolo) {
		userService.removeRoleService(ruolo);
		return "redirect:/";
	}
}
