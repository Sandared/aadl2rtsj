package de.uniaugsburg.smds.aadl2rtsj.converter;

import java.util.List;
import java.util.logging.Logger;

import org.osate.aadl2.instance.InstanceObject;

import org.osate.aadl2.instance.FeatureInstance;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

import org.eclipse.jdt.internal.core.util.MethodParametersAttribute;
import org.osate.aadl2.AbstractNamedValue;
import org.osate.aadl2.BasicPropertyAssociation;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.Element;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.Feature;
import org.osate.aadl2.FeatureClassifier;
import org.osate.aadl2.IntegerLiteral;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.RecordValue;
import org.osate.aadl2.UnitLiteral;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;

public class PeriodicThreadConverter{
	
  protected static String nl;
  public static synchronized PeriodicThreadConverter create(String lineSeparator)
  {
    nl = lineSeparator;
    PeriodicThreadConverter result = new PeriodicThreadConverter();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL + "" + NL + "import javax.realtime.AsyncEventHandler;" + NL + "import javax.realtime.Timer;" + NL + "import javax.realtime.PeriodicTimer;" + NL + "import javax.realtime.RelativeTime;" + NL + "import javax.realtime.PriorityParameters;" + NL;
  protected final String TEXT_3 = NL;
  protected final String TEXT_4 = NL + NL + "public class ";
  protected final String TEXT_5 = " extends AsyncEventHandler{" + NL + "\t";
  protected final String TEXT_6 = NL + "\t";
  protected final String TEXT_7 = NL + "\t" + NL + "\tprivate Timer timer;" + NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_8 = "(";
  protected final String TEXT_9 = "){" + NL + "\t\tsuper();" + NL + "\t\tsetSchedulingParametersIfFeasible(new PriorityParameters(";
  protected final String TEXT_10 = "));" + NL + "\t\ttimer = new PeriodicTimer(new RelativeTime(), new RelativeTime(";
  protected final String TEXT_11 = ", ";
  protected final String TEXT_12 = "), this);" + NL + "\t\t";
  protected final String TEXT_13 = NL + "\t}" + NL + "\t" + NL + "\tpublic void startExecution(){" + NL + "\t\ttimer.start();" + NL + "\t}" + NL + "\t" + NL + "\t@Override" + NL + "\tpublic void handleAsyncEvent() {" + NL + "\t\tdispatch();" + NL + "\t\tstart();" + NL + "\t\tcompute();" + NL + "\t\tcompletion();" + NL + "\t}" + NL + "" + NL + "\tprivate final void dispatch() {" + NL + "\t\t";
  protected final String TEXT_14 = NL + "\t}" + NL + "\t" + NL + "\tprivate final void start() {" + NL + "\t\t";
  protected final String TEXT_15 = NL + "\t}" + NL + "" + NL + "\tprivate final void compute() {" + NL + "\t\t" + NL + "\t}" + NL + "\t" + NL + "\tprivate final void completion() {" + NL + "\t\t";
  protected final String TEXT_16 = NL + "\t}" + NL + "}";
  protected final String TEXT_17 = NL;

	private static final Logger log = Logger.getLogger( PeriodicThreadConverter.class.getName() );
	
	private static String getSubcomponentImportStatements(ComponentInstance component){
		List<ComponentInstance> subcomponents = component.getAllComponentInstances();
		if(subcomponents.size() > 1){
			StringBuilder sb = new StringBuilder();
//			sb.append("/*\n");
//			sb.append("* IMPORT SUBCOMPONENTS\n");
//			sb.append("*/\n");
			//skip the first one as it is the component itself
			for(ComponentInstance cmp: subcomponents){
				sb.append(new ImportStatement().generate(cmp));
			}
			return sb.toString();
		}
		else{
			return "";
		}
	}
	
	private static String getSubcomponentMemberStatements(ComponentInstance component){
		List<ComponentInstance> subcomponents = component.getAllComponentInstances();
		if(subcomponents.size() > 1){
			StringBuilder sb = new StringBuilder();
//			sb.append("\t/*\n");
//			sb.append("\t* SUBCOMPONENTS\n");
//			sb.append("\t*/\n");
			//skip the first one as it is the component itself
			for(ComponentInstance cmp: subcomponents){
				sb.append(new MemberStatement().generate(cmp));
			}
			return sb.toString();
		}
		else{
			return "";
		}
	}
	
	private static String getFeatureMemberStatements(ComponentInstance component){
		List<FeatureInstance> features = component.getAllFeatureInstances();
		if(features.size() > 0){
			StringBuilder sb = new StringBuilder();
//			sb.append("\t/*\n");
//			sb.append("\t* FEATURES\n");
//			sb.append("\t*/\n");
			for(FeatureInstance feature: features){
				sb.append(new DeclarationMemberStatement().generate(feature));
			}
			return sb.toString().trim();
		}
		else{
			return "";
		}
	}
	
	private static String getDispatchStatements(ComponentInstance component){
		return getStatementsForReferenceTime(component, Communication_Properties_IO_Reference_Time_Dispatch);
	}
	
	private static String getStartStatements(ComponentInstance component){
		return getStatementsForReferenceTime(component, Communication_Properties_IO_Reference_Time_Start);
	}
	
	private static String getCompletionStatements(ComponentInstance component){
		return getStatementsForReferenceTime(component, Communication_Properties_IO_Reference_Time_Completion);
	}
	
	private static String getDeadlineStatements(ComponentInstance component){
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
						log.info("some other feature than ports");
						break;
					}
			}
			return sb.toString().trim();
		}
		log.info("No features found for " + component.getName());
		return "";
	}

	// ASSUMPTIONS (Maybe correct them later with OneShotTimers)
	// (1) IGNORE Output_Rate
	// (2) IGNORE Offset part of Timing
	// (3) IGNORE Output_Time == Dispatch, as we don't know how to achieve that in rtsj, AND it seems to be forbidden by standard 8.3.2 (27)
	// (4) IGNORE Output_Time == Deadline, as we don't know how to achieve that in rtsj
	private static String getDataPortOutputStatementsForReferenceTime(FeatureInstance feature, final String IOReferenceTime){
		// see (3) and (4)
		if(IOReferenceTime.equals(Communication_Properties_IO_Reference_Time_Deadline) || IOReferenceTime.equals(Communication_Properties_IO_Reference_Time_Dispatch))
			return "";
		
		StringBuilder sb = new StringBuilder();
		// get all connections, where this feature is the destination
		List<ConnectionInstance> connections = feature.getSrcConnectionInstances();
		String outputAt = null;
		
		// if immediate/delayed connections are present, then partially ignore Output_Time
		if(connections.size() > 0){
			// out DataPorts may have multiple connections per mode. 
			// those might have different timings
			for (ConnectionInstance connection : connections) {
				// determine actual timing for this feature-connection-combination 
				outputAt = getTime(feature, connection, IOReferenceTime, false);

				//if outputAt is NoIO, then nothing happens, see AADL Standard 8.3.2 (19)
				if(outputAt.equals(IOReferenceTime)){
					//send output, but only for this specific connection
					sb.append(new SendOutputStatement().generate(feature, connection));// see AADL Standard 8.3.2 (29)
				}
			}
			return sb.toString();
		}
		
		return sb.toString();
	}
	
	// ASSUMPTIONS (Maybe correct them later with OneShotTimers)
	// (1) IGNORE Input_Rate
	// (2) IGNORE Offset part of Timing
	// (3) IGNORE Input_Time == Dispatch, as we don't know how to achieve that in rtsj
	// (4) IGNORE Input_Time == Deadline, as we don't know how to achieve that in rtsj, AND it seems to be forbidden by standard 8.3.2 (19)
	private static String getDataPortInputStatementsForReferenceTime(FeatureInstance feature, final String IOReferenceTime){
		// see (3) and (4)
		if(IOReferenceTime.equals(Communication_Properties_IO_Reference_Time_Deadline) || IOReferenceTime.equals(Communication_Properties_IO_Reference_Time_Dispatch))
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
		String inputAt = getTime(feature, connection, IOReferenceTime, true);
		
		//if inputAt is NoIO, then nothing happens, see AADL Standard 8.3.2 (19)
		if(inputAt.equals(IOReferenceTime)){
			//freeze input
			sb.append(new ReceiveInputStatement().generate(feature));// see AADL Standard 8.3.2 (21)
		}
		
		return sb.toString();
	}
	
	private static String getTime(FeatureInstance feature, ConnectionInstance connection, final String IOReferenceTime, boolean isInput){
		String time = null;
		
		// if immediate/delayed connections are present, then (partially) ignore Input/Output_Time
		if(connection != null){
			time = getTimeForConnection(feature, connection, isInput);
		}
		
		// if NOT immediate/delayed consider Time part of Input/Output_Time and IGNORE offset
		if(time == null)
			time = getTimeForReferenceTime(feature, IOReferenceTime, false, isInput);
        else{
    	   // if there has been no Input/Output_Time on the feature, check for Input/Output_Time on thread, see AADL Standard 8.3.2 (18)
    	   ComponentInstance component = feature.getContainingComponentInstance();
    	   time = getTimeForReferenceTime(component, IOReferenceTime, false, isInput);
        }
		
		if(time == null)
			if(isInput)
				//if time is still null, then default is Dispatch for Input, see AADL Standard 8.3.2 (17)). 
				time = Communication_Properties_IO_Reference_Time_Dispatch;
			else
				//if time is still null, then default is Completion for Output, see AADL Standard 8.3.2 (25)
				time = Communication_Properties_IO_Reference_Time_Completion;
		return time;
	}
	
	private static String getTimeForConnection(NamedElement element, ConnectionInstance connection, boolean isInput){
		List<PropertyExpression> timingProperties = connection.getPropertyValues(Communication_Properties, Communication_Properties_Timing);
		String time = null;
		if(timingProperties.size() > 0){
			EnumerationLiteral timingProperty = (EnumerationLiteral)((NamedValue)timingProperties.get(0)).getNamedValue();
			String timing = timingProperty.getName(); // sampled, immediate, delayed
			if(timing.equals(Communication_Properties_Timing_Immediate))
				if(isInput)
					time = Communication_Properties_IO_Reference_Time_Start; // see AADL Standard 9.2.5 (50)
				else{
					// if there is a single valued Output_Time, then take it, otherwise the time is assumed to be Completion, 
					// see AADL Standard 9.2.5 (50)
					time = getTimeForReferenceTime(element, null, true, false);
					if(time == null)
						time = Communication_Properties_IO_Reference_Time_Completion; 
				}
			if(timing.equals(Communication_Properties_Timing_Delayed))
				if(isInput)
					time = Communication_Properties_IO_Reference_Time_Dispatch;// see AADL Standard 9.2.5 (51)
				else
					time = Communication_Properties_IO_Reference_Time_Deadline;// see AADL Standard 9.2.5 (51)
			// sampled has no special semantic meaning for input and output timing
		}
		return time;
	}
	
	private static String getTimeForReferenceTime(NamedElement element, final String IOReferenceTime, boolean forceSingleValued, boolean isInput){
		List<PropertyExpression> timeProperties = null;
		if(isInput)
			timeProperties = element.getPropertyValues(Communication_Properties, Communication_Properties_Input_Time);
		else
			timeProperties = element.getPropertyValues(Communication_Properties, Communication_Properties_Output_Time);
		
		String time = null;
		
        if(timeProperties.size() > 0){
        	if(forceSingleValued)
        		if(timeProperties.size() != 1)
        			return time;
     	   // Input/Output_Time might be a list, so Input can be frozen multiple times during a dispatch, see AADL Standard 8.3.2 (20)
     	   for (PropertyExpression timeProperty : timeProperties) {
     		   // Input/Output_Time consists of a Time Part, which is an EnumerationLiteral and an Offset, which is a RangeValue
     		   // we ignor Offset and are only interested in the Time part (Dispatch, Start, Completion, Deadline, NoIO)
     		   NamedValue timePart = (NamedValue)((RecordValue)timeProperty).getOwnedFieldValues().get(0).getOwnedValue();
     		   time = ((EnumerationLiteral)timePart.getNamedValue()).getName();
     		   if(time.equals(IOReferenceTime)){
     			   break;
     		   }
     	   }
        }
        return time;
	}
	
	private static String getConstructorParameters(ComponentInstance component){
		List<FeatureInstance> features = component.getFeatureInstances();
		List<ComponentInstance> subcomponents = component.getAllComponentInstances();
		// remove the first element, because this is always the component itself
		subcomponents.remove(0);
		// for each feature we have to create a parameter
		StringBuilder sb = new StringBuilder();
		MethodParameterStatement statement = new MethodParameterStatement();
		for(FeatureInstance feature : features){
			sb.append(statement.generate(feature));
		}
		//for each subcomponent we have to create a Parameter
		for (ComponentInstance subcomponent : subcomponents) {
			sb.append(statement.generate(subcomponent));
		}
		// delete pending commata ", "
		sb.delete(sb.length()-2, sb.length());
		return sb.toString().trim();
	}
	
	private static String getConstructorMemberAssignments(ComponentInstance component){
		List<FeatureInstance> features = component.getFeatureInstances();
		// for each feature we have to create an assignment
		if(features.size() > 0){
			StringBuilder sb = new StringBuilder();
			for (FeatureInstance feature : features) {
				sb.append(new ConstructorAssignmentStatement().generate(feature));
			}
			return sb.toString().trim();
		}
		return "";
	}
	
	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ComponentInstance component)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getPackageName(component));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(getSubcomponentImportStatements(component));
    stringBuffer.append(TEXT_4);
    stringBuffer.append(getClassName(component));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(getSubcomponentMemberStatements(component));
    stringBuffer.append(TEXT_6);
    stringBuffer.append(getFeatureMemberStatements(component));
    stringBuffer.append(TEXT_7);
    stringBuffer.append(getClassName(component));
    stringBuffer.append(TEXT_8);
    stringBuffer.append(getConstructorParameters(component));
    stringBuffer.append(TEXT_9);
    stringBuffer.append(getPriority(component));
    stringBuffer.append(TEXT_10);
    stringBuffer.append(getPeriodMilliSeconds(component));
    stringBuffer.append(TEXT_11);
    stringBuffer.append(getPeriodNanoSeconds(component));
    stringBuffer.append(TEXT_12);
    stringBuffer.append(getConstructorMemberAssignments(component));
    stringBuffer.append(TEXT_13);
    stringBuffer.append(getDispatchStatements(component));
    stringBuffer.append(TEXT_14);
    stringBuffer.append(getStartStatements(component));
    stringBuffer.append(TEXT_15);
    stringBuffer.append(getCompletionStatements(component));
    stringBuffer.append(TEXT_16);
    stringBuffer.append(TEXT_17);
    return stringBuffer.toString();
  }
}