package org.example.follow.me.manager.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.felix.ipojo.annotations.Requires;
import org.example.follow.me.manager.EnergyGoal;
import org.example.follow.me.manager.FollowMeAdministration;
import org.example.follow.me.manager.IlluminanceGoal;

import com.example.binary.follow.me.configuration.FollowMeConfiguration;

import fr.liglab.adele.icasa.location.Zone;
import fr.liglab.adele.icasa.location.util.ZoneTracker;
import fr.liglab.adele.icasa.location.util.ZoneTrackerCustomizer;
import fr.liglab.adele.icasa.service.location.PersonLocationService;
import fr.liglab.adele.icasa.service.preferences.Preferences;

@SuppressWarnings("rawtypes") 
public class FollowMeManagerImpl implements FollowMeAdministration {
	
    /**User preferences for illuminance**/
    public static final String USER_PROP_ILLUMINANCE = "illuminance";
    public static final String USER_PROP_ILLUMINANCE_VALUE_SOFT = "SOFT";
    public static final String USER_PROP_ILLUMINANCE_VALUE_MEDIUM = "MEDIUM";
    public static final String USER_PROP_ILLUMINANCE_VALUE_FULL = "FULL";

	// Declare a dependency to a FollowMeConfiguration service
    private FollowMeConfiguration followMeConfiguration;
	// Declare a dependency to a Preferences service
    private Preferences preferencesService;
	// Declare a dependency to a PersonLocation service
    private PersonLocationService personLocationService;
    
    
    
	public void bindFollowMeConfiguration(FollowMeConfiguration config, Map properties) {
        System.out.println("New Configuration");
    }
	public void unbindFollowMeConfiguration(FollowMeConfiguration config, Map properties) {
        System.out.println("Stopping configuration");
    }
	public void bindPreferencesService(Preferences config, Map properties) {
        System.out.println("New preferences");
    }
	public void unbindPreferencesService(Preferences config, Map properties) {
        System.out.println("Stopping preferences");
    }	
	public void bindPersonLocationService(PersonLocationService config, Map properties) {
        System.out.println("New person location service");
    }
	public void unbindPersonLocationService(PersonLocationService config, Map properties) {
        System.out.println("Stopping person location service");
    }	
	
	@Override
	public void setIlluminancePreference(IlluminanceGoal illuminanceGoal) {
		
		followMeConfiguration.setMaximumNumberOfLightsToTurnOn(illuminanceGoal.getNumberOfLightsToTurnOn());
		followMeConfiguration.setTargetedIlluminance(illuminanceGoal.getTargetedIlluminance());
	}
	//TODO: Get illuminance preference of user, rather than a general one
        //Need to check who is in the flat, if empty choose default
	@Override
	public IlluminanceGoal getIlluminancePreference() {
		int numberLights = followMeConfiguration.getMaximumNumberOfLightsToTurnOn();
		
	    //String julienIlm = (String) preferencesService.getUserPropertyValue("Julien", USER_PROP_ILLUMINANCE);
	   // String paulIlm = (String) preferencesService.getUserPropertyValue("Paul", USER_PROP_ILLUMINANCE);
	    List<String> listOfZones = new ArrayList<String>();
	    listOfZones.add("bathroom");
	    listOfZones.add("kitchen");
	    listOfZones.add("livingroom");
	    listOfZones.add("bedroom");
	   
	    Set<String> listOfPersonsHere = new HashSet<String>() ;
	    for (String thisZone : listOfZones){
	    	
	    	listOfPersonsHere.addAll(personLocationService.getPersonInZone(thisZone));
	   
	    }
	    
	    IlluminanceGoal goal = IlluminanceGoal.SOFT;
	   
	    if((listOfPersonsHere.size()==0) || (listOfPersonsHere.size()>1)){
	    	//default as no preference : soft
	    	
	    	//if(numberLights<=1){
	    		goal = IlluminanceGoal.SOFT;
	    	//}
	    	//else if(numberLights==2){
	    		//goal = IlluminanceGoal.MEDIUM;
	    	//}
	    	//else if(numberLights>2) {
	    		//goal = IlluminanceGoal.FULL;
	    	//}
	    }
	    else{
	    	//only one user in the room : return its preferences
	    	Iterator<String> userIterator = listOfPersonsHere.iterator();
	    	String user = userIterator.next();
	    	String userPref = (String) preferencesService.getUserPropertyValue(user, USER_PROP_ILLUMINANCE);
	    	if(userPref.equals("SOFT")){
	    		goal=IlluminanceGoal.SOFT;
	    	}
	    	else if (userPref.equals("MEDIUM")){
	    		goal=IlluminanceGoal.MEDIUM;
	    	}
	    	else if (userPref.equals("FULL")){
	    		goal=IlluminanceGoal.FULL;
	    	}
	    	else{
	    		System.out.println("Error user preferences");
	    		goal=IlluminanceGoal.SOFT;
	    	}
	    }
	    return goal;
			
	}
	
	

	@Override
	public void setEnergySavingGoal(EnergyGoal energyGoal) {
		followMeConfiguration.setMaximumAllowedEnergyInRoom(energyGoal.getMaximumEnergyInRoom());
	}

	@Override
	public EnergyGoal getEnergyGoal() {
		double energyConsumption = followMeConfiguration.getMaximumAllowedEnergyInRoom();
		EnergyGoal goal = EnergyGoal.LOW;
		if(energyConsumption<=100d){
			goal = EnergyGoal.LOW;
		}
		else if(energyConsumption<=200d){
			goal = EnergyGoal.MEDIUM;
		}
		else{
			goal = EnergyGoal.HIGH;
		}
		return goal;
	}
}
