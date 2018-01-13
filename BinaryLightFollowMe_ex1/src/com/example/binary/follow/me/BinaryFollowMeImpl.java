package com.example.binary.follow.me;

import fr.liglab.adele.icasa.device.presence.PresenceSensor;
import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.light.BinaryLight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BinaryFollowMeImpl implements DeviceListener{

	/** Field for presenceSensors dependency */
	private PresenceSensor[] presenceSensors;
	/** Field for binaryLights dependency */
	private BinaryLight[] binaryLights;
	/** Field for max number of light allowed by room*/
	private int maxLightToTurnOnPerRoom = 1;

	/** Bind Method for binaryLights dependency */
	public void bindBinaryLight(BinaryLight binaryLight, Map properties) {
		binaryLight.addListener(this); 
		System.out.println("bind binary light " + binaryLight.getSerialNumber());	}

	/** Unbind Method for binaryLights dependency */
	public void unbindBinaryLight(BinaryLight binaryLight, Map properties) {
		binaryLight.removeListener(this);
		System.out.println("unbind binary light " + binaryLight.getSerialNumber());	}

	/** Bind Method for presenceSensors dependency */
	public synchronized void bindPresenceSensor(PresenceSensor presenceSensor, Map properties) {
		// Add the listener to the presence sensor
		presenceSensor.addListener(this); 
		System.out.println("bind presence sensor "+ presenceSensor.getSerialNumber());    }

	/** Unbind Method for presenceSensors dependency */
	public synchronized void unbindPresenceSensor(PresenceSensor presenceSensor, Map properties) {
		// Remove the listener from the presence sensor
		presenceSensor.removeListener(this);
		System.out.println("unbind presence sensor "+ presenceSensor.getSerialNumber());	}

	/** Component Lifecycle Method */
	public synchronized void stop() {
		for(PresenceSensor sensor : presenceSensors){
			sensor.removeListener(this);
		}
		System.out.println("Component is stopping...");	}
	
	/** Component Lifecycle Method */
	public void start() {
		   System.out.println("Component is starting...");	}
	
    /**
     * This method is part of the DeviceListener interface and is called when a
     * subscribed device property is modified.
     * 
     * @param device
     *            is the device whose property has been modified.
     * @param propertyName
     *            is the name of the modified property.
     * @param oldValue
     *            is the old value of the property
     * @param newValue
     *            is the new value of the property
     */

    /**
     * The name of the LOCATION property
     */
    public static final String LOCATION_PROPERTY_NAME = "Location";
     
    /**
     * The name of the location for unknown value
     */
    public static final String LOCATION_UNKNOWN = "unknown";
    public void devicePropertyModified(GenericDevice device,
        String propertyName, Object oldValue,Object newValue) {
    	
      if (device instanceof PresenceSensor) {
		  //based on the assumption we can cast the generic device without checking via instanceof
		  PresenceSensor changingSensor = (PresenceSensor) device;
		 
		  // check the change is related to presence sensing
		  if (propertyName.equals(PresenceSensor.PRESENCE_SENSOR_SENSED_PRESENCE)) {
			// get the location where the sensor is:
		    String detectorLocation = (String) changingSensor.getPropertyValue(LOCATION_PROPERTY_NAME);
		    System.out.println("The sensor with the serial number"
		                       + changingSensor.getSerialNumber()+" has changed");
		    System.out.println("This sensor has detected something in the room :" + detectorLocation);  
		    // if the location is known :
		    if (!detectorLocation.equals(LOCATION_UNKNOWN)) {
		    	// get the related binary lights
		    	List<BinaryLight> sameLocationLigths = getBinaryLightFromLocation(detectorLocation);
	    		int numberOfSwitchedOnLights = 0;
		    	for (BinaryLight binaryLight : sameLocationLigths) {
		    		if (binaryLight.getPowerStatus()) {numberOfSwitchedOnLights ++;}
		    		// and switch them on/off depending on the sensed presence and the number of lights
		    		// already switched on
			    		if(changingSensor.getSensedPresence()){
			    			if (numberOfSwitchedOnLights < maxLightToTurnOnPerRoom) {
				    			binaryLight.turnOn();
				    			numberOfSwitchedOnLights ++;
			    			}
			    		}
		    		else{
		    			binaryLight.turnOff();
		    			numberOfSwitchedOnLights --;
		    		}
		    	}
		    }
		  }
      }
      if (device instanceof BinaryLight) {
		  BinaryLight changingLight = (BinaryLight) device;
		  //when the light will change location, its state will depend of the new room's sensor state 
		  if (propertyName.equals(LOCATION_PROPERTY_NAME)) {
			  String lightLocation = (String) changingLight.getPropertyValue(LOCATION_PROPERTY_NAME);
			  System.out.println("The light with serial number" + changingLight.getSerialNumber()+" has changed");
			  System.out.println("The light is now in the room: " + lightLocation);
			  if (!lightLocation.equals(LOCATION_UNKNOWN)) {
				  //get all sensors in the new room
				  List<PresenceSensor> sameLocationSensors = getSensorFromLocation(lightLocation);
				  for (PresenceSensor presenceSensor : sameLocationSensors) {
			    		// and switch the light on or off depending if the sensors is detecting a presence
					  	//TODO: check also if there are other lights already lit
			    		if(presenceSensor.getSensedPresence()){
			    			changingLight.turnOn();
			    		}
			    		else{
			    			changingLight.turnOff();
			    		}
				  }
			  }
		  }
      }
    }
     
    /**
     * Return all BinaryLight from the given location
     * 
     * @param location
     *            : the given location
     * @return the list of matching BinaryLights
     */
    private synchronized List<BinaryLight> getBinaryLightFromLocation(
        String location) {
      List<BinaryLight> binaryLightsLocation = new ArrayList<BinaryLight>();
      for (BinaryLight binLight : binaryLights) {
        if (binLight.getPropertyValue(LOCATION_PROPERTY_NAME).equals(
            location)) {
          binaryLightsLocation.add(binLight);
        }
      }
      return binaryLightsLocation;
    }
    private synchronized List<PresenceSensor> getSensorFromLocation(
            String location) {
          List<PresenceSensor> sensorsLocation = new ArrayList<PresenceSensor>();
          for (PresenceSensor pSen : presenceSensors) {
            if (pSen.getPropertyValue(LOCATION_PROPERTY_NAME).equals(
                location)) {
              sensorsLocation.add(pSen);
            }
          }
          return sensorsLocation;
        }

	@Override
	public void deviceAdded(GenericDevice arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deviceEvent(GenericDevice arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void devicePropertyAdded(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deviceRemoved(GenericDevice arg0) {
		// TODO Auto-generated method stub
		
	}

}
