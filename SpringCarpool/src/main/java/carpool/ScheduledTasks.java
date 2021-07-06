package carpool;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import carpool.services.TripService;

@Component
public class ScheduledTasks {
	
	@Autowired
	private TripService viaggioService;
	
	//Questo è un metodo 'scheduled', si ripete da solo ogni tot tempo
	//Imposto 10 minuti come tempo per ripetere periodicamente il metodo
	@Scheduled(fixedRate = 60000)
	public void deleteViaggiScaduti() {
		//Prendo la data di oggi
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date yesterday = cal.getTime();
		viaggioService.deleteTripsBeforeDate(yesterday);
	}
}
