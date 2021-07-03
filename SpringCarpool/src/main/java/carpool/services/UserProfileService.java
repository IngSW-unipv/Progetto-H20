package carpool.services;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import carpool.CustomUserDetails;
import carpool.data.Car;
import carpool.data.User;
import carpool.repos.CarRepository;
import carpool.repos.UserRepository;
import functions.FileUploadUtil;

@Service
public class UserProfileService {
	
	@Autowired 
	private CarRepository carRepo;
	
	@Autowired
	private UserRepository userRepo;

	/**
	 * @param car to register for a given user
	 * @param user
	 * @return if operation succeeded or not
	 */
	public boolean addCar(Car car, 	User user) {
	
		if(user!=null)
		{
			car.setUserId(user.getUserId());
			carRepo.save(car);	
			return true;
		}
		return false;
	}

	/**
	 * @param multipartFile user profile picture
	 * @param user
	 * @return if operation succeeded or not
	 * @throws IOException
	 */
	public boolean uploadImage(MultipartFile multipartFile, User user) throws IOException {
		 
		if(user!=null)
		{
	        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        user.setPhotos(fileName);
	
	        User savedUser = userRepo.save(user);
	
	        String uploadDir = "user-photos/" + savedUser.getUserId();
	
	        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	        return true;
		}
		return false;
	}
	
}
