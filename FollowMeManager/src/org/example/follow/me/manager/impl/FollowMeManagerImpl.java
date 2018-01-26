package org.example.follow.me.manager.impl;

import java.util.Map;

import org.apache.felix.ipojo.annotations.Requires;
import org.example.follow.me.manager.EnergyGoal;
import org.example.follow.me.manager.FollowMeAdministration;
import org.example.follow.me.manager.IlluminanceGoal;

import com.example.binary.follow.me.configuration.FollowMeConfiguration;

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
	    String julienIlm = (String) preferencesService.getUserPropertyValue("Julien", USER_PROP_ILLUMINANCE);

		IlluminanceGoal goal = IlluminanceGoal.SOFT;
		if(julienIlm.equals(USER_PROP_ILLUMINANCE_VALUE_SOFT)){
			goal = IlluminanceGoal.SOFT;
		}
		else if(julienIlm.equals(USER_PROP_ILLUMINANCE_VALUE_MEDIUM)){
			goal = IlluminanceGoal.MEDIUM;
		}
		else if(julienIlm.equals(USER_PROP_ILLUMINANCE_VALUE_FULL)){
			goal = IlluminanceGoal.FULL;
		}
		else {
			if(numberLights<=1){
				goal = IlluminanceGoal.SOFT;
			}
			else if(numberLights==2){
				goal = IlluminanceGoal.MEDIUM;
			}
			else if(numberLights>2) {
				goal = IlluminanceGoal.MEDIUM;
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
