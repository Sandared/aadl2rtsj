package de.uniaugsburg.smds.aadl2rtsj.generation.services.components.threads;

import static de.uniaugsburg.smds.aadl2rtsj.generation.utils.Constants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.IntegerLiteral;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.RangeValue;
import org.osate.aadl2.RecordValue;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.FeatureInstance;

import de.uniaugsburg.smds.aadl2rtsj.generation.utils.OffsetTime;
import de.uniaugsburg.smds.aadl2rtsj.generation.utils.SimpleStatements;

public class PeriodicThreadHelper {
	private static final Logger log = Logger.getLogger( PeriodicThreadHelper.class.getName() );
	
	
	
	public static String getDispatchStatements(ComponentInstance component){
		return getStatementsForReferenceTime(component, Communication_Properties_IO_Reference_Time_Dispatch);
	}
	
	public static String getStartStatements(ComponentInstance component){
		return getStatementsForReferenceTime(component, Communication_Properties_IO_Reference_Time_Start);
	}
	
	public static String getCompletionStatements(ComponentInstance component){
		return getStatementsForReferenceTime(component, Communication_Properties_IO_Reference_Time_Completion);
	}
	
	public static String getDeadlineStatements(ComponentInstance component){
		return getStatementsForReferenceTime(component, Communication_Properties_IO_Reference_Time_Deadline);
	}
	
	private static String getStatementsForReferenceTime(ComponentInstance component, final String IOReferenceTime){
		List<FeatureInstance> features = component.getAllFeatureInstances();
		// only do something if features are defined
		if(features.size() > 0){
			StringBuilder sb = new StringBuilder();
			for (FeatureInstance feature : features) {
				// which direction is the feature? in, out, inout
				DirectionType direction = feature.getDirection();
				switch (feature.getCategory()) {
					case DATA_PORT:
						if(direction.incoming())
							sb.append(getDataPortInputStatementsForReferenceTime(feature, IOReferenceTime));
						if(direction.outgoing())
							sb.append(getDataPortOutputStatementsForReferenceTime(feature, IOReferenceTime));
						break;
					case EVENT_PORT:
						// TODO: Event Port Statements
						break;
					case EVENT_DATA_PORT:
						// TODO: Event Data Port Statements
						break;
					default:
						log.info("some other features than ports");
						break;
					}
			}
			return sb.toString().trim();
		}
		log.info("No features found for " + component.getName());
		return "";
	}

	// ASSUMPTIONS
	// (1) IGNORE Output_Rate
	// (2) IGNORE negative Offset part of Timing
	// (3) IGNORE Output_Time == Dispatch, it seems to be forbidden by standard 8.3.2 (27) TODO: ask on mailinglist for clarification
	private static String getDataPortOutputStatementsForReferenceTime(FeatureInstance feature, final String IOReferenceTime){
		// see (3)
		if(IOReferenceTime.equals(Communication_Properties_IO_Reference_Time_Dispatch))
			return "";
		
		StringBuilder sb = new StringBuilder();
		// get all connections, where this feature is the destination
		List<ConnectionInstance> connections = feature.getSrcConnectionInstances();
		
		// if immediate/delayed connections are present, then partially ignore Output_Time
		if(connections.size() > 0){
			// out DataPorts may have multiple connections per mode. 
			// those might have different timings
			for (ConnectionInstance connection : connections) {
				// determine actual timing for this feature-connection-combination 
				List<OffsetTime> outputsAt = getTimes(feature, connection, IOReferenceTime, false);

				for (OffsetTime offsetTime : outputsAt) {
					
					String outputAt = offsetTime.getIOTime();
					//if outputAt is NoIO, then nothing happens, see AADL Standard 8.3.2 (19)
					if(outputAt.equals(IOReferenceTime)){
						// deadline must be treated different, as it is always done by a handler
						if(IOReferenceTime.equals(Communication_Properties_IO_Reference_Time_Deadline))
							sb.append(SimpleStatements.iOViaHandlerStatement(feature, offsetTime, true));
						else{
							if(offsetTime.getMs() == 0 && offsetTime.getNs() == 0)
								//send output, but only for this specific connection via simple statement
								sb.append(SimpleStatements.sendOutputStatement(feature, connection));// see AADL Standard 8.3.2 (29)
							else{
								// we need to generate a statement for handling via handler
								sb.append(SimpleStatements.iOViaHandlerStatement(feature, offsetTime, false));
							}
						}
					}
				}
			}
		}
		return sb.toString();
	}
	
	// ASSUMPTIONS 
	// (1) IGNORE Input_Rate
	// (2) IGNORE negative Offset part of Timing
	// (3) IGNORE Input_Time == Deadline, it seems to be forbidden by standard 8.3.2 (19) TODO: ask on mailinglist for clarification
	private static String getDataPortInputStatementsForReferenceTime(FeatureInstance feature, final String IOReferenceTime){
		// see (3)
		if(IOReferenceTime.equals(Communication_Properties_IO_Reference_Time_Deadline))
			return "";
		
		StringBuilder sb = new StringBuilder();
		// get all connections, where this feature is the destination
		List<ConnectionInstance> connections = feature.getDstConnectionInstances();
		
		// if immediate/delayed connections are present, then ignore Input_Time
		ConnectionInstance connection = null;
		if(connections.size() > 0)
			// in DataPorts may have only one connection per mode. We don't consider modes at the moment, so there may only be one connection. 
			connection = connections.get(0);
		
		
		// determine actual timing for this feature-connection-combination
		// as we don#t consider modes, we can take the first one
		OffsetTime time = getTimes(feature, connection, IOReferenceTime, true).get(0);
		String inputAt = time.getIOTime();
		
		//if inputAt is NoIO, then nothing happens, see AADL Standard 8.3.2 (19)
		if(inputAt.equals(IOReferenceTime)){
			if(time.getMs() == 0 && time.getNs() == 0)
				//freeze input via simpple statement
				sb.append(SimpleStatements.receiveInputStatement(feature));// see AADL Standard 8.3.2 (21)
			else{
				// we need a handler statement, so that the input can be received later
				sb.append(SimpleStatements.iOViaHandlerStatement(feature, time, false));
			}
		}
		return sb.toString().trim();
	}
	
	private static List<OffsetTime> getTimes(FeatureInstance feature, ConnectionInstance connection, final String IOReferenceTime, boolean isInput){
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
			if(isInput)
				//if time is still null, then default is Dispatch for Input, see AADL Standard 8.3.2 (17)). 
				times.add(new OffsetTime(0, 0, System.identityHashCode(feature), Communication_Properties_IO_Reference_Time_Dispatch, connection));
			else
				//if time is still null, then default is Completion for Output, see AADL Standard 8.3.2 (25)
				times.add(new OffsetTime(0, 0, System.identityHashCode(feature), Communication_Properties_IO_Reference_Time_Completion, connection));
		return times;
	}
	
	private static OffsetTime getTimeForConnection(NamedElement element, ConnectionInstance connection, boolean isInput){
		OffsetTime time = null;
		String timing = getTiming(connection); // sampled, immediate, delayed
		if(timing.equals(Communication_Properties_Timing_Immediate))
			if(isInput)
				time = new OffsetTime(0, 0, System.identityHashCode(connection), Communication_Properties_IO_Reference_Time_Start, connection); // see AADL Standard 9.2.5 (50)
			else{
				// if there is a single valued Output_Time, then take it, otherwise the time is assumed to be Completion, 
				// see AADL Standard 9.2.5 (50)
				List<OffsetTime> times = getTimeForReferenceTime(element, null, true, false, connection);
				if(times.size() == 0)
					time = new OffsetTime(0, 0, System.identityHashCode(connection), Communication_Properties_IO_Reference_Time_Completion, connection); 
			}
		if(timing.equals(Communication_Properties_Timing_Delayed))
			if(isInput)
				time = new OffsetTime(0, 0, System.identityHashCode(connection), Communication_Properties_IO_Reference_Time_Dispatch, connection);// see AADL Standard 9.2.5 (51)
			else
				time = new OffsetTime(0, 0, System.identityHashCode(connection), Communication_Properties_IO_Reference_Time_Deadline, connection);// see AADL Standard 9.2.5 (51)
		// sampled has no special semantic meaning for input and output timing
		return time;
	}
	
	private static String getTiming(ConnectionInstance connection){
		String timing = null;
		List<PropertyExpression> timingProperties = connection.getPropertyValues(Communication_Properties, Communication_Properties_Timing);
		if(timingProperties.size() > 0){
			EnumerationLiteral timingProperty = (EnumerationLiteral)((NamedValue)timingProperties.get(0)).getNamedValue();
			timing = timingProperty.getName(); // sampled, immediate, delayed
		}
		else{
			//default
			timing = Communication_Properties_Timing_Sampled;
		}
		return timing;
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
     		   time = new OffsetTime(minimumMs, minimumNs, System.identityHashCode(timeProperty), ioTime, connection);
     		   
     		   if(IOReferenceTime == null || time.getIOTime().equals(IOReferenceTime)){
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
				String timing = getTiming(connection);
				if(timing.equals(Communication_Properties_Timing_Immediate))
					return connection;
			}
		}
		return null;
	}
}
