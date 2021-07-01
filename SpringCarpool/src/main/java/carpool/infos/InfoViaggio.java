package carpool.infos;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import carpool.data.Automobile;
import carpool.data.User;
import carpool.data.Viaggio;

//classe usata per il passaggio di alcune informazioni al frontend
public class InfoViaggio 
{
	//Viaggio
	private long 	viaggioId;

	private String 	partenza;

	private double partenzaX;
	
	private double partenzaY;
	
	private String 	destinazione;
	
	private double destinazioneX;
	
	private double destinazioneY;

	private double 	lunghezza_percorso;
	
	private double tariffa;

	private Date 	viaggioDate;
	
	//User
	private String firstName;

	private String lastName;
	
	private String telefono;
	
	private int avgPunteggio;
	
	//Auto
	
	private int postiLiberi;
	
	private double bagagliaioLibero;	//vol
	
	private String targa;
	

	public InfoViaggio(Viaggio viaggio, User user, Automobile auto, int avgPunteggio)
	{
		viaggioId = viaggio.getViaggioId();  
		partenza = viaggio.getPartenza();
		partenzaX= viaggio.getPartenzaX();
		partenzaY= viaggio.getPartenzaY();
		tariffa= viaggio.getTariffaKm();

		destinazione = viaggio.getDestinazione();
		destinazioneX = viaggio.getDestinazioneX();
		destinazioneY = viaggio.getDestinazioneY();
		lunghezza_percorso = viaggio.getLunghezza_percorso();
		viaggioDate = viaggio.getViaggioDate();
		
		firstName = user.getFirstName();
		lastName = user.getLastName();
		telefono = user.getTelefono();
		this.avgPunteggio = avgPunteggio;
		
		postiLiberi = auto.getPosti_totali() - viaggio.getPostiPrenotati();
		bagagliaioLibero = auto.getVol_bagaglialio_tot() - viaggio.getTotale_bagaglio_prenotato();
		targa = auto.getTarga();
	}

	public long getViaggioId() {
		return viaggioId;
	}

	public void setViaggioId(long viaggioId) {
		this.viaggioId = viaggioId;
	}

	public String getPartenza() {
		return partenza;
	}

	public void setPartenza(String partenza) {
		this.partenza = partenza;
	}

	public String getDestinazione() {
		return destinazione;
	}

	public void setDestinazione(String destinazione) {
		this.destinazione = destinazione;
	}

	public double getLunghezza_percorso() {
		return lunghezza_percorso;
	}

	public void setLunghezza_percorso(double lunghezza_percorso) {
		this.lunghezza_percorso = lunghezza_percorso;
	}

	public Date getViaggioDate() {
		return viaggioDate;
	}

	public void setViaggioDate(Date viaggioDate) {
		this.viaggioDate = viaggioDate;
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
		return targa;
	}

	public void setTarga(String targa) {
		this.targa = targa;
	}public double getPartenzaX() {
		return partenzaX;
	}

	public void setPartenzaX(double partenzaX) {
		this.partenzaX = partenzaX;
	}

	public double getPartenzaY() {
		return partenzaY;
	}

	public void setPartenzaY(double partenzaY) {
		this.partenzaY = partenzaY;
	}

	public double getDestinazioneX() {
		return destinazioneX;
	}

	public void setDestinazioneX(double destinazioneX) {
		this.destinazioneX = destinazioneX;
	}

	public double getDestinazioneY() {
		return destinazioneY;
	}

	public void setDestinazioneY(double destinazioneY) {
		this.destinazioneY = destinazioneY;
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