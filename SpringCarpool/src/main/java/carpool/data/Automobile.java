package carpool.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "automobile")
public class Automobile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_automobile")
	private long ID_automobile;
	@Column(nullable = false, unique = false, length = 45)
	private long ID_user;
	@Column(nullable = false, unique = false, length = 45)
	private String targa;
	private int cilindrata;
	@Column(nullable = false, unique = false, length = 45)
	private int posti_totali;
	@Column(nullable = false, unique = false, length = 45)
	private int vol_bagaglialio_tot;
	
	public long getID_automobile() {
		return ID_automobile;
	}
	public void setID_automobile(long iD_automobile) {
		ID_automobile = iD_automobile;
	}
	public long getID_user() {
		return ID_user;
	}
	public void setID_user(long iD_user) {
		ID_user = iD_user;
	}
	public String getTarga() {
		return targa;
	}
	public void setTarga(String targa) {
		this.targa = targa;
	}
	public int getCilindrata() {
		return cilindrata;
	}
	public void setCilindrata(int cilindrata) {
		this.cilindrata = cilindrata;
	}
	public int getPosti_totali() {
		return posti_totali;
	}
	public void setPosti_totali(int posti_totali) {
		this.posti_totali = posti_totali;
	}
	public int getVol_bagaglialio_tot() {
		return vol_bagaglialio_tot;
	}
	public void setVol_bagaglialio_tot(int vol_bagaglialio_tot) {
		this.vol_bagaglialio_tot = vol_bagaglialio_tot;
	}

}
