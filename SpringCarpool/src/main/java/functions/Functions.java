package functions;

import java.util.Comparator;
import java.util.List;

import carpool.data.Viaggio; 

public  class Functions implements Comparator<Viaggio>{
	private Viaggio viaggio;
	private List<Viaggio> viaggi;
	
	public Functions (Viaggio viaggio, List<Viaggio> viaggi) {
		this.viaggio = viaggio;
		this.viaggi = viaggi;
	}

	//Metodo "compare", per riordinare la lista in base alla distanza
	@Override
	public int compare(Viaggio viaggio1, Viaggio viaggio2) {
		return Double.compare(viaggio1.getDistanza(), viaggio2.getDistanza());
		}

	//Questo metodo inserisce in ogni viaggio disponibile la distanza dal viaggio selezionato
	//La distanza verrà poi usata dal metodo "compare" per riordinarli
	public static List<Viaggio> setDistances (List<Viaggio> viaggi, Viaggio viaggio) {
		for (Viaggio i : viaggi) {
			i.setDistanza(distance (i.getPartenzaX(), viaggio.getPartenzaX(), i.getPartenzaY(), viaggio.getPartenzaY()));
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
	
	
	/*
	 * per filtrare viaggi in base alla data 
	*/
	
	
	
	
	
	
}
