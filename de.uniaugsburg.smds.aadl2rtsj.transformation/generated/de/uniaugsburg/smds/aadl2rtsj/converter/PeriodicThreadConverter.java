package de.uniaugsburg.smds.aadl2rtsj.converter;

import java.util.List;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.FeatureInstance;
import de.uniaugsburg.smds.aadl2rtsj.utils.Utils;

import org.osate.aadl2.Classifier;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.Feature;
import org.osate.aadl2.FeatureClassifier;
import org.osate.aadl2.IntegerLiteral;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.PropertyExpression;
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
						
						// TODO: how to distinguish between in and inout port? and IF it is an inout port, 
						// then how to distinguish between the connections? which one is incoming and which one is ougoing?
						
						// we need information about the type of connection (immediate/delayed/sampled) and how many are there
						List<ConnectionInstance> connections = feature.getAllEnclosingConnectionInstances();
						
						// if immediate/delayed, then ignore input/output times?
						
						// if NOT immediate/delayed consider Time part of input/output time
						List<PropertyExpression> inputTimeProperties = feature.getPropertyValues("Communication_Properties", "Input_Time");
						
						// read a value if it is incoming
						DirectionType direction = feature.getDirection();
						//we need the datatype of the port
						Feature feature2 = feature.getFeature();
						Classifier var1 = feature2.getClassifier();
						FeatureClassifier var2 = feature2.getFeatureClassifier();
						if(direction.incoming()){
							
						}
						break;
	
					default:
						break;
					}
			}
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