package carpool.infos;

import java.util.Date;

import carpool.data.Prenotazione;
import carpool.data.User;

public class InfoPrenotazione {
	
	//PRENOTAZIONE
	private int 	numero_posti;
	
	private Date 	creation_date;

	private String 	indirizzo_incontro;
	
	private long 	bagagliaio_prenotato;
	
	//USER
	private String firstName;

	private String lastName;

	private String telefono;
	
	public InfoPrenotazione(Prenotazione prenotazione, User user)
	{
		this.numero_posti = prenotazione.getNumero_posti();        

		this.creation_date = prenotazione.getCreation_date();       
                 
		this.indirizzo_incontro = prenotazione.getIndirizzo_incontro();  
                   
		this.bagagliaio_prenotato = prenotazione.getBagagliaio_prenotato();

		this.firstName = user.getFirstName();

		this.lastName = user.getLastName();
       
		this.telefono = user.getTelefono();
	}

	public int getNumero_posti() {
		return numero_posti;
	}

	public void setNumero_posti(int numero_posti) {
		this.numero_posti = numero_posti;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public String getIndirizzo_incontro() {
		return indirizzo_incontro;
	}

	public void setIndirizzo_incontro(String indirizzo_incontro) {
		this.indirizzo_incontro = indirizzo_incontro;
	}

	public long getBagagliaio_prenotato() {
		return bagagliaio_prenotato;
	}

	public void setBagagliaio_prenotato(long bagagliaio_prenotato) {
		this.bagagliaio_prenotato = bagagliaio_prenotato;
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
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	
}
