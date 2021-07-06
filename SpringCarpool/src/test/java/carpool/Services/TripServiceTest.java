package carpool.Services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import carpool.data.Reservation;
import carpool.data.Trip;
import carpool.repos.ReservationRepository;
import carpool.repos.TripRepository;
import carpool.services.TripService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class TripServiceTest {
	
	@MockBean
	private Trip MockTrip; 
	
	@MockBean
	private Reservation MockReservation; 
	
	@MockBean
	private TripRepository MockTripRepo;
	
	@MockBean 
	private ReservationRepository MockReservationRepo;
	
	@InjectMocks
	private TripService MockTripService;
	
	private long 	MockUserId = 2;
	private int 	MockReservedSeats 	= 2;
	
	//trip info	(check github for more attributes)
	private long 	MockTripId = 12L;
	private int 	MockTripDriverId 		= 1;
	private int 	MockTripTotalSeats 		= 4;

	//other reservation info
	//private long 	Mock2ReservationId  = 2;
	private long 	Mock2UserId 		= 3;
	private long 	Mock2TripId 		= 0;
	private Date 	Mock2CreationDate 	= new Date(); //today
	private Boolean Mock2Deleted		= false;
	private int 	Mock2ReservedSeats	= 3;
	//private Boolean Mock2PaymentMade    = false;
	
	/**
	 * Test method for {@link carpool.services.TripService#bookTrip(carpool.data.Reservation, long)}.
	 */
	@Test
	@DisplayName("test della prenotazione viaggio")
	void testBookTrip() {

		//setup trip	
		Trip trip = new Trip();
		trip.setTripId(MockTripId);
		trip.setUserId(MockTripDriverId);
		trip.setTotalSeats(MockTripTotalSeats);
		int tripPrevResSeats = !Mock2Deleted ? Mock2ReservedSeats : 0;
		trip.setReservedSeats(tripPrevResSeats);
		
		//setup reservation
		Reservation res = new Reservation();
		res.setTripId(MockTripId);
		res.setReservedSeats(MockReservedSeats);
		
		//setup reservation 2
		Reservation res2 = new Reservation(Mock2UserId,Mock2TripId,Mock2CreationDate);
		res2.setDeleted(Mock2Deleted);
		res2.setReservedSeats(Mock2ReservedSeats);
		List<Reservation> ress = new ArrayList<Reservation>();
		ress.add(res2);
		
		//Mock viaggio repo
		Mockito.when(MockTripRepo.findById(MockTripId)).thenReturn(Optional.of(trip));
		
		//mock res repo
		Mockito.when(MockReservationRepo.findByTripId(MockTripId)).thenReturn(ress);
		
		//mock save
		Mockito.when(MockReservationRepo.save(res)).thenReturn(null);
		
		//if prenotation was successfull trip reserved seats must be:
		//previous seats + reservation's reserved seats
		if(MockTripService.bookTrip(res, MockUserId))
		{
			assertTrue(trip.getReservedSeats()  == tripPrevResSeats + MockReservedSeats);
		}
		else
		{
			//if prenotation failed it means that tot seats < tripPrevResSeats + MockReservedSeats
			//check this
			assertTrue(!trip.hasAvailableSeats(MockReservedSeats));
		}
	}
	
	/**
	 * Test method for {@link carpool.services.TripService#deleteTrip(carpool.data.Trip)}.
	 */
	@Test
	@DisplayName("test della cancellazione del viaggio")
	void testDeleteTrip() {
		//setup trip	
		Trip trip = new Trip();
		trip.setTripId(MockTripId);
		trip.setUserId(MockTripDriverId);
		trip.setTotalSeats(MockTripTotalSeats);
		int tripPrevResSeats = !Mock2Deleted ? Mock2ReservedSeats : 0;
		trip.setReservedSeats(tripPrevResSeats);
		
		//setup reservation 2
		Reservation res2 = new Reservation(Mock2UserId,Mock2TripId,Mock2CreationDate);
		res2.setDeleted(Mock2Deleted);
		res2.setReservedSeats(Mock2ReservedSeats);
		
		//setup reservation array
		List<Reservation> ress = new ArrayList<Reservation>();
		ress.add(res2);
		
		//mock res repo
		Mockito.when(MockReservationRepo.findByTripId(MockTripId)).thenReturn(ress);
		
		//Mock viaggio repo
		Mockito.when(MockTripRepo.findById(MockTripId)).thenReturn(Optional.of(trip));
		
		MockTripService.deleteTrip(MockTripId);
		
		//trip è cancellata e la res2 è cancellata
		assertTrue(trip.getDeleted() && res2.getDeleted());	
	}


}
