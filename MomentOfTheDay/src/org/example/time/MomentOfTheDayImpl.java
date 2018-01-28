package org.example.time;

import java.util.concurrent.TimeUnit;

import fr.liglab.adele.icasa.service.scheduler.PeriodicRunnable;
import fr.liglab.adele.icasa.clockservice.Clock;

import org.example.time.MomentOfTheDay;

public class MomentOfTheDayImpl implements MomentOfTheDayService, PeriodicRunnable{
    
    /**
    * The current moment of the day :
    **/
    MomentOfTheDay currentMomentOfTheDay;
    /**
     * The clock of iCasa Service :
     **/
    Clock clock;
    // Implementation of the MomentOfTheDayService ....
 
    public MomentOfTheDay getMomentOfTheDay(){
		return currentMomentOfTheDay;
 
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
        //The method run is called on a regular basis
    	int currentHour = convertMilliInHours(clock.currentTimeMillis());
    	MomentOfTheDay now = currentMomentOfTheDay.getCorrespondingMoment(currentHour); 
    	if(now.compareTo(currentMomentOfTheDay)!=0) {
    		currentMomentOfTheDay=now;
    	}
 
    }
    private int convertMilliInHours(double milli) {
    	int oneSecondInMillis = 1000;
    	int oneHourInSeconds=3600;
    	int oneDayInHours = 23;
    	return (int) (milli/(oneSecondInMillis*oneHourInSeconds*oneDayInHours)%24); //modulo
    }
}