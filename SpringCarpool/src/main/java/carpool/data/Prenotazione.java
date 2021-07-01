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
@Table(name = "prenotazione")
public class Prenotazione {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ID_prenotazione;
	@Column(nullable = false, unique = false, length = 45)
	private long ID_user;
	@Column(nullable = false, unique = false, length = 45)
	private long ID_viaggio;
	
	private int numero_posti;
	
	private long bagagliaio_prenotato;


	@Column(nullable = false, unique = false, length = 45)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creation_date;
	private String indirizzo_incontro;
	@Column(columnDefinition = "boolean default false", nullable = false)
	private Boolean cancellata;
	@Column(columnDefinition = "boolean default false", nullable = false)
	private Boolean pagamento_effettuato;
	 
	
	@Transient
	private Recensione tmprecensione;

	@Transient
	private User usertemp;

	
	public Prenotazione(int numero_posti, long ID_viaggio, String ora_prenotazione, String indirizzo_incontro) {
		this.numero_posti = numero_posti;
		this.ID_viaggio = ID_viaggio;
		this.indirizzo_incontro = indirizzo_incontro;
		this.cancellata = false;
	}
	
	public Prenotazione() {
	}
	

	
	public Boolean getPagamento_effettuato() {
		return pagamento_effettuato;
	}

	public void setPagamento_effettuato(Boolean pagamento_effettuato) {
		this.pagamento_effettuato = pagamento_effettuato;
	}
	
	public long getID_prenotazione() {
		return ID_prenotazione;
	}
	public void setID_prenotazione(int iD_prenotazione) {
		ID_prenotazione = iD_prenotazione;
	}
	public long getID_user() {
		return ID_user;
	}
	public void setID_user(long iD_user) {
		ID_user = iD_user;
	}
	public long getID_viaggio() {
		return ID_viaggio;
	}
	public void setID_viaggio(long iD_viaggio) {
		ID_viaggio = iD_viaggio;
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

	public Boolean getCancellata() {
		return cancellata;
	}

	public void setCancellata(Boolean cancellata) {
		this.cancellata = cancellata;
	}
	
	public long getBagagliaio_prenotato() {
		return bagagliaio_prenotato;
	}

	public void setBagagliaio_prenotato(long bagagliaio_prenotato) {
		this.bagagliaio_prenotato = bagagliaio_prenotato;
	}
	
	public User getUsertemp() {
		return usertemp;
	}

	public void setUsertemp(User usertemp) {
		this.usertemp = usertemp;
	}

	public Recensione getTmprecensione() {
		return tmprecensione;
	}

	public void setTmprecensione(Recensione tmprecensione) {
		this.tmprecensione = tmprecensione;
	}

	
	
}