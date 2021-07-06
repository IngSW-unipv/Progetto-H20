package carpool.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cars")
public class Car {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "car_id")
	private long carId;
	
	@Column(nullable = false, unique = false, length = 45)
	private long userId;
	
	@Column(nullable = false, unique = false, length = 45)
	private String licensePlate;
	
	private int cylinder;
	
	@Column(nullable = false, unique = false, length = 45)
	private int totalSeats;
	
	@Column(nullable = false, unique = false, length = 45)
	private int totalTrunk;

	private boolean defaultCar;
	
	public long getCarId() {
		return carId;
	}
	public void setCarId(long iD_automobile) {
		carId = iD_automobile;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long iD_user) {
		userId = iD_user;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public int getCylinder() {
		return cylinder;
	}
	public void setCylinder(int cylinder) {
		this.cylinder = cylinder;
	}
	public int getTotalSeats() {
		return totalSeats;
	}
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}
	public int getTotalTrunk() {
		return totalTrunk;
	}
	public void setTotalTrunk(int totalTrunk) {
		this.totalTrunk = totalTrunk;
	}
	public boolean isDefaultCar() {
		return defaultCar;
	}
	public void setDefaultCar(boolean defaultCar) {
		this.defaultCar = defaultCar;
	}

}
