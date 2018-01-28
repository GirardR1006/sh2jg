package org.example.follow.me.manager.command;
     
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.example.follow.me.manager.EnergyGoal;
import org.example.follow.me.manager.FollowMeAdministration;
import org.example.follow.me.manager.IlluminanceGoal;
import org.example.time.MomentOfTheDay;
import org.example.time.MomentOfTheDayService;


import fr.liglab.adele.icasa.command.handler.Command;
import fr.liglab.adele.icasa.command.handler.CommandProvider;
import fr.liglab.adele.icasa.service.location.PersonLocationService;
import fr.liglab.adele.icasa.service.preferences.Preferences;
     
    //Define this class as an implementation of a component :
    @Component
    //Create an instance of the component
    @Instantiate(name = "follow.me.manager.command")
    //Use the handler command and declare the command as a command provider. The
    //namespace is used to prevent name collision.
    @CommandProvider(namespace = "followme")
    public class FollowMeManagerCommandImpl {
     
        // Declare a dependency to a FollowMeAdministration service
        @Requires
        private FollowMeAdministration m_administrationService;
        // Declare a dependency to a preference service
        @Requires
        private Preferences preferencesService;
        @Requires
    	// Declare a dependency to a PersonLocation service
        private PersonLocationService personLocationService;
        @Requires
    	// Declare a dependency to a MomentOfDay service
        private MomentOfTheDayService momentOfDayService;
     
        /**
         * Felix shell command implementation to sets the illuminance preference.
         *
         * @param   user the name of the user you want to set preference
         * 			goal the new illuminance preference ("SOFT", "MEDIUM", "FULL")
         */
        
        /**User preferences for illuminance**/
        public static final String USER_PROP_ILLUMINANCE = "illuminance";
        public static final String USER_PROP_ILLUMINANCE_VALUE_SOFT = "SOFT";
        public static final String USER_PROP_ILLUMINANCE_VALUE_MEDIUM = "MEDIUM";
        public static final String USER_PROP_ILLUMINANCE_VALUE_FULL = "FULL";
     
        // Each command should start with a @Command annotation
        
        /**
         * Command that sets the preference for the user
         * @param goal
         * @param user
         */
        @Command
        public void setIlluminancePreference(String goal, String user) {
            // The targeted goal
            IlluminanceGoal illuminanceGoal=IlluminanceGoal.FULL;
            String userIlluminance = USER_PROP_ILLUMINANCE_VALUE_FULL;
            // Here you have to convert the goal string into an user preference
            // goal and fail if the entry is not "SOFT", "MEDIUM" or "HIGH".
            //Else, return a global illuminance goal
            if (goal.equals("MEDIUM")){
           		illuminanceGoal= IlluminanceGoal.MEDIUM;
           		userIlluminance=USER_PROP_ILLUMINANCE_VALUE_MEDIUM;
           	}
           	else if (goal.equals("SOFT")){
           		illuminanceGoal= IlluminanceGoal.SOFT;
           		userIlluminance=USER_PROP_ILLUMINANCE_VALUE_SOFT;
           	}
           	else if (goal.equals("FULL")){
           		illuminanceGoal= IlluminanceGoal.FULL;
           		userIlluminance=USER_PROP_ILLUMINANCE_VALUE_FULL;
           	}
           	else{
           		System.out.println("Error, enter SOFT, FULL or MEDIUM.");
           	}
            //save the user's preferences
            preferencesService.setUserPropertyValue(user, USER_PROP_ILLUMINANCE, userIlluminance);
            
            //set the values to follow either the default mode or the user mode (only one user)
            List<String> listOfZones = new ArrayList<String>();
    	    listOfZones.add("bathroom");
    	    listOfZones.add("kitchen");
    	    listOfZones.add("livingroom");
    	    listOfZones.add("bedroom");
    	   
    	    Set<String> listOfPersonsHere = new HashSet<String>() ;
    	    for (String thisZone : listOfZones){
    	    	
    	    	listOfPersonsHere.addAll(personLocationService.getPersonInZone(thisZone));
    	   
    	    }
    	    
    	    if((listOfPersonsHere.size()==0) || (listOfPersonsHere.size()>1)){
    	    	//default as no preference : soft

    	    	illuminanceGoal = IlluminanceGoal.SOFT;
    	    }
    	    else{
    	    	//get goal of only user
    	    	Iterator<String> userIterator = listOfPersonsHere.iterator();
    	    	String userString = userIterator.next();
    	    	String userPref = (String) preferencesService.getUserPropertyValue(userString, USER_PROP_ILLUMINANCE);
    	    	if(userPref.equals("SOFT")){
    	    		illuminanceGoal=IlluminanceGoal.SOFT;
    	    	}
    	    	else if (userPref.equals("MEDIUM")){
    	    		illuminanceGoal=IlluminanceGoal.MEDIUM;
    	    	}
    	    	else if (userPref.equals("FULL")){
    	    		illuminanceGoal=IlluminanceGoal.FULL;
    	    	}
    	    	else{
    	    		//default if no information
    	    		illuminanceGoal=IlluminanceGoal.SOFT;
    	    	}
    	    }
    	    
            m_administrationService.setIlluminancePreference(illuminanceGoal);
            
        }
     
        @Command
        public void getIlluminancePreference(){
            // implement the command that print the current value of the goal
            System.out.println("The illuminance goal is "); 
            IlluminanceGoal illumGoal = m_administrationService.getIlluminancePreference();
            //to update the administration service
            //m_administrationService.setIlluminancePreference(illumGoal);
            if(illumGoal.equals(IlluminanceGoal.FULL)){
            	System.out.println("FULL");
            }
            else if(illumGoal.equals(IlluminanceGoal.MEDIUM)){
            	System.out.println("MEDIUM");
            }
            else if(illumGoal.equals(IlluminanceGoal.SOFT)){
            	System.out.println("SOFT");
            }
            else{
            	System.out.println("Error");
            }
        }
        
        /**
         * Felix shell command implementation to sets the power consumption preference.
         *
         * @param goal the new power consumption preference ("LOW", "MEDIUM", "HIGH")
         */
        @Command
        public void setPowerConsumptionPreference(String goal) {
            EnergyGoal energyGoal=EnergyGoal.HIGH;
            if (goal.equals("MEDIUM")){
            	energyGoal= EnergyGoal.MEDIUM;
            }
            else if (goal.equals("HIGH")){
            	energyGoal= energyGoal.HIGH;
            }
            else if (goal.equals("LOW")){
            	energyGoal= energyGoal.LOW;
            }
            else{
            	System.out.println("Error, enter HIGH, MEDIUM or LOW");
            }
           
            //call the administration service to configure it :
            m_administrationService.setEnergySavingGoal(energyGoal);;
        }
     
        @Command
        public void getPowerConsumptionPreference(){
            // implement the command that print the current value of the goal
            System.out.println("The illuminance goal is "); 
            EnergyGoal energGoal = m_administrationService.getEnergyGoal();
            if(energGoal.equals(EnergyGoal.HIGH)){
            	System.out.println("HIGH");
            }
            else if(energGoal.equals(EnergyGoal.MEDIUM)){
            	System.out.println("MEDIUM");
            }
            else if(energGoal.equals(EnergyGoal.LOW)){
            	System.out.println("LOW");
            }
            else{
            	System.out.println("Error");
            }
        }
        
        @Command
        public void getMomentOfDay(){
            // implement the command that print the current moment of day
            System.out.println("The current moment of day is "); 
            MomentOfTheDay now = momentOfDayService.getMomentOfTheDay();
            if(now.equals(MomentOfTheDay.NIGHT)){
            	System.out.println("NIGHT");
            }
            else if(now.equals(MomentOfTheDay.MORNING)){
            	System.out.println("MORNING");
            }
            else if(now.equals(MomentOfTheDay.AFTERNOON)){
            	System.out.println("AFTERNOON");
            }
            else{
            	System.out.println("Error");
            }
        }
     
     
    }
