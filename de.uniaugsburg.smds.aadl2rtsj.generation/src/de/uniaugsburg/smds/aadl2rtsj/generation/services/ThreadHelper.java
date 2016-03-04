package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import static de.uniaugsburg.smds.aadl2rtsj.generation.utils.Constants.*;

import java.util.List;

import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.instance.ComponentInstance;

public class ThreadHelper {
	
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
		return dispatchProtocol;
	}
}
