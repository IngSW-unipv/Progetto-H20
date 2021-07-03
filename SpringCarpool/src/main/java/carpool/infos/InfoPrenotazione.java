package carpool.infos;

import java.util.Date;

import carpool.data.Reservation;
import carpool.data.User;

public class InfoPrenotazione {
	
	//PRENOTAZIONE
	private int 	reservedSeats;
	
	private Date 	creationDate;

	private String 	meetingAddress;
	
	private long 	reservedTrunk;
	
	//USER
	private String firstName;

	private String lastName;

	private String phoneNumber;
	
	public InfoPrenotazione(Reservation prenotazione, User user)
	{
		this.reservedSeats = prenotazione.getReservedSeats();        

		this.creationDate = prenotazione.getCreationDate();       
                 
		this.meetingAddress = prenotazione.getMeetingAddress();  
                   
		this.reservedTrunk = prenotazione.getReservedTrunk();

		this.firstName = user.getFirstName();

		this.lastName = user.getLastName();
       
		this.phoneNumber = user.getPhoneNumber();
	}

	public int getNumero_posti() {
		return reservedSeats;
	}

	public void setNumero_posti(int reservedSeats) {
		this.reservedSeats = reservedSeats;
	}

	public Date getCreation_date() {
		return creationDate;
	}

	public void setCreation_date(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getIndirizzo_incontro() {
		return meetingAddress;
	}

	public void setIndirizzo_incontro(String meetingAddress) {
		this.meetingAddress = meetingAddress;
	}

	public long getBagagliaio_prenotato() {
		return reservedTrunk;
	}

	public void setBagagliaio_prenotato(long reservedTrunk) {
		this.reservedTrunk = reservedTrunk;
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
	
	
}
