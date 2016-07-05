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
	
	// TODO refactor if we still need this for connections
	public static String getClassName(InstanceObject io){
		NamedElement object = io; // need the broader reference for stuff like connections
		if(object instanceof ConnectionInstance)
			object = getConnection((ConnectionInstance) object);
		// Data is a special case, as its "class" is always determined by the classifier
		if(object instanceof ComponentInstance){
			ComponentInstance component = (ComponentInstance)object;
			if(component.getCategory().equals(ComponentCategory.DATA)){
				object = component.getComponentClassifier(); // we assume, that it is a DataImplementation, not a DataType
				// if it's a DataType the aadl model was not specific enough and this will result in an error
				//TODO: log warning?
			}
		}
		StringBuilder b = new StringBuilder(object.getName());
		if(object instanceof ComponentImplementation){
			//replace the ".xxx" part with "Xxx"
			int dotIndex = b.lastIndexOf(".");
			if(dotIndex != -1){
				b.deleteCharAt(dotIndex);
				b.replace(dotIndex, dotIndex + 1, b.substring(dotIndex, dotIndex + 1).toUpperCase());
			}
		}
		
		//generally we replace "_xxx" with "Xxx"
		int index;
		while((index = b.indexOf("_")) != -1){
			b.replace(index, index+2, b.substring(index+1, index+2).toUpperCase());
		}
		b.replace(0, 1, b.substring(0,1).toUpperCase());
		return b.toString();
	}
	
	
	
	public static String getObjectName(NamedElement object){
		if(object instanceof ConnectionInstance)//
			object = getConnection((ConnectionInstance) object);
		return object.getName();
	}
	
	public static String getPackageName(InstanceObject element){
		StringBuffer pkg = new StringBuffer();
		
		// special case: Data- InstanceObject: we need just the data package 
		if(element instanceof ComponentInstance && ((ComponentInstance)element).getCategory().equals(ComponentCategory.DATA)){
			ComponentClassifier cc = ((ComponentInstance)element).getComponentClassifier();
			return ComponentClassifierHelper.getPackageName(cc);
		}
		else{
			//in case of an instanceobject we need its path within the model
			pkg.append("instance.");
			pkg.append(getHierarchyName((InstanceObject) element));
		}
		
		return pkg.toString().toLowerCase();
	}
	
	private static String getHierarchyName(InstanceObject object){
		StringBuffer buffer = new StringBuffer(object.getInstanceObjectPath());
		// if its a feature instance we have to omit the last part of the path as we want the feature in the same package as its parent component
		if(object instanceof FeatureInstance){
			buffer.delete(buffer.lastIndexOf("."), buffer.length());
		}
		//if it is a ConnectionInstance we have to omit the name part of the path, as it is not the name, but xxx.port -> yyy.port
		if(object instanceof ConnectionInstance){
			buffer.delete(buffer.indexOf(object.getName())-1, buffer.length()); // -1 because otherwise we would have an extra dot at the end
		}
		
		int pkgEnd = buffer.indexOf(".");
		// if the buffer is already the package
		if(pkgEnd == -1)
			pkgEnd = buffer.length();
		StringBuffer pkg = new StringBuffer(buffer.substring(0, pkgEnd));//get the part that represents the package
		buffer.delete(0, pkgEnd); // remove the part that represents the package
		
		int implPos;
		if((implPos = pkg.indexOf("_impl_")) != -1){//if the package part contains an _impl_section start deletion here
			pkg = pkg.delete(implPos, pkg.length());
		}
		else{//else start deletion at the _Instance position
			pkg = pkg.delete(pkg.indexOf("_Instance"), pkg.length());
		}
		buffer.insert(0, pkg);//insert package in front of the rest again
		return buffer.toString();
	}
	
	public static String getHandlerClassName(OffsetTime time){
		return "Handler_" + time.getUniqueId();
	}
	
	public static String getDataType(FeatureInstance feature){
		ComponentClassifier classifier = (ComponentClassifier) feature.getFeature().getClassifier();
		//if no classifier is given, then return default type object
		if(classifier == null)
			return "Object";
		
		String type = null;
		type = getBaseType(classifier);
		
		//if it is not a base type, then return the class name of this datatype
		if(type == null)
			type = ComponentClassifierHelper.getClassName(classifier);
		
		return type;
	}
	
	private static String getBaseType(ComponentClassifier classifier){
		String type = null;
		// determine if it is one of the predefined AADL Base_Types
		if(ComponentClassifierHelper.isBaseType(classifier)){
			Classifier currentSuperType = classifier;
			Classifier superType = classifier;
			while(currentSuperType != null){
				// go up the inheritance tree if any
				superType = currentSuperType;
				currentSuperType = currentSuperType.getExtended();
			}
			type = superType.getName();
		}
		return type;
	}
	
	// if there are multiple connectionreferences, then the one upmost in the system hierarchy is returned
	public static Connection getConnection(ConnectionInstance connection){
		ConnectionReference upmost = connection.getConnectionReferences().get(0);
		for (ConnectionReference current: connection.getConnectionReferences()) {
			if(!current.equals(upmost)){
				ComponentInstance currentContext = current.getContext();
				ComponentInstance upmostContext = upmost.getContext();
				if(currentContext.getAllComponentInstances().contains(upmostContext))
					upmost = current;
				else
					break;
			}
		}
		return upmost.getConnection();
	}
	
//	public static String getSynchronisationObjectName(ConnectionInstance connection){
//		ConnectionInstanceEnd source = connection.getSource();
//		ConnectionInstanceEnd target = connection.getDestination();
//		return getObjectName(source) + "2" + getClassName(target) + "Sync";
//	}
	
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
	
	public static boolean isDataPort(FeatureInstance fi){
		return fi.getCategory() == FeatureCategory.DATA_PORT;
	}
	
	public static boolean isIncoming(FeatureInstance fi){
		return fi.getDirection().incoming();
	}
	
	public static boolean isOutgoing(FeatureInstance fi){
		return fi.getDirection().outgoing();
	}
	
	public static boolean isThread(ComponentInstance ci){
		return ci.getCategory() == ComponentCategory.THREAD;
	}
	
	public static boolean isProcess(ComponentInstance ci){
		return ci.getCategory() == ComponentCategory.PROCESS;
	}
	
	public static boolean isPeriodic(ComponentInstance ci){
		return PropertyHelper.getDispatchProtocol(ci).equals(Thread_Properties_Dispatch_Protocol_Periodic);
	}
	
	public static boolean isImmediate(ConnectionInstance connection){
		return PropertyHelper.getTiming(connection).equals(Communication_Properties_Timing_Immediate);
	}
	
	public static boolean isAtDeadline(OffsetTime time){
		return time.getIoTime().equals(Communication_Properties_IO_Reference_Time_Deadline);
	}
	
	public static ComponentInstance getComponentInstance(ConnectionInstanceEnd conie){
		return conie.getComponentInstance();
	}
	
	public static List<ComponentInstance> getSubcomponents(ComponentInstance ci){
		EList<ComponentInstance> subcomponents = ci.getAllComponentInstances();
		subcomponents.remove(0); // the first one is always the Component itself, thus no check is needed
		return subcomponents;
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
	
	// TODO is this used anywhere?
	public static Classifier getRealizedTypeClassifier(ComponentInstance ci){
		//we assume ci to be an implementation declaration, otherwise it's a type declaration that doesn't realize anything
		ComponentImplementationImpl ciClassifier = (ComponentImplementationImpl) getClassifier(ci);
		return ciClassifier.basicGetType();
	}
	
	public static List<ConnectionInstance> getConnectionInstances(ComponentInstance ci){
		return ci.getAllEnclosingConnectionInstances();
	}
}
