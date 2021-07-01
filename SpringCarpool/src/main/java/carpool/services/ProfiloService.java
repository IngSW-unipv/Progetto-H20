package carpool.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import carpool.CustomUserDetails;
import carpool.data.Automobile;
import carpool.data.Prenotazione;
import carpool.data.User;
import carpool.data.Viaggio;
import carpool.repos.AutomobileRepository;
import carpool.repos.PrenotazioneRepository;
import carpool.repos.UserRepository;
import carpool.repos.ViaggioRepository;
import functions.FileUploadUtil;
import functions.Functions;

@Service

public class ProfiloService {
	
	@Autowired 
	private AutomobileRepository automobileRepo;
	
	@Autowired
	private UserRepository userRepo;


	public void autoAddService(Automobile automobile) {
		CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getPrincipal();
		User user1 = user.getUser();
		automobile.setID_user(user1.getID_user());
		automobileRepo.save(automobile);
	}


	public void uploadImageService(MultipartFile multipartFile) throws IOException {
		 
        CustomUserDetails user1 = (CustomUserDetails) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
        User user2 = user1.getUser();

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user2.setPhotos(fileName);

        User savedUser = userRepo.save(user2);

        String uploadDir = "user-photos/" + savedUser.getID_user();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
	}
	
}
