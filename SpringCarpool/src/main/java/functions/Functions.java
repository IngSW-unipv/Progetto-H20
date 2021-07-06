package functions;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

import carpool.data.Trip; 

public  class Functions implements Comparator<Trip>{
	private Trip viaggio;
	private List<Trip> viaggi;
	
	public Functions (Trip viaggio, List<Trip> viaggi) {
		this.viaggio = viaggio;
		this.viaggi = viaggi;
	}

	//Metodo "compare", per riordinare la lista in base alla distanza
	@Override
	public int compare(Trip viaggio1, Trip viaggio2) {
		return Double.compare(viaggio1.getDistance(), viaggio2.getDistance());
		}

	//Questo metodo inserisce in ogni viaggio disponibile la distanza dal viaggio selezionato
	//La distanza verrà poi usata dal metodo "compare" per riordinarli
	public static List<Trip> setDistances (List<Trip> viaggi, Trip viaggio) {
		for (Trip i : viaggi) {
			i.setDistance(distance (i.getStartX(), viaggio.getStartX(), i.getStartY(), viaggio.getStartY()));
		}
		return viaggi;
	}
	
	//Questo metodo restituisce la distanza tra 2 punti
	//el1 e el2 sono l'altezza, per ora sta a 0.0
	public static double distance(double lat1, double lat2, double lon1, double lon2) {
		
		
		double el1 = 0.0;
		double el2 = 0.0;
		final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance);
	}
	
	//La funzione si aspetta in ingresso la lunghezza kilometri (o comunque qualcosa di prestabilito)
	//Restituisce un periodo di tempo in secondi di tipo Duration
		public static Duration estimatedTripDuration (double tripLength) {
			int coefficiente = 36;
			//Data una lunghezza, la moltiplico per un fattore che converta la lunghezza in durata in secondi
			//Il coefficiente dipende dalla velocità ipotizzata
			//Ad esempio se ho una velocità media di 100km/h, avrò 1 ora se il percorso è lungo 100km
			long l1=(long)(tripLength*coefficiente);
			//Converto tutto in secondi in tipo Duration
			Duration d1 = Duration.ofSeconds(l1);
			return d1;
		}
	
	
	
	
}
