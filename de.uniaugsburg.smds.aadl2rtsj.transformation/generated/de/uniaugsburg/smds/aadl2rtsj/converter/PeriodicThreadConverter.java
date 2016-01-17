package de.uniaugsburg.smds.aadl2rtsj.converter;

import java.util.List;
import java.util.logging.Logger;

import org.osate.aadl2.instance.InstanceObject;

import org.osate.aadl2.instance.FeatureInstance;
import de.uniaugsburg.smds.aadl2rtsj.utils.OffsetTime;
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
  protected final String TEXT_2 = ";" + NL + "" + NL + "import javax.realtime.Timer;" + NL + "import javax.realtime.OneShotTimer;" + NL + "import javax.realtime.RelativeTime;" + NL + "import javax.realtime.PeriodicTimer;" + NL + "import javax.realtime.AsyncEventHandler;" + NL + "import javax.realtime.PriorityParameters;" + NL;
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
  protected final String TEXT_13 = NL + "\t}" + NL + "\t" + NL + "\t@Override" + NL + "\tpublic void handleAsyncEvent() {" + NL + "\t\tdispatch();" + NL + "\t\tstart();" + NL + "\t\tcompute();" + NL + "\t\tcompletion();" + NL + "\t}" + NL + "\t" + NL + "\tprivate final void dispatch() {" + NL + "\t\t";
  protected final String TEXT_14 = NL + "\t\t";
  protected final String TEXT_15 = NL + "\t}" + NL + "\t" + NL + "\tprivate final void start() {" + NL + "\t\t";
  protected final String TEXT_16 = NL + "\t}" + NL + "\t" + NL + "\tprivate final void compute() {" + NL + "\t\t" + NL + "\t}" + NL + "\t" + NL + "\tprivate final void completion() {" + NL + "\t\t";
  protected final String TEXT_17 = NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * Starts the execution of this object" + NL + "\t */" + NL + "\tpublic void startExecution(){" + NL + "\t\ttimer.start();" + NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * Stops the execution of this object" + NL + "\t */" + NL + "\tpublic void stopExcution(){" + NL + "\t\ttimer.stop();" + NL + "\t}" + NL + "}";
  protected final String TEXT_18 = NL;

	private static final Logger log = Logger.getLogger( PeriodicThreadConverter.class.getName() );
	
	private static String getSubcomponentImportStatements(ComponentInstance component){
		List<ComponentInstance> subcomponents = component.getAllComponentInstances();
		if(subcomponents.size() > 1){
			StringBuilder sb = new StringBuilder();
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
							sb.append(new DeadlineIOViaHandlerStatement().generate(feature, offsetTime));
						else{
							if(offsetTime.getMs() == 0 && offsetTime.getNs() == 0)
								//send output, but only for this specific connection via simple statement
								sb.append(new SendOutputStatement().generate(feature, connection));// see AADL Standard 8.3.2 (29)
							else{
								// we need to generate a statement for handling via handler
								sb.append(new IOViaHandlerStatement().generate(feature, offsetTime));
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
				sb.append(new ReceiveInputStatement().generate(feature));// see AADL Standard 8.3.2 (21)
			else{
				// we need a handler statement, so that the input can be received later
				sb.append(new IOViaHandlerStatement().generate(feature, time));
			}
		}
		return sb.toString().trim();
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
		List<ComponentInstance> subcomponents = component.getAllComponentInstances();
		// remove the first element, because this is always the component itself
		subcomponents.remove(0);
		ConstructorAssignmentStatement statement= new ConstructorAssignmentStatement();
		StringBuilder sb = new StringBuilder();
		// for each feature we have to create an assignment
		for (FeatureInstance feature : features) {
			sb.append(statement.generate(feature));
		}
		//for each subcomponent we have to create a Parameter
		for (ComponentInstance subcomponent : subcomponents) {
			sb.append(statement.generate(subcomponent));
		}
		return sb.toString().trim();
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
    stringBuffer.append(getDeadlineStatements(component));
    stringBuffer.append(TEXT_15);
    stringBuffer.append(getStartStatements(component));
    stringBuffer.append(TEXT_16);
    stringBuffer.append(getCompletionStatements(component));
    stringBuffer.append(TEXT_17);
    stringBuffer.append(TEXT_18);
    return stringBuffer.toString();
  }
}