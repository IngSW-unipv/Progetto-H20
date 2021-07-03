package carpool.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import carpool.data.Car;
import carpool.services.UserProfileService;

@Controller
public class UserProfileController extends ControllerUtility{
	
	
	@Autowired
	private UserProfileService userProfileService;
		
	@PreAuthorize("hasAnyAuthority('AUTISTA')")
	@PostMapping("/add-car")
	public String addCar (Car car) {
		userProfileService.addCar(car, getUserDetails().getUser());
		
		return "redirect:/";
	}

	
	//Interessante, per il redirect si può ritornare il tipo RedirectView invece di String e poi return "redirect:/";
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/upload-image")
    public RedirectView uploadImage(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        userProfileService.uploadImage(multipartFile, getUserDetails().getUser());
        
        return new RedirectView("/", true);
    }

}
