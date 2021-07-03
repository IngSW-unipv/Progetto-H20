package carpool.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import carpool.data.Reservation;
import carpool.data.Review;
import carpool.data.Trip;
import carpool.repos.ReservationRepository;
import carpool.repos.ReviewRepository;
import carpool.repos.TripRepository;

@Service
public class ReservationService {
	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired 
	private ReservationRepository reservationRepo;
	
	@Autowired
	private TripRepository tripRepo;

	private void deleteReservation(Reservation resToDelete) {
		
		resToDelete.setDeleted(true);
		int reservedSeats = resToDelete.getReservedSeats();
		long bagaglioPrenotato = resToDelete.getReservedTrunk();
		var check = tripRepo.findById(resToDelete.getTripId());
		if(check.isPresent())
		{
			Trip trip = check.get();
			//remove reserved seats and trunk from the trip
			trip.addReservedSeats(-reservedSeats);
			trip.addReservedTrunk(-bagaglioPrenotato);
			tripRepo.save(trip);
		}
		reservationRepo.save(resToDelete);
	}
	
	public void deleteReservation(long resId) {
		var checkRes = reservationRepo.findById(resId);
		if(!checkRes.isPresent()) return;
		
		deleteReservation(checkRes.get());
	}

	public void setPaymentReceived(long resId) {

		var checkRes = reservationRepo.findById(resId);
		if(checkRes.isPresent())
		{
			setPaymentReceived(checkRes.get());
		}
	}
	
	private void setPaymentReceived(Reservation resPaid) {
		resPaid.setPaymentMade(true);
		reservationRepo.save(resPaid);
	}

	public void reviewReservation(Review review, long  resId) {
		var checkRes = reservationRepo.findById(resId);
		if(checkRes.isPresent()){
			reviewReservation(review,checkRes.get());
		}
	}
	
	private void reviewReservation(Review review, Reservation res) {
		//Imposto la data in cui la recensione è stato creata
		Date creationDate = new Date();
		review.setCreationDate(creationDate);
		reviewRepo.save(review);
	}
}
