package carpool.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "reservations")
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long reservationId;
	@Column(nullable = false, unique = false, length = 45)
	private long userId;
	@Column(nullable = false, unique = false, length = 45)
	private long tripId;
	
	private int reservedSeats;
	
	private long reservedTrunk;

	@Column(nullable = false, unique = false, length = 45)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	private String meetingAddress;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	private Boolean deleted;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	private Boolean paymentMade;
	
	@Transient
	private Review review;

	@Transient
	private User user;

	
	public Reservation(int reservedSeats, long tripId, String ora_prenotazione, String meetingAddress) {
		this.reservedSeats = reservedSeats;
		this.tripId = tripId;
		this.meetingAddress = meetingAddress;
		this.deleted = false;
	}
	
	public Reservation() {
	}
	

	
	public Boolean getPaymentMade() {
		return paymentMade;
	}

	public void setPaymentMade(Boolean paymentMade) {
		this.paymentMade = paymentMade;
	}
	
	public long getReservationId() {
		return reservationId;
	}
	public void setReservationId(int iD_prenotazione) {
		reservationId = iD_prenotazione;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long iD_user) {
		userId = iD_user;
	}
	public long getTripId() {
		return tripId;
	}
	public void setTripId(long iD_viaggio) {
		tripId = iD_viaggio;
	}
	public int getReservedSeats() {
		return reservedSeats;
	}
	public void setReservedSeats(int reservedSeats) {
		this.reservedSeats = reservedSeats;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getMeetingAddress() {
		return meetingAddress;
	}
	public void setMeetingAddress(String meetingAddress) {
		this.meetingAddress = meetingAddress;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public long getReservedTrunk() {
		return reservedTrunk;
	}

	public void setReservedTrunk(long reservedTrunk) {
		this.reservedTrunk = reservedTrunk;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	
	
}