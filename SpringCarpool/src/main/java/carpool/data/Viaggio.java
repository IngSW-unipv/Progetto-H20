package carpool.data;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "viaggio")
public class Viaggio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Controllare nel database se è effettivamente impostato AI
	private long viaggioId;
	@Column(nullable = false, unique = false, length = 45)
	private long userId;
	@Column(nullable = false, unique = false, length = 45)
	private long id_automobile;
	@Column(nullable = false, unique = false, length = 45)
	private String partenza;
	@Column(nullable = false, unique = false, length = 45)
	private double partenzaX;
	@Column(nullable = false, unique = false, length = 45)
	private double partenzaY;
	@Column(nullable = false, unique = false, length = 45)
	private String destinazione;
	@Column(nullable = false, unique = false, length = 45)
	private double destinazioneX;
	private double destinazioneY;
	@Column(nullable = false, unique = false, length = 5)
	private int postiTotali;
	@Column(nullable = false, unique = false, length = 5)
	private int postiPrenotati;
	@Column(nullable = false, unique = false, length = 5)
	private double tariffaKm;
	@Column(nullable = false, unique = false, length = 45)
	private double lunghezza_percorso;
	@Column(nullable = false, unique = false, length = 45)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date viaggioDate;
	private double totale_bagaglio_prenotato; 
	private Boolean cancellato;
	private Boolean concluso;
	
	//La tag Transient permette di aggiungere un attributo che non fa parte del DB
	@Transient
	private String datatemp;
	@Transient
	private String oratemp;
	
	
	//Attributo transient usato per mostrare le prenotazioni associate a questo viaggio
	@Transient
	private ArrayList<Prenotazione> prenotazioni_associate;
	public ArrayList<Prenotazione> getPrenotazioni_associate() {
		return prenotazioni_associate;
	}
	public void setPrenotazioni_associate(ArrayList<Prenotazione> prenotazioni_associate) {
		this.prenotazioni_associate = prenotazioni_associate;
	}

	


	
	@Transient
	private double distanza; //Attributo temporaneo per la distanza da un viaggio e l'altro
	
	
	//Il costruttore non serve per mettere il viaggio nel DB, però viene usato per altre cose
	public Viaggio (String partenza, double partenzaX, double partenzaY, String destinazione, double destinazioneX, double destinazioneY, int postiTotali) {
		
		this.partenza = partenza;
		this.partenzaX = partenzaX;
		this.partenzaY = partenzaY;
		this.destinazione = destinazione;
		this.destinazioneX = destinazioneX;
		this.destinazioneY = destinazioneY;
		this.postiTotali = postiTotali;
		this.setCancellato(false);
	}
	
	
	public Viaggio() {
	}

	public long getId_automobile() {
		return id_automobile;
	}

	public void setId_automobile(long id_automobile) {
		this.id_automobile = id_automobile;
	}

	public double getTariffaKm() {
		return tariffaKm;
	}

	public void setTariffaKm(double tariffaKm) {
		this.tariffaKm = tariffaKm;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public double getLunghezza_percorso() {
		return lunghezza_percorso;
	}

	public void setLunghezza_percorso(double lunghezza_percorso) {
		this.lunghezza_percorso = lunghezza_percorso;
	}

	public double getTotale_bagaglio_prenotato() {
		return totale_bagaglio_prenotato;
	}

	public void setTotale_bagaglio_prenotato(double totale_bagaglio_prenotato) {
		this.totale_bagaglio_prenotato = totale_bagaglio_prenotato;
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
	public double getPartenzaX() {
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
	public String getDestinazione() {
		return destinazione;
		}
	public void setDestinazione(String destinazione) {
		this.destinazione = destinazione;
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
	@JsonIgnore
	public Date getcreationDate() {
		return creationDate;
		}
	public void setcreationDate(Date creationDate) {
		this.creationDate = creationDate;
		}

	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getPostiTotali() {
		return postiTotali;
	}
	public void setPostiTotali(int postiTotali) {
		this.postiTotali = postiTotali;
	}

	public int getPostiPrenotati() {
		return postiPrenotati;
	}
	public void setPostiPrenotati(int postiPrenotati) {
		this.postiPrenotati = postiPrenotati;
	}

	public double getDistanza() {
		return distanza;
	}
	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}

	public Date getViaggioDate() {
		return viaggioDate;
	}
	public void setViaggioDate(Date viaggioDate) {
		this.viaggioDate = viaggioDate;
	}


	public String getDatatemp() {
		return datatemp;
	}

	public void setDatatemp(String datatemp) {
		this.datatemp = datatemp;
	}

	public String getOratemp() {
		return oratemp;
	}

	public void setOratemp(String oratemp) {
		this.oratemp = oratemp;
	}
	public Boolean getCancellato() {
		return cancellato;
	}
	public void setCancellato(Boolean cancellato) {
		this.cancellato = cancellato;
	}
	public Boolean getConcluso() {
		return concluso;
	}
	public void setConcluso(Boolean concluso) {
		this.concluso = concluso;
	}

	
	/*
	public Point2D getPuntoPartenza() {
		return puntoPartenza;
	}

	public void setPuntoPartenza(Point2D puntoPartenza) {
		this.puntoPartenza = puntoPartenza;
	}

	public Point2D getPuntoDestinazione() {
		return puntoDestinazione;
	}

	public void setPuntoDestinazione(Point2D puntoDestinazione) {
		this.puntoDestinazione = puntoDestinazione;
	}*/
	
}
