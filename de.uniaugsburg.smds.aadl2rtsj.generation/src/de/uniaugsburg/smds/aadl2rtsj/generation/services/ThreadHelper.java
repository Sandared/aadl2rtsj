package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import static de.uniaugsburg.smds.aadl2rtsj.generation.utils.Constants.*;

import java.util.List;
import java.util.logging.Logger;

import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.IntegerLiteral;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.instance.ComponentInstance;

public class ThreadHelper {
	private static final Logger log = Logger.getLogger( ThreadHelper.class.getName() );
	
	public static boolean isThread(ComponentInstance ci){
		return ci.getCategory() == ComponentCategory.THREAD;
	}
	
	public static boolean isPeriodic(ComponentInstance ci){
		return getDispatchProtocol(ci).equals(Thread_Properties_Dispatch_Protocol_Periodic);
	}
	
	public static String getDispatchProtocol(ComponentInstance ci){
		String dispatchProtocol = "";
		// get Thread Type: Periodic, Aperiodic, Sporadic, Hybrid, Timed or Background
		List<PropertyExpression> dispatchProtocolPropertyList = ci.getPropertyValues(Thread_Properties, Thread_Properties_Dispatch_Protocol);
		// only do something if the type of thread was defined
		if(dispatchProtocolPropertyList.size() > 0){
			PropertyExpression dispatchProtocolProperty = dispatchProtocolPropertyList.get(0); //TODO we don't consider modes at the moment
			if(dispatchProtocolProperty instanceof NamedValue){
				EnumerationLiteral namedValue = (EnumerationLiteral)((NamedValue)dispatchProtocolProperty).getNamedValue();
				dispatchProtocol = namedValue.getName();// should be "Periodic" or "Aperiodic" or one of the others
			}
		}
		if(dispatchProtocol.equals(""))
			log.warning("No Thread_Properties::Dispatch_Protocol was given for " + ci.getName());
		return dispatchProtocol;
	}
	
	public static String getPeriodMilliSeconds(ComponentInstance ci){
		// get period
		List<PropertyExpression> periodPropertyList = ci.getPropertyValues(Timing_Properties, Timing_Properties_Period);
		// only do something if period was defined
		if(periodPropertyList.size() > 0){
			PropertyExpression periodProperty = periodPropertyList.get(0); //TODO: we don't consider modes at the moment
			Integer period = new Integer((int)((IntegerLiteral)periodProperty).getScaledValue(AADL_Project_Time_Units_Milli_Seconds));
			return period.toString();
		}
		log.warning("No Timing_Properties::Period was given for " + ci.getName());
		return "0";
	}
	
	public static String getPeriodNanoSeconds(ComponentInstance ci){
		// get period
		List<PropertyExpression> periodPropertyList = ci.getPropertyValues(Timing_Properties, Timing_Properties_Period);
		// only do something if period was defined
		if(periodPropertyList.size() > 0){
			PropertyExpression periodProperty = periodPropertyList.get(0); //TODO: we don't consider modes at the moment
			// at nanoseconds we only want the part that is below ms, so we modulo by 1 million
			Integer period = new Integer((int)(((IntegerLiteral)periodProperty).getScaledValue(AADL_Project_Time_Units_Nano_Seconds) % 1000000));
			return period.toString();
		}
		log.warning("No Timing_Properties::Period was given for " + ci.getName());
		return "0";
	}
	
	public static String getPriority(ComponentInstance ci){
		// get priority
		List<PropertyExpression> priorityPropertyList = ci.getPropertyValues(Thread_Properties, Thread_Properties_Priority);
		// only do something if period was defined
		if(priorityPropertyList.size() > 0){
			PropertyExpression priorityProperty = priorityPropertyList.get(0); //TODO: we don't consider modes at the moment
			Integer priority = new Integer((int)(((IntegerLiteral)priorityProperty).getValue()));
			return priority.toString();
		}
		log.warning("No Thread_Properties::Priority was given for " + ci.getName());
		return "5";
	}
}
