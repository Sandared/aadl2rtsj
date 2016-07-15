package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.AADL_Project_Time_Units_Milli_Seconds;
import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.AADL_Project_Time_Units_Nano_Seconds;
import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.Communication_Properties;
import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.Communication_Properties_IO_Reference_Time_Completion;
import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.Communication_Properties_IO_Reference_Time_Deadline;
import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.Communication_Properties_IO_Reference_Time_Dispatch;
import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.Communication_Properties_IO_Reference_Time_Start;
import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.Communication_Properties_Input_Time;
import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.Communication_Properties_Output_Time;
import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.Communication_Properties_Timing_Delayed;
import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.Communication_Properties_Timing_Immediate;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.BasicEList;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.Connection;
import org.osate.aadl2.DataPort;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.Feature;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.RangeValue;
import org.osate.aadl2.RecordValue;

import util.OffsetTime;
import util.UtilFactory;

public class ThreadHelper {
	
	private static Logger log = Logger.getLogger(ThreadHelper.class.getName());
	
	public static List<OffsetTime> getTimes(Feature feature, Connection connection, String IOReferenceTime, Boolean isInput){
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
    	   ComponentClassifier component = (ComponentClassifier) feature.getContainingClassifier();
    	   times = getTimeForReferenceTime(component, IOReferenceTime, false, isInput, connection);
        }
		
		if(times.size() == 0)
			if(isInput){
				//if time is still null, then default is Dispatch for Input, see AADL Standard 8.3.2 (17)). 
				OffsetTime ot = createOffsetTime(0, 0, System.identityHashCode(feature), Communication_Properties_IO_Reference_Time_Dispatch, connection);
				times.add(ot);
			}
			else{
				//if time is still null, then default is Completion for Output, see AADL Standard 8.3.2 (25)
				OffsetTime ot = createOffsetTime(0, 0, System.identityHashCode(feature), Communication_Properties_IO_Reference_Time_Completion, connection);
				times.add(ot);
			}
		return times;
	}
	
	private static OffsetTime getTimeForConnection(NamedElement element, Connection connection, boolean isInput){
		OffsetTime time = null;
		String timing = PropertyHelper.getTiming(connection); // sampled, immediate, delayed
		if(timing.equals(Communication_Properties_Timing_Immediate))
			if(isInput){
				// see AADL Standard 9.2.5 (50)
				time = createOffsetTime(0, 0, System.identityHashCode(connection), Communication_Properties_IO_Reference_Time_Start, connection);
			}
			else{
				// if there is a single valued Output_Time, then take it, otherwise the time is assumed to be Completion, 
				// see AADL Standard 9.2.5 (50)
				List<OffsetTime> times = getTimeForReferenceTime(element, null, true, false, connection);
				if(times.size() == 0){
					time = createOffsetTime(0, 0, System.identityHashCode(connection), Communication_Properties_IO_Reference_Time_Completion, connection);
				}
			}
		if(timing.equals(Communication_Properties_Timing_Delayed))
			if(isInput){
				// see AADL Standard 9.2.5 (51)
				time = createOffsetTime(0, 0, System.identityHashCode(connection), Communication_Properties_IO_Reference_Time_Dispatch, connection);
			}
			else{
				// see AADL Standard 9.2.5 (51)
				time = createOffsetTime(0, 0, System.identityHashCode(connection), Communication_Properties_IO_Reference_Time_Deadline, connection);
			}
		// sampled has no special semantic meaning for input and output timing
		return time;
	}
	
	private static List<OffsetTime> getTimeForReferenceTime(NamedElement element, final String IOReferenceTime, boolean forceSingleValued, boolean isInput, Connection connection){
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
     		   time = createOffsetTime(minimumMs, minimumNs, System.identityHashCode(timeProperty), ioTime, connection);
     		   
     		   if(IOReferenceTime == null || time.getIoTime().equals(IOReferenceTime)){
     			  times.add(time);
     		   }
     	   }
        }
        return times;
	}
	
	private static OffsetTime createOffsetTime(long minimumMs, long minimumNs, int uniqueID, String ioTime, Connection connection){
		OffsetTime time = UtilFactory.eINSTANCE.createOffsetTime();
		time.setMs(minimumMs);
		time.setNs(minimumNs);
		time.setUniqueId(uniqueID);
		time.setIoTime(ioTime);
		time.setConnection(connection);
		return time;
	}
	
	
	// TODO: was ist mit In Out Ports???
	public static List<OffsetTime> getTimes(DataPort feature, ComponentImplementation parent){
		List<OffsetTime> times = new BasicEList<OffsetTime>();
		List<Connection> connections = null;
		boolean isInput = false;
		if(ComponentClassifierHelper.isIncoming(feature) && !ComponentClassifierHelper.isOutgoing(feature)){
			connections = ComponentClassifierHelper.getIncomingConnections(feature, parent);
			isInput = true;
		}
		if(ComponentClassifierHelper.isOutgoing(feature) && !ComponentClassifierHelper.isIncoming(feature)){
			connections = ComponentClassifierHelper.getOutgoingConnections(feature, parent);
			isInput = false;
		}
		if (connections == null) {
			log.severe("no connections found for " + feature);
		}
		else{
			for (Connection connection : connections) {
				times.addAll(getTimes(feature, connection, null, isInput)); // no referencetime is needed if we want all times for one feature
			}
		}
		return times;
	}

}
