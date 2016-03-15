package de.uniaugsburg.smds.aadl2rtsj.generation.services.features.ports;

import org.osate.aadl2.instance.FeatureCategory;
import org.osate.aadl2.instance.FeatureInstance;

public class PortHelper {

	public static boolean isDataPort(FeatureInstance fi){
		return fi.getCategory() == FeatureCategory.DATA_PORT;
	}
	
	public static boolean isIncoming(FeatureInstance fi){
		return fi.getDirection().incoming();
	}
	
	public static boolean isOutgoing(FeatureInstance fi){
		return fi.getDirection().outgoing();
	}
}
