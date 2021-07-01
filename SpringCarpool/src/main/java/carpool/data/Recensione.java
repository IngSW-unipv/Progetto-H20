package carpool.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Recensione {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ID_recensione;
	@Column(nullable = false, unique = false, length = 45)
	private long ID_prenotazione;
	@Column(nullable = false, unique = false, length = 45)
	private int score;
	private String testo_recensione;
	@Column(nullable = false, unique = false, length = 45)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	
	public long getID_recensione() {
		return ID_recensione;
	}
	public void setID_recensione(long iD_recensione) {
		ID_recensione = iD_recensione;
	}
	public long getID_prenotazione() {
		return ID_prenotazione;
	}
	public void setID_prenotazione(long iD_prenotazione) {
		ID_prenotazione = iD_prenotazione;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getTesto_recensione() {
		return testo_recensione;
	}
	public void setTesto_recensione(String testo_recensione) {
		this.testo_recensione = testo_recensione;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creation_date) {
		this.creationDate = creation_date;
	}

}
