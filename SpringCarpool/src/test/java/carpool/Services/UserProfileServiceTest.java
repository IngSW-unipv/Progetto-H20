package carpool.Services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import carpool.data.Car;
import carpool.data.Reservation;
import carpool.data.Trip;
import carpool.repos.CarRepository;
import carpool.services.UserProfileService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class UserProfileServiceTest {
	
	
	@MockBean
	private Trip MockTrip; 
	
	@MockBean
	private Reservation MockReservation; 
	
	@MockBean
	private CarRepository MockCarRepo;

	@InjectMocks
	private UserProfileService MockUserProfileService;
	
	private long MockUserId = 12;
	private long MockCarId = 11;
	/**
	 * Test method for {@link carpool.services.UserProfileService#setDefaultCar(long)}.
	 */
	@Test
	@DisplayName("test set default della macchina")
	void testSetDefaultCar() {
		
		Car car = new Car();
		car.setUserId(MockUserId);
		car.setDefaultCar(false);
		
		Car otherCar = new Car();
		otherCar.setUserId(MockUserId);
		otherCar.setDefaultCar(true);
		
		ArrayList<Car> userCars = new ArrayList<Car>();
		userCars.add(car);
		userCars.add(otherCar);
		
		//Mock viaggio repo
		Mockito.when(MockCarRepo.findById(MockCarId)).thenReturn(Optional.of(car));
		
		//mock res repo
		Mockito.when(MockCarRepo.findAllByUserId(MockUserId)).thenReturn(userCars);
		
		//mock save
		Mockito.when(MockCarRepo.save(car)).thenReturn(null);
		
		MockUserProfileService.setDefaultCar(MockCarId);
		
		//ensure car is the default car and the other one is no more
		assertTrue(car.isDefaultCar() && !otherCar.isDefaultCar());
	}

}
