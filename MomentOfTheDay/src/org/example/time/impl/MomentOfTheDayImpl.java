package org.example.time.impl;

import java.util.concurrent.TimeUnit;

import fr.liglab.adele.icasa.service.scheduler.PeriodicRunnable;
import fr.liglab.adele.icasa.clockservice.Clock;

import org.example.time.MomentOfTheDay;
import org.example.time.MomentOfTheDayListener;
import org.example.time.MomentOfTheDayService;

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
    // WARNING: iCasa takes a factor to speed up its elapsed time
    // This only works with "Factor" in iCasa GUI manager equals 3600
    public MomentOfTheDay getMomentOfTheDay(){
    	int currentHour = convertMilliInHours(clock.getElapsedTime());
    	System.out.println(currentHour);    	
    	System.out.println(clock.getElapsedTime());
    	return(MomentOfTheDay.getCorrespondingMoment(currentHour));
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
    	//int currentHour = convertMilliInHours(clock.currentTimeMillis());
    	//MomentOfTheDay now = currentMomentOfTheDay.getCorrespondingMoment(currentHour); 
    	//if(now.compareTo(currentMomentOfTheDay)!=0) {
    	//	currentMomentOfTheDay=now;
    	//}
 
    }
    protected int convertMilliInHours(double milli) {
    	int oneSecondInMillis = 1000;
    	int oneHourInSeconds=3600;
    	return (int) (milli/(oneSecondInMillis*oneHourInSeconds)%24); //modulo
    }


	//@Override
	//public void register(MomentOfTheDayListener listener) {
		// TODO Auto-generated method stub
		
	//}

	//@Override
	//public void unregister(MomentOfTheDayListener listener) {
		// TODO Auto-generated method stub
		
	//}
}