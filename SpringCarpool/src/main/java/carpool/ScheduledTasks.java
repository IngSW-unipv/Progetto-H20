package carpool;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import carpool.repos.ViaggioRepository;

@Component
public class ScheduledTasks {
	
	@Autowired
	private ViaggioRepository viaggioRepo;
	 
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
}