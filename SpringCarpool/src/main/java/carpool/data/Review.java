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

@Entity
@Table(name = "reviews")
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long reviewId;
	
	@Column(nullable = false, unique = false, length = 45)
	private long reservationId;
	
	@Column(nullable = false, unique = false, length = 45)
	private int score;
	
	private String reviewText;
	
	@Column(nullable = false, unique = false, length = 45)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	
	public long getReviewId() {
		return reviewId;
	}
	public void setReviewId(long iD_recensione) {
		reviewId = iD_recensione;
	}
	public long getReservationId() {
		return reservationId;
	}
	public void setReservationId(long iD_prenotazione) {
		reservationId = iD_prenotazione;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getReviewText() {
		return reviewText;
	}
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
