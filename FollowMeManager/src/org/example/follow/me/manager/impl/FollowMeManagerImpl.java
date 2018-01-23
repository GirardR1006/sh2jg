package org.example.follow.me.manager.impl;

import java.util.Map;

import org.apache.felix.ipojo.annotations.Requires;
import org.example.follow.me.manager.EnergyGoal;
import org.example.follow.me.manager.FollowMeAdministration;
import org.example.follow.me.manager.IlluminanceGoal;

import com.example.binary.follow.me.configuration.FollowMeConfiguration;

@SuppressWarnings("rawtypes") 
public class FollowMeManagerImpl implements FollowMeAdministration {

	// Declare a dependency to a FollowMeConfiguration service
    private FollowMeConfiguration followMeConfiguration;
	
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
		followMeConfiguration.setMaximumNumberOfLightsToTurnOn(illuminanceGoal.getNumberOfLightsToTurnOn());
		followMeConfiguration.setTargetedIlluminance(illuminanceGoal.getTargetedIlluminance());
	}

	@Override
	public IlluminanceGoal getIlluminancePreference() {
		int numberLights = followMeConfiguration.getMaximumNumberOfLightsToTurnOn();
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
