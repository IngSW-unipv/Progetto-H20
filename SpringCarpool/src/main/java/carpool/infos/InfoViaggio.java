package carpool.infos;

import java.util.Date;
import carpool.data.Car;
import carpool.data.User;
import carpool.data.Trip;

//classe usata per il passaggio di alcune informazioni al frontend
public class InfoViaggio 
{
	//Viaggio
	private long 	tripId;

	private String 	start;

	private double startX;
	
	private double startY;
	
	private String 	end;
	
	private double endX;
	
	private double endY;

	private double 	tripLength;
	
	private double tariffa;

	private Date 	tripDate;
	
	//User
	private String firstName;

	private String lastName;
	
	private String phoneNumber;
	
	private int avgPunteggio;
	
	//Auto
	
	private int postiLiberi;
	
	private double bagagliaioLibero;	//vol
	
	private String licensePlate;
	

	public InfoViaggio(Trip viaggio, User user, Car auto, int avgPunteggio)
	{
		tripId = viaggio.getTripId();  
		start = viaggio.getStart();
		startX= viaggio.getStartX();
		startY= viaggio.getStartY();
		tariffa= viaggio.getPriceKm();

		end =  viaggio.getEnd();
		endX = viaggio.getEndX();
		endY = viaggio.getEndY();
		tripLength = viaggio.getTripLength();
		tripDate = viaggio.getTripDate();
		
		firstName = user.getFirstName();
		lastName = user.getLastName();
		phoneNumber = user.getPhoneNumber();
		this.avgPunteggio = avgPunteggio;
		
		postiLiberi = auto.getTotalSeats() - viaggio.getReservedSeats();
		bagagliaioLibero = auto.getTotalTrunk() - viaggio.getReservedTrunk();
		licensePlate = auto.getLicensePlate();
	}

	public long getTripId() {
		return tripId;
	}

	public void setViaggioId(long tripId) {
		this.tripId = tripId;
	}

	public String getStart() {
		return start;
	}

	public void setPartenza(String start) {
		this.start = start;
	}

	public String getDestinazione() {
		return end;
	}

	public void setDestinazione(String end) {
		this.end = end;
	}

	public double getLunghezza_percorso() {
		return tripLength;
	}

	public void setLunghezza_percorso(double tripLength) {
		this.tripLength = tripLength;
	}

	public Date getTripDate() {
		return tripDate;
	}

	public void setTripDate(Date tripDate) {
		this.tripDate = tripDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTelefono() {
		return phoneNumber;
	}

	public void setTelefono(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getPostiLiberi() {
		return postiLiberi;
	}

	public void setPostiLiberi(int postiLiberi) {
		this.postiLiberi = postiLiberi;
	}

	public double getBagagliaioLibero() {
		return bagagliaioLibero;
	}

	public void setBagagliaioLibero(double bagagliaioLibero) {
		this.bagagliaioLibero = bagagliaioLibero;
	}     

	public String getTarga() {
		return licensePlate;
	}

	public void setTarga(String licensePlate) {
		this.licensePlate = licensePlate;
	}public double getStartX() {
		return startX;
	}

	public void setPartenzaX(double startX) {
		this.startX = startX;
	}

	public double getStartY() {
		return startY;
	}

	public void setPartenzaY(double startY) {
		this.startY = startY;
	}

	public double getEndX() {
		return endX;
	}

	public void setDestinazioneX(double endX) {
		this.endX = endX;
	}

	public double getEndY() {
		return endY;
	}

	public void setDestinazioneY(double endY) {
		this.endY = endY;
	}

	public double getTariffa() {
		return tariffa;
	}

	public void setTariffa(double tariffa) {
		this.tariffa = tariffa;
	}

	public int getAvgPunteggio() {
		return avgPunteggio;
	}

	public void setAvgPunteggio(int avgPunteggio) {
		this.avgPunteggio = avgPunteggio;
	}

	
	
}                                    