package carpool.controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import carpool.data.Car;
import carpool.data.User;
import carpool.repos.CarRepository;
import carpool.services.UserProfileService;
import carpool.services.UserService;

@Controller
public class UserProfileController extends ControllerUtility{
	
	@Autowired
	private CarRepository carRepo;
	
	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private UserService userService;
	
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/editUserDetails")
    public String editUserDetails(User updatedUser, String newPassword){
        
		userService.editUser(updatedUser, getUserDetails().getUser());
		userService.editUserPassword(newPassword, getUserDetails().getUser());
        return "redirect:/Profilo";
    }
	
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/addCar")
	public String addCar (Car car) {
		userProfileService.addCar(car, getUserDetails().getUser());
		
		return "redirect:/Profilo";
	}
	
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/deleteCar")
	public String deleteCar (Car car) {
		userProfileService.deleteCar(car);
		
		return "redirect:/Profilo";
	}

	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/setDefaultCar")
	public String setDefaultCar (Car car) {
		userProfileService.setDefaultCar(car.getCarId());
		
		return "redirect:/Profilo";
	}
	
	//Interessante, per il redirect si può ritornare il tipo RedirectView invece di String e poi return "redirect:/";
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/upload-image")
    public RedirectView uploadImage(@RequestParam("image") MultipartFile multipartFile, @RequestParam("percorso") String percorso) throws IOException {
        userProfileService.uploadImage(multipartFile, percorso, getUserDetails().getUser());
        
        return new RedirectView("/Profilo", true);
    }
	
	@PreAuthorize("hasAnyAuthority('AUTISTA', 'PASSEGGERO')")
	@PostMapping("/upload-driverimage")
    public RedirectView uploadImageDriverLicense(@RequestParam("image") MultipartFile multipartFile, @RequestParam("percorso") String percorso) throws IOException {
        userProfileService.uploadImage(multipartFile, percorso, getUserDetails().getUser());
        
        return new RedirectView("/Profilo", true);
    }

}
