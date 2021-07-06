package carpool.services;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
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
	public boolean addCar(Car car, User user) {
	
		if(user!=null)
		{
			car.setUserId(user.getUserId());
			carRepo.save(car);	
			return true;
		}
		return false;
	}
	
	public boolean deleteCar(Car car) {
		carRepo.deleteById(car.getCarId());
		return true;
	}
	
	
	public boolean setDefaultCar(long carId) {
		var check= carRepo.findById(carId);
		if(check.isPresent())
		{
			setDefaultCar(check.get());
			return true;
		}
		return false;
	}
	
	private void setDefaultCar(Car car) {
		//set to false the "defaultCar" of all cars owned by the specific user
		ArrayList<Car> cars = carRepo.findAllByUserId(car.getUserId());
		for(Car c : cars) {
			c.setDefaultCar(false);
		}
		//set to true the "defaultCar" of the car used
		car.setDefaultCar(true);
		carRepo.save(car);
	}

	/**
	 * @param multipartFile user profile picture
	 * @param user
	 * @return if operation succeeded or not
	 * @throws IOException
	 */
	public boolean uploadImage(MultipartFile multipartFile, String percorso, User user) throws IOException {
		 
		if(user!=null)
		{
			//Prendo il nome del file immagine uploadato
	        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        //Creo la stringa uploadDir che conterrà il percorso dove verrà salvata l'immagine
	        String uploadDir = null;
	        
	        //Se è un'immagine profilo allora uso il primo if, se è un'immagine patente il secondo if
	        //Profile e Driver in questo caso vengono dal bottone nel form
	        if (percorso.equals("Profile")) {
	        	user.setPhotos(fileName);
		        User savedUser = userRepo.save(user);
	        	uploadDir = "user-photos/" + savedUser.getUserId() + "/profile";
	        }
	        else if (percorso.equals("Driver")) {
	        	user.setDriverPhoto(fileName);
		        User savedUser = userRepo.save(user);
	        	 uploadDir = "user-photos/" + savedUser.getUserId() + "/driver";
	        }
	
	        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	        return true;
		}
		return false;
	}

}
