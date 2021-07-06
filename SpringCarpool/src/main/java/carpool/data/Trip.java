package carpool.data;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "trips")
public class Trip {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Controllare nel database se è effettivamente impostato AI
	private long tripId;
	
	@Column(nullable = false, unique = false, length = 45)
	private long userId;
	
	@Column(nullable = false, unique = false, length = 45)
	private long carId;
	
	@Column(nullable = false, unique = false, length = 100)
	private String start;	//partenza
	
	@Column(nullable = false, unique = false, length = 45)
	private double startX;	
	
	@Column(nullable = false, unique = false, length = 45)
	private double startY;
	
	@Column(nullable = false, unique = false, length = 100)
	private String end;	//destinazione
	
	@Column(nullable = false, unique = false, length = 45)
	private double endX;
	
	@Column(nullable = false, unique = false, length = 45)
	private double endY;
	
	@Column(nullable = false, unique = false, length = 5)
	private int totalSeats;
	
	@Column(nullable = false, unique = false, length = 5)
	private int reservedSeats;
	
	@Column(nullable = false, unique = false, length = 5)
	private double priceKm;
	
	@Column(nullable = false, unique = false, length = 45)
	private double tripLength;	//lunghezza_percorso
	
	@Column(nullable = false, unique = false, length = 45)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date tripDate;	
	
	private double reservedTrunk; 
	private Boolean deleted;
	private Boolean ended;
	
	//La tag Transient permette di aggiungere un attributo che non fa parte del DB
	@Transient
	private String dateString;
	@Transient
	private String timeString;
	
	//Attributo transient usato per mostrare le prenotazioni associate a questo viaggio
	@Transient
	private ArrayList<Reservation> reservations;
	
	//Attributo temporaneo per la distanza da un viaggio e l'altro
	@Transient
	private double distance; 
	
	

	public Trip ( long userId, long carId, String start, double startX, double startY, String end, double endX, double endY, int totalSeats, int reservedSeats, double priceKm,  double tripLength, Date creationDate) {
		
		this.userId = userId;
		this.carId = carId;
		this.start = start;
		this.startX = startX;
		this.startY = startY;
		this.end = end;
		this.endX = endX;
		this.endY = endY;
		this.totalSeats = totalSeats;
		this.deleted = false;
		this.reservedSeats = reservedSeats;
		this.priceKm = priceKm;
		this.tripLength  = tripLength;
		this.creationDate = creationDate;
		this.deleted = false;
		this.ended = false;
	}
	
	//Il costruttore non serve per mettere il viaggio nel DB, però viene usato per altre cose
	public Trip (String start, double startX, double startY, String end, double endX, double endY, int totalSeats) {
		
		this.start = start;
		this.startX = startX;
		this.startY = startY;
		this.end = end;
		this.endX = endX;
		this.endY = endY;
		this.totalSeats = totalSeats;
		this.deleted = false;
	}
	
	public Trip() {
	}


	public long getTripId() {
		return tripId;
	}


	public void setTripId(long tripId) {
		this.tripId = tripId;
	}


	public long getUserId() {
		return userId;
	}


	public void setUserId(long userId) {
		this.userId = userId;
	}


	public long getCarId() {
		return carId;
	}


	public void setCarId(long carId) {
		this.carId = carId;
	}


	public String getStart() {
		return start;
	}


	public void setStart(String start) {
		this.start = start;
	}


	public double getStartX() {
		return startX;
	}


	public void setStartX(double startX) {
		this.startX = startX;
	}


	public double getStartY() {
		return startY;
	}


	public void setStartY(double startY) {
		this.startY = startY;
	}


	public String getEnd() {
		return end;
	}


	public void setEnd(String end) {
		this.end = end;
	}


	public double getEndX() {
		return endX;
	}


	public void setEndX(double endX) {
		this.endX = endX;
	}


	public double getEndY() {
		return endY;
	}


	public void setEndY(double endY) {
		this.endY = endY;
	}

	//SEATS
	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getReservedSeats() {
		return reservedSeats;
	}

	public void setReservedSeats(int reservedSeats) {
		this.reservedSeats = reservedSeats;
	}
	
	public void addReservedSeats(int additionalSeats)
	{
		this.reservedSeats += additionalSeats;
		this.reservedSeats = Math.max(0,  Math.min(this.totalSeats, this.reservedSeats));	//ensure this does not go under 0 and above total seats
	}
	
	public boolean addReservedSeatsIfAvailable(int additionalSeats)
	{
		if(hasAvailableSeats(additionalSeats))
		{
			addReservedSeats(additionalSeats);
			return true;
		}
		return false;
	}
	
	public boolean hasAvailableSeats(int seats)
	{
		return seats <= (this.totalSeats - this.reservedSeats);
	}

	public double getPriceKm() {
		return priceKm;
	}


	public void setPriceKm(double priceKm) {
		this.priceKm = priceKm;
	}


	public double getTripLength() {
		return tripLength;
	}


	public void setTripLength(double tripLength) {
		this.tripLength = tripLength;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public Date getTripDate() {
		return tripDate;
	}


	public void setTripDate(Date tripDate) {
		this.tripDate = tripDate;
	}


	public double getReservedTrunk() {
		return reservedTrunk;
	}


	public void setReservedTrunk(double reservedTrunk) {
		this.reservedTrunk = reservedTrunk;
	}
	
	public void addReservedTrunk(double additionalTrunk)
	{
		this.reservedTrunk += additionalTrunk;
		this.reservedTrunk = Math.min(this.reservedSeats,0.0);	//ensure this does not go under 0
	}


	public Boolean getDeleted() {
		return deleted;
	}


	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}


	public Boolean getEnded() {
		return ended;
	}


	public void setEnded(Boolean ended) {
		this.ended = ended;
	}


	public String getDateString() {
		return dateString;
	}


	public void setDateString(String dateString) {
		this.dateString = dateString;
	}


	public String getTimeString() {
		return timeString;
	}


	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}


	public ArrayList<Reservation> getReservations() {
		return reservations;
	}


	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}


	public double getDistance() {
		return distance;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getDistanceKm()
	{
		return distance/1000;

	}
	
}
