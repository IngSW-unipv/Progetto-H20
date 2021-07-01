package carpool.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import carpool.CustomUserDetails;
import carpool.data.Automobile;
import carpool.data.User;
import carpool.repos.AutomobileRepository;
import carpool.repos.UserRepository;
import carpool.services.ProfiloService;
import functions.FileUploadUtil;

@Controller
public class ProfiloController {
	
	
	@Autowired
	private ProfiloService profiloService;
	
	
	@PreAuthorize("hasAnyAuthority('AUTISTA')")
	@PostMapping("/auto_add")
	public String autoregister (Automobile automobile) {
		profiloService.autoAddService(automobile);
		
		return "redirect:/";
	}

	
	//Interessante, per il redirect si può ritornare il tipo RedirectView invece di String e poi return "redirect:/";
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/upload_image")
    public RedirectView saveUser(@RequestParam("image") MultipartFile multipartFile) throws IOException {

        profiloService.uploadImageService(multipartFile);
        
        return new RedirectView("/", true);
    }

}
