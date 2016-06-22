package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import static de.uniaugsburg.smds.aadl2rtsj.generation.utils.Constants.*;

import java.util.List;
import java.util.logging.Logger;

import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.IntegerLiteral;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.PropertyExpression;

public class PropertyHelper {
	
	private static final Logger log = Logger.getLogger(PropertyHelper.class.getName());
	
	public static String getTiming(NamedElement ne){
		String timing = null;
		List<PropertyExpression> timingProperties = ne.getPropertyValues(Communication_Properties, Communication_Properties_Timing);
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
	
	public static String getDispatchProtocol(NamedElement ne){
		String dispatchProtocol = "";
		// get Thread Type: Periodic, Aperiodic, Sporadic, Hybrid, Timed or Background
		List<PropertyExpression> dispatchProtocolPropertyList = ne.getPropertyValues(Thread_Properties, Thread_Properties_Dispatch_Protocol);
		// only do something if the type of thread was defined
		if(dispatchProtocolPropertyList.size() > 0){
			PropertyExpression dispatchProtocolProperty = dispatchProtocolPropertyList.get(0); //TODO we don't consider modes at the moment
			if(dispatchProtocolProperty instanceof NamedValue){
				EnumerationLiteral namedValue = (EnumerationLiteral)((NamedValue)dispatchProtocolProperty).getNamedValue();
				dispatchProtocol = namedValue.getName();// should be "Periodic" or "Aperiodic" or one of the others
			}
		}
		if(dispatchProtocol.equals(""))
			log.warning("No Thread_Properties::Dispatch_Protocol was given for " + ne.getName() + ". Default is periodic");
		return Thread_Properties_Dispatch_Protocol_Periodic;
	}
	
	public static String getPeriodMilliSeconds(NamedElement ne){
		// get period
		List<PropertyExpression> periodPropertyList = ne.getPropertyValues(Timing_Properties, Timing_Properties_Period);
		// only do something if period was defined
		if(periodPropertyList.size() > 0){
			PropertyExpression periodProperty = periodPropertyList.get(0); //TODO: we don't consider modes at the moment
			Integer period = new Integer((int)((IntegerLiteral)periodProperty).getScaledValue(AADL_Project_Time_Units_Milli_Seconds));
			return period.toString();
		}
		log.warning("No Timing_Properties::Period was given for " + ne.getName() + ". Default for millisecond part is 200 ms");
		return "200";
	}
	
	public static String getPeriodNanoSeconds(NamedElement ne){
		// get period
		List<PropertyExpression> periodPropertyList = ne.getPropertyValues(Timing_Properties, Timing_Properties_Period);
		// only do something if period was defined
		if(periodPropertyList.size() > 0){
			PropertyExpression periodProperty = periodPropertyList.get(0); //TODO: we don't consider modes at the moment
			// at nanoseconds we only want the part that is below ms, so we modulo by 1 million
			Integer period = new Integer((int)(((IntegerLiteral)periodProperty).getScaledValue(AADL_Project_Time_Units_Nano_Seconds) % 1000000));
			return period.toString();
		}
		log.warning("No Timing_Properties::Period was given for " + ne.getName() + ". Default for nanosecond part is 0 ns");
		return "0";
	}
	
	public static String getPriority(NamedElement ne){
		// get priority
		List<PropertyExpression> priorityPropertyList = ne.getPropertyValues(Thread_Properties, Thread_Properties_Priority);
		// only do something if period was defined
		if(priorityPropertyList.size() > 0){
			PropertyExpression priorityProperty = priorityPropertyList.get(0); //TODO: we don't consider modes at the moment
			Integer priority = new Integer((int)(((IntegerLiteral)priorityProperty).getValue()));
			return priority.toString();
		}
		log.warning("No Thread_Properties::Priority was given for " + ne.getName() + ". Default is 5");
		return "5";
	}

}
