package de.uniaugsburg.smds.aadl2rtsj.generation.services.aadl;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.ConnectionInstanceEnd;
import org.osate.aadl2.instance.FeatureInstance;

public class AADLHelper {
	
	public static ComponentInstance getComponentInstance(ConnectionInstanceEnd conie){
		return conie.getComponentInstance();
	}
	
	public static EList<ComponentInstance> getSubcomponents(ComponentInstance ci){
		EList<ComponentInstance> subcomponents = ci.getAllComponentInstances();
		subcomponents.remove(0); // the first one is always the Component itself
		return subcomponents;
	}
	
	public static EList<FeatureInstance> getFeatures(ComponentInstance ci){
		return ci.getFeatureInstances();
	}
	
	public static ConnectionInstanceEnd getConnectionDestination(ConnectionInstance coni){
		return coni.getDestination();
	}
	
	public static EList<ConnectionInstance> getDstConnectionInstances(FeatureInstance fi){
		return fi.getDstConnectionInstances();
	}
	
	public static Classifier getClassifier(FeatureInstance fi){
		return fi.getFeature().getClassifier();
	}

}
