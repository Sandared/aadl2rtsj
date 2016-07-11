package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.Connection;
import org.osate.aadl2.DataImplementation;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.Element;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.RangeValue;
import org.osate.aadl2.RecordValue;
import org.osate.aadl2.impl.ComponentImplementationImpl;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.ConnectionInstanceEnd;
import org.osate.aadl2.instance.ConnectionReference;
import org.osate.aadl2.instance.FeatureCategory;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;

import util.OffsetTime;
import util.UtilFactory;

public class ComponentInstanceHelper {
	
	private static final Logger log = Logger.getLogger(ComponentInstanceHelper.class.getName());
	
	public static List<OffsetTime> getTimes(FeatureInstance feature, ConnectionInstance connection, String IOReferenceTime, Boolean isInput){
		OffsetTime time = null;
		List<OffsetTime> times = new ArrayList<OffsetTime>();
		
		// if immediate/delayed connections are present, then (partially) ignore Input/Output_Time
		if(connection != null){
			time = getTimeForConnection(feature, connection, isInput);
			// special case: if there's a connection timing then this overwrites all other possible timings
			if(time != null){
				times.add(time);
				return times;
			}
		}
		
		// if NOT immediate/delayed consider Time part of Input/Output_Time and IGNORE offset
		if(time == null)
			times = getTimeForReferenceTime(feature, IOReferenceTime, false, isInput, connection);
        
		if(times.size() == 0){
    	   // if there has been no Input/Output_Time on the feature, check for Input/Output_Time on thread, see AADL Standard 8.3.2 (18)
    	   ComponentInstance component = feature.getContainingComponentInstance();
    	   times = getTimeForReferenceTime(component, IOReferenceTime, false, isInput, connection);
        }
		
		if(times.size() == 0)
			if(isInput){
				//if time is still null, then default is Dispatch for Input, see AADL Standard 8.3.2 (17)). 
				OffsetTime ot = UtilFactory.eINSTANCE.createOffsetTime();
				ot.setMs(0);
				ot.setNs(0);
				ot.setUniqueId(System.identityHashCode(feature));
				ot.setIoTime(Communication_Properties_IO_Reference_Time_Dispatch);
				ot.setConnection(connection);
				times.add(ot);
			}
			else{
				//if time is still null, then default is Completion for Output, see AADL Standard 8.3.2 (25)
				OffsetTime ot = UtilFactory.eINSTANCE.createOffsetTime();
				ot.setMs(0);
				ot.setNs(0);
				ot.setUniqueId(System.identityHashCode(feature));
				ot.setIoTime(Communication_Properties_IO_Reference_Time_Completion);
				ot.setConnection(connection);
				times.add(ot);
			}
		return times;
	}
	
	private static OffsetTime getTimeForConnection(NamedElement element, ConnectionInstance connection, boolean isInput){
		OffsetTime time = null;
		String timing = PropertyHelper.getTiming(connection); // sampled, immediate, delayed
		if(timing.equals(Communication_Properties_Timing_Immediate))
			if(isInput){
				// see AADL Standard 9.2.5 (50)
				time = UtilFactory.eINSTANCE.createOffsetTime();
				time.setMs(0);
				time.setNs(0);
				time.setUniqueId(System.identityHashCode(connection));
				time.setIoTime(Communication_Properties_IO_Reference_Time_Start);
				time.setConnection(connection);
			}
			else{
				// if there is a single valued Output_Time, then take it, otherwise the time is assumed to be Completion, 
				// see AADL Standard 9.2.5 (50)
				List<OffsetTime> times = getTimeForReferenceTime(element, null, true, false, connection);
				if(times.size() == 0){
					time = UtilFactory.eINSTANCE.createOffsetTime();
					time.setMs(0);
					time.setNs(0);
					time.setUniqueId(System.identityHashCode(connection));
					time.setIoTime(Communication_Properties_IO_Reference_Time_Completion);
					time.setConnection(connection);
				}
			}
		if(timing.equals(Communication_Properties_Timing_Delayed))
			if(isInput){
				// see AADL Standard 9.2.5 (51)
				time = UtilFactory.eINSTANCE.createOffsetTime();
				time.setMs(0);
				time.setNs(0);
				time.setUniqueId(System.identityHashCode(connection));
				time.setIoTime(Communication_Properties_IO_Reference_Time_Dispatch);
				time.setConnection(connection);
			}
			else{
				// see AADL Standard 9.2.5 (51)
				time = UtilFactory.eINSTANCE.createOffsetTime();
				time.setMs(0);
				time.setNs(0);
				time.setUniqueId(System.identityHashCode(connection));
				time.setIoTime(Communication_Properties_IO_Reference_Time_Deadline);
				time.setConnection(connection);
			}
		// sampled has no special semantic meaning for input and output timing
		return time;
	}
	
	private static List<OffsetTime> getTimeForReferenceTime(NamedElement element, final String IOReferenceTime, boolean forceSingleValued, boolean isInput, ConnectionInstance connection){
		ArrayList<OffsetTime> times = new ArrayList<OffsetTime>();
		List<PropertyExpression> timeProperties = null;
		if(isInput)
			timeProperties = element.getPropertyValues(Communication_Properties, Communication_Properties_Input_Time);
		else
			timeProperties = element.getPropertyValues(Communication_Properties, Communication_Properties_Output_Time);
		
		OffsetTime time = null;
		
        if(timeProperties.size() > 0){
        	if(forceSingleValued)// needed for the special case, when this method is called during connection-specific timing determination
        		if(timeProperties.size() != 1)
        			return new ArrayList<OffsetTime>();//empty list
     	   // Input/Output_Time might be a list, so Input can be frozen multiple times during a dispatch, see AADL Standard 8.3.2 (20)
     	   for (PropertyExpression timeProperty : timeProperties) {
     		   // Input/Output_Time consists of a Time Part, which is an EnumerationLiteral and an Offset Part, which is a RangeValue
     		   NamedValue timePart = (NamedValue)((RecordValue)timeProperty).getOwnedFieldValues().get(0).getOwnedValue();
     		   String ioTime = ((EnumerationLiteral)timePart.getNamedValue()).getName();
     		   RangeValue offsetPart = (RangeValue)((RecordValue)timeProperty).getOwnedFieldValues().get(1).getOwnedValue();
     		   long minimumMs = (long) offsetPart.getMinimumValue().getScaledValue(AADL_Project_Time_Units_Milli_Seconds);
     		   long minimumNs = (long) offsetPart.getMinimumValue().getScaledValue(AADL_Project_Time_Units_Nano_Seconds) % 1000000;
     		   time = UtilFactory.eINSTANCE.createOffsetTime();
			   time.setMs(minimumMs);
			   time.setNs(minimumNs);
			   time.setUniqueId(System.identityHashCode(timeProperty));
			   time.setIoTime(ioTime);
			   time.setConnection(connection);
     		   
     		   if(IOReferenceTime == null || time.getIoTime().equals(IOReferenceTime)){
     			  times.add(time);
     		   }
     	   }
        }
        return times;
	}
	
	public static ConnectionInstance getImmediateConnection(ComponentInstance component){
		List<FeatureInstance> features = component.getAllFeatureInstances();
		for (FeatureInstance feature : features) {
			DirectionType direction = feature.getDirection();
			List<ConnectionInstance> connections = null;
			if(direction.incoming() && !direction.outgoing())
				connections = feature.getDstConnectionInstances();
			if(direction.outgoing() && !direction.incoming())
				connections = feature.getSrcConnectionInstances();
			for (ConnectionInstance connection : connections) {
				String timing = PropertyHelper.getTiming(connection);
				if(timing.equals(Communication_Properties_Timing_Immediate))
					return connection;
			}
		}
		return null;
	}
	
	public static List<OffsetTime> getTimes(FeatureInstance feature){
		EList<OffsetTime> times = new BasicEList<OffsetTime>();
		EList<ConnectionInstance> connections = null;
		boolean isInput = false;
		DirectionType direction = feature.getDirection();
		if(direction.incoming() && !direction.outgoing()){
			connections = feature.getDstConnectionInstances();
			isInput = true;
		}
		if(direction.outgoing() && !direction.incoming()){
			connections = feature.getSrcConnectionInstances();
			isInput = false;
		}
		if (connections == null) {
			System.out.println("no connections");
		}
		for (ConnectionInstance connection : connections) {
			times.addAll(getTimes(feature, connection, null, isInput)); // no referencetime is needed if we want all times for one feature
		}
		return times;
	}
	
	public static ComponentInstance getComponentInstance(ConnectionInstanceEnd conie){
		return conie.getComponentInstance();
	}
	
	public static List<FeatureInstance> getFeatures(ComponentInstance ci){
		return ci.getFeatureInstances();
	}
	
	public static ConnectionInstanceEnd getConnectionDestination(ConnectionInstance coni){
		return coni.getDestination();
	}
	
	public static List<ConnectionInstance> getDstConnectionInstances(FeatureInstance fi){
		return fi.getDstConnectionInstances();
	}
	
	public static List<ConnectionInstance> getSrcConnectionInstances(FeatureInstance fi){
		return fi.getSrcConnectionInstances();
	}
	
	public static ComponentClassifier getClassifier(FeatureInstance fi){
		return (ComponentClassifier) fi.getFeature().getClassifier();
	}
	
	public static ComponentClassifier getClassifier(ComponentInstance ci){
		return ci.getComponentClassifier();
	}

	
	// ##############################################################
	// DEFINITLY NEEDED
	// ##############################################################
	
	/**
	 * In order to create a valid Java package name for the given InstanceObject we take the</br>
	 * element.getInstanceObjectPath() which is always in form of 'System_Type_Name' + 'System_Implementation_Name' + '_Instance' + '.path.within.systemtree'</br>
	 * The '_Instance' part gets removed and the rest of this path is returned as is.</br>
	 * If isUserCode is <code>true</code>, then the packagename is prepended with 'user.', otherwise with 'instance.'
	 * @param element the instance one wants the package name for
	 * @param isUserCode shall the class be usercode or framework code
	 * @return a unique, valid Java package name for the given InstanceObject
	 */
	public static String getPackageName(InstanceObject element, Boolean isUserCode){
		StringBuffer pkg = new StringBuffer();
		//TODO: this might change if we generate Data Elements, that have inner connections and are not only used as classifiers
		// special case: Data- InstanceObject: we need just the classifier package 
		if(element instanceof ComponentInstance && ((ComponentInstance)element).getCategory().equals(ComponentCategory.DATA)){
			ComponentClassifier cc = ((ComponentInstance)element).getComponentClassifier();
			return ComponentClassifierHelper.getPackageName(cc);
		}
		else{
			if (isUserCode) 
				pkg.append("user.");
			else
				pkg.append("instance.");
			pkg.append(getHierarchyName(element));
		}
		return pkg.toString().toLowerCase();
	}
	
	private static String getHierarchyName(InstanceObject object){
		StringBuffer buffer = new StringBuffer(object.getInstanceObjectPath()); // returns a String in form of <System_Type_Name><System_Implementation_Name>_Instance.<path>.<within>.<systemtree>
		int pkgEnd = buffer.indexOf(".");// find the first occurence of '.'
		// if the buffer is already the <System_Type_Name><System_Implementation_Name> part
		if(pkgEnd == -1)
			pkgEnd = buffer.length();
		StringBuffer pkg = new StringBuffer(buffer.substring(0, pkgEnd));//get the part <System_Type_Name><System_Implementation_Name>
		buffer.delete(0, pkgEnd); // remove the part <System_Type_Name><System_Implementation_Name>
		pkg = pkg.delete(pkg.indexOf("_Instance"), pkg.length());//delete the "_Instance" part
		buffer.insert(0, pkg);//insert <System_Type_Name><System_Implementation_Name> in front of the rest again
		return buffer.toString();
	}
	
	/**
	 * @param ne the NamedElement one wants a class name for
	 * @return ne.getName(). If ne is a Data Component, ComponentClassifierHelper.getClassName(ne.getClassifier()) is called.
	 */
	public static String getClassName(NamedElement ne){
		// Data is a special case, as its "class" is always determined by the classifier
		if(ne instanceof ComponentInstance && ((ComponentInstance)ne).getCategory().equals(ComponentCategory.DATA)){
			ComponentClassifier cc = ((ComponentInstance)ne).getComponentClassifier();
				return ComponentClassifierHelper.getClassName(cc);
		}
		return ne.getName();
	}
	
	/**
	 * @param ne NamedElement one wants a user class name for
	 * @return ne.getName() concatenated with 'UserClass'
	 */
	public static String getUserCodeClassName(NamedElement ne){
		return ne.getName() + "UserCode";
	}
	
	/**
	 * @param time OffsetTime one wants a handler class name for
	 * @return 'Handler_' concatenated with the time's unique ID
	 */	
	public static String getHandlerClassName(OffsetTime time){
		return "Handler_" + time.getUniqueId();
	}
	
	/**
	 * @param ci the ComponentInstance one wants the children for
	 * @return a list with all children of the given ci, like subcomponents, connections, etc.
	 */
	public static List<Element> getChildren(ComponentInstance ci){
		return ci.getChildren();
	}
}
