package org.example.time;

import java.util.concurrent.TimeUnit;

import fr.liglab.adele.icasa.service.scheduler.PeriodicRunnable;

public class MomentOfTheDayImpl implements MomentOfTheDayService, PeriodicRunnable{
    
    /**
    * The current moment of the day :
    **/
    MomentOfTheDay currentMomentOfTheDay;
 
    // Implementation of the MomentOfTheDayService ....
 
    public MomentOfTheDay getMomentOfTheDay(){
		return null;
 
    }
 
    // Implementation ot the PeriodicRunnable ...
    // The service will be periodically called every hour.
    
    public long getPeriod(){
        return 1;
    }
 
    public TimeUnit getUnit() {
        return TimeUnit.HOURS;
    }
    
    public void run() {
        // TODO: J'ai pas compris ici ! The method run is called on a regular basis
    	currentMomentOfTheDay.compareTo(getMomentOfTheDay());
    	
    	
    	//currentMomentOfTheDay.getCorrespondingMoment(hour)		
    			// TODO : do something to check the current time of the day and see if
        // it has changed
 
    }
 
}