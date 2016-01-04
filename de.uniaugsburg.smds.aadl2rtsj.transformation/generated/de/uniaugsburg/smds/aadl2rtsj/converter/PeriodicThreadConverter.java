package de.uniaugsburg.smds.aadl2rtsj.converter;

import java.util.List;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.FeatureInstance;
import de.uniaugsburg.smds.aadl2rtsj.utils.Utils;

import org.osate.aadl2.AbstractNamedValue;
import org.osate.aadl2.BasicPropertyAssociation;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.Element;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.Feature;
import org.osate.aadl2.FeatureClassifier;
import org.osate.aadl2.IntegerLiteral;
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
  protected final String TEXT_1 = NL + "import javax.realtime.AsyncEventHandler;" + NL + "import javax.realtime.Timer;" + NL + "import javax.realtime.PeriodicTimer;" + NL + "import javax.realtime.RelativeTime;" + NL + "import javax.realtime.PriorityParameters;" + NL;
  protected final String TEXT_2 = NL;
  protected final String TEXT_3 = NL + NL + "public class ";
  protected final String TEXT_4 = " extends AsyncEventHandler{" + NL + "\t";
  protected final String TEXT_5 = NL + "\t";
  protected final String TEXT_6 = NL + "\t" + NL + "\tprivate Timer timer;" + NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_7 = "(){" + NL + "\t\tsuper();" + NL + "\t\tsetSchedulingParametersIfFeasible(new PriorityParameters(";
  protected final String TEXT_8 = "));" + NL + "\t\ttimer = new PeriodicTimer(new RelativeTime(), new RelativeTime(";
  protected final String TEXT_9 = ", ";
  protected final String TEXT_10 = "), this);" + NL + "\t}" + NL + "\t" + NL + "\tpublic void start(){" + NL + "\t\ttimer.start();" + NL + "\t}" + NL + "\t" + NL + "\t@Override" + NL + "\tpublic void handleAsyncEvent() {" + NL + "\t\tdoDispatch();" + NL + "\t\tdoCompute();" + NL + "\t\tdoFinalize();" + NL + "\t}" + NL + "" + NL + "\tprivate final void doFinalize() {" + NL + "\t\t" + NL + "\t}" + NL + "" + NL + "\tprivate final void doCompute() {" + NL + "\t\t" + NL + "\t}" + NL + "" + NL + "\tprivate final void doDispatch() {" + NL + "\t\t";
  protected final String TEXT_11 = NL + "\t}" + NL + "\t" + NL + "\t" + NL + "}";
  protected final String TEXT_12 = NL;

	private static String getSubcomponentImportStatements(ComponentInstance component){
		List<ComponentInstance> subcomponents = component.getAllComponentInstances();
		if(subcomponents.size() > 1){
			StringBuilder sb = new StringBuilder();
			sb.append("/*\n");
			sb.append("* IMPORT SUBCOMPONENTS\n");
			sb.append("*/\n");
			//skip the first one as it is the component itself
			for(int i = 1; i < subcomponents.size(); i++){
				appendImportStatement(subcomponents.get(i), sb);
				if(i != subcomponents.size() - 1){
					sb.append("\n");
				}
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
			sb.append("/*\n");
			sb.append("\t* SUBCOMPONENTS\n");
			sb.append("\t*/\n");
			//skip the first one as it is the component itself
			for(int i = 1; i < subcomponents.size(); i++){
				appendMemberStatement(subcomponents.get(i), sb);
				if(i != subcomponents.size() - 1){
					sb.append("\n");
				}
			}
			return sb.toString();
		}
		else{
			return "";
		}
	}
	
//	private static String getFeatureImportStatements(ComponentInstance component){
//		List<FeatureInstance> features = component.getAllFeatureInstances();
//		if(features.size() > 0){
//			StringBuilder sb = new StringBuilder();
//			sb.append("/*\n");
//			sb.append("* IMPORT FEATURES\n");
//			sb.append("*/\n");
//			for(int i = 0; i < features.size(); i++){
//				appendImportStatement(features.get(i), sb);
//				if(i != features.size() - 1){
//					sb.append("\n");
//				}
//			}
//			return sb.toString();
//		}
//		else{
//			return "";
//		}
//	}
	
	private static String getFeatureMemberStatements(ComponentInstance component){
		List<FeatureInstance> features = component.getAllFeatureInstances();
		if(features.size() > 0){
			StringBuilder sb = new StringBuilder();
			sb.append("/*\n");
			sb.append("\t* FEATURES\n");
			sb.append("\t*/\n");
			//skip the first one as it is the component itself
			for(int i = 0; i < features.size(); i++){
				appendMemberStatement(features.get(i), sb);
				if(i != features.size() - 1){
					sb.append("\n");
				}
			}
			return sb.toString();
		}
		else{
			return "";
		}
	}
	
	/*
	 * 	for each object create a statement like this:<br>
	 *  import Packagename.Classname;
	 */
	private static void appendImportStatement(InstanceObject object, StringBuilder sb){
		sb.append("import ");
		sb.append(Utils.getPackageName(object));
		sb.append(".");
		sb.append(Utils.getClassName(object));
		sb.append(";");
	}
	
	/*
	 * 	for each object create a statement like this:<br>
	 *  \t Classname variablename = new Classname();;
	 */
	private static void appendMemberStatement(InstanceObject object, StringBuilder sb){
		sb.append("\tprivate ");
		sb.append(Utils.getClassName(object));
		sb.append(" ");
		sb.append(object.getName());
		sb.append(" = new ");
		sb.append(Utils.getClassName(object));
		sb.append("();");
	}
	
	private static String getPeriodMilliSeconds(ComponentInstance component){
		// get period
		List<PropertyExpression> periodPropertyList = component.getPropertyValues("Timing_Properties", "Period");
		// only do something if period was defined
		if(periodPropertyList.size() > 0){
			PropertyExpression periodProperty = periodPropertyList.get(0); // we don't consider modes at the moment
			Integer period = new Integer((int)((IntegerLiteral)periodProperty).getScaledValue("ms"));
			return period.toString();
		}
		System.err.println("No Timing_Properties::Period was given for " + component.getName());
		return "";
	}
	
	private static String getPeriodNanoSeconds(ComponentInstance component){
		// get period
		List<PropertyExpression> periodPropertyList = component.getPropertyValues("Timing_Properties", "Period");
		// only do something if period was defined
		if(periodPropertyList.size() > 0){
			PropertyExpression periodProperty = periodPropertyList.get(0); // we don't consider modes at the moment
			// at nanoseconds we only want the part that is below ms, so we modulo by 1 million
			Integer period = new Integer((int)(((IntegerLiteral)periodProperty).getScaledValue("ns") % 1000000));
			return period.toString();
		}
		System.err.println("No Timing_Properties::Period was given for " + component.getName());
		return "";
	}
	
	private static String getPriority(ComponentInstance component){
		// get priority
		List<PropertyExpression> priorityPropertyList = component.getPropertyValues("Thread_Properties", "Priority");
		// only do something if period was defined
		if(priorityPropertyList.size() > 0){
			PropertyExpression priorityProperty = priorityPropertyList.get(0); // we don't consider modes at the moment
			Integer priority = new Integer((int)(((IntegerLiteral)priorityProperty).getValue()));
			return priority.toString();
		}
		System.err.println("No Thread_Properties::Priority was given for " + component.getName());
		//TODO default values
		return "";
	}
	
	private static String getDispatchStatements(ComponentInstance component){
		List<FeatureInstance> features = component.getAllFeatureInstances();
		// only do something if features are defined
		if(features.size() > 0){
			StringBuilder sb = new StringBuilder();
			for (FeatureInstance feature : features) {
				switch (feature.getCategory()) {
					case DATA_PORT:
						getDataPortDispatchStatements(feature, sb);
						
						break;
					
	
					default:
						break;
					}
			}
			return sb.toString();
		}
		return "";
	}
	
	private static void getDataPortDispatchStatements(FeatureInstance feature, StringBuilder sb ){
		// which direction is the feature? in, out, inout
		DirectionType direction = feature.getDirection();
		
		if(direction.incoming()){
			// get all connections, where this feature is the destination
			List<ConnectionInstance> ingoingConnections = feature.getDstConnectionInstances();
			String inputAt = null;
			
			// if immediate/delayed connections are present, then ignore Input_Time
			if(ingoingConnections.size() > 0){
				// in DataPorts may have only one connection per mode. We don't consider modes at the moment, so there may only be one connection. 
				ConnectionInstance connection = ingoingConnections.get(0);
				List<PropertyExpression> timingProperties = connection.getPropertyValues("Communication_Properties", "Timing");
				
				if(timingProperties.size() > 0){
					EnumerationLiteral timingProperty = (EnumerationLiteral)((NamedValue)timingProperties.get(0)).getNamedValue();
					String timing = timingProperty.getName(); // sampled, immediate, delayed
					if(timing.equals("immediate"))
						inputAt = "Start"; // see AADL Standard 9.2.5 (50)
					if(timing.equals("delayed"))
						inputAt = "Dispatch";// see AADL Standard 9.2.5 (51)
					// sampled has no special semantic meaning for input and output timing
				}
			}
			
			// if NOT immediate/delayed consider Time part of Input_Time and IGNORE offset
			if(inputAt == null){
               List<PropertyExpression> inputTimeProperties = feature.getPropertyValues("Communication_Properties", "Input_Time");
               if(inputTimeProperties.size() > 0){
                   //Input_Time consists of a Time Part, which is an EnumerationLiteral and an Offset, which is a RangeValue
                   RecordValue inputTimeProperty = (RecordValue)inputTimeProperties.get(0);
                   // we ignor offset and are only interested in the Time part (Dispatch, Start, Completion, Deadline, NoIO)
                   NamedValue timePart = (NamedValue)inputTimeProperty.getOwnedFieldValues().get(0).getOwnedValue();
                   inputAt = ((EnumerationLiteral)timePart.getNamedValue()).getName();
               }
			}
			
			//if inputAt is null (default is Dispatch, see AADL Standard 8.3.2 (17)) or Dispatch, then create Input Statement
			if(inputAt == null || inputAt.equals("Dispatch")){
				getDataPortDispatchInputStatement(feature, sb);
			}
		}
		
		
		
//		if(direction.outgoing()){
//			//get all connections where this feature is the source
//			List<ConnectionInstance> outgoingConnections = feature.getSrcConnectionInstances();
//			// if immediate/delayed, then ignore input/output times?
//			
//			// if NOT immediate/delayed consider Time part of input/output time
//			List<PropertyExpression> inputTimeProperties = feature.getPropertyValues("Communication_Properties", "Input_Time");
//			List<PropertyExpression> outputTimeProperties = feature.getPropertyValues("Communication_Properties", "Output_Time");
//		}
		
	}
	
	private static void getDataPortDispatchInputStatement(FeatureInstance feature, StringBuilder sb ) {
		//we need the datatype of the port in order to know what we are reading/writing
		Classifier classifier = feature.getFeature().getClassifier();
		sb.append("" + Utils.getDataType(classifier));// DataType 
		sb.append(" " + feature.getName() + "Value"); // variable name is featurenameValue
		sb.append(" = " + feature.getName() + ".getValue();\n\t\t");// see AADL Standard 8.3.1 (12) for getValue()
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
    stringBuffer.append(TEXT_2);
    stringBuffer.append(getSubcomponentImportStatements(component));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(Utils.getClassName(component));
    stringBuffer.append(TEXT_4);
    stringBuffer.append(getSubcomponentMemberStatements(component));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(getFeatureMemberStatements(component));
    stringBuffer.append(TEXT_6);
    stringBuffer.append(Utils.getClassName(component));
    stringBuffer.append(TEXT_7);
    stringBuffer.append(getPriority(component));
    stringBuffer.append(TEXT_8);
    stringBuffer.append(getPeriodMilliSeconds(component));
    stringBuffer.append(TEXT_9);
    stringBuffer.append(getPeriodNanoSeconds(component));
    stringBuffer.append(TEXT_10);
    stringBuffer.append(getDispatchStatements(component));
    stringBuffer.append(TEXT_11);
    stringBuffer.append(TEXT_12);
    return stringBuffer.toString();
  }
}