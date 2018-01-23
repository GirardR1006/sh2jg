package org.example.follow.me.manager.impl;

import java.util.Map;

import org.apache.felix.ipojo.annotations.Requires;
import org.example.follow.me.manager.FollowMeAdministration;
import org.example.follow.me.manager.IlluminanceGoal;

import com.example.binary.follow.me.configuration.FollowMeConfiguration;

@SuppressWarnings("rawtypes") 
public class FollowMeManagerImpl implements FollowMeAdministration {

	// Declare a dependency to a FollowMeConfiguration service
    
    private FollowMeConfiguration config;
	
	public void bindFollowMeConfiguration(FollowMeConfiguration config, Map properties) {
        System.out.println("New Configuration");
    }
 
	public void unbindFollowMeConfiguration(FollowMeConfiguration config, Map properties) {
        System.out.println("Stopping configuration");
    }
	
	/** TODO write a manager so that the number of lights is adjusted depending on a targeted goal
	 * 
	 */
	
	@Override
	public void setIlluminancePreference(IlluminanceGoal illuminanceGoal) {
			
			config.setMaximumNumberOfLightsToTurnOn(illuminanceGoal.getNumberOfLightsToTurnOn());
		
	}

	@Override
	public IlluminanceGoal getIlluminancePreference() {
		int numberLights = config.getMaximumNumberOfLightsToTurnOn();
		IlluminanceGoal goal = IlluminanceGoal.SOFT;
		if(numberLights<=1){
			goal = IlluminanceGoal.SOFT;
		}
		else if(numberLights == 2){
			goal = IlluminanceGoal.MEDIUM;
		}
		else{
			goal = IlluminanceGoal.FULL;
		}
		return goal;
	}
}
