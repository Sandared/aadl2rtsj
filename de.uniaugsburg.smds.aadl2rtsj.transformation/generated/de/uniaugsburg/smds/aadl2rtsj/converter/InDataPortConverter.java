package de.uniaugsburg.smds.aadl2rtsj.converter;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.List;
import java.util.logging.Logger;

import org.osate.aadl2.Classifier;
import org.osate.aadl2.instance.ConnectionInstance;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

import de.uniaugsburg.smds.aadl2rtsj.utils.Utils;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.FeatureInstance;

public class InDataPortConverter{
	
  protected static String nl;
  public static synchronized InDataPortConverter create(String lineSeparator)
  {
    nl = lineSeparator;
    InDataPortConverter result = new InDataPortConverter();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL;
  protected final String TEXT_3 = NL;
  protected final String TEXT_4 = NL;
  protected final String TEXT_5 = NL;
  protected final String TEXT_6 = NL + NL + "public class ";
  protected final String TEXT_7 = "{" + NL + "\t" + NL + "\tprivate ";
  protected final String TEXT_8 = " value = null;" + NL + "\tprivate boolean isNew = false;" + NL + "\t";
  protected final String TEXT_9 = NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_10 = "(";
  protected final String TEXT_11 = "){" + NL + "\t\t";
  protected final String TEXT_12 = NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * This method returns the data that was made available by calling <code>receiveInput()</code><br>" + NL + "\t * If there has been no call to <code>receiveInput()</code> since the last call, then the old value <br>" + NL + "\t * of this data port is returned" + NL + "\t * @return the currently available data for this port" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_13 = " getValue(){" + NL + "\t\tisNew = false;" + NL + "\t\treturn value;" + NL + "\t}" + NL + "" + NL + "\t";
  protected final String TEXT_14 = NL + "\t" + NL + "\t/**" + NL + "\t * As a Data Port only can have one data at one time, a return value of 1<br>" + NL + "\t * can be considered as \"new data available\", as well as a return value of 0 can be considered as <br>" + NL + "\t * \"no new data available\"" + NL + "\t * @return number of data received at this port since the last call to <code>getValue()</code>" + NL + "\t */" + NL + "\tpublic int getCount(){" + NL + "\t\treturn (isNew)? 1:0;" + NL + "\t}" + NL + "\t" + NL + "}";

	private static String getDataTypeImportStatement(FeatureInstance feature){
		Classifier classifier = feature.getFeature().getClassifier();
		// if it is a base type, then we don't have to import anything, as Integer, Boolean etc are part of java.lang
		if(classifier == null || isBaseType(classifier))
			return "";
		return new ImportStatement().generate(classifier);
	}
	
	private static String getConnectionImportStatements(FeatureInstance feature){
		StringBuilder sb = new StringBuilder();
		//for all connections we have to create an import statement
		// actually this should only be one, as we don't consider modes
		for (ConnectionInstance connection : feature.getDstConnectionInstances()) {
			sb.append(new ImportStatement().generate(connection));
		}
		return sb.toString().trim();
	}
	
	private static String getConnectionMemberStatements(FeatureInstance feature){
		StringBuilder sb = new StringBuilder();
		//for all connections we have to create an import statement
		// actually this should only be one, as we don't consider modes
		for (ConnectionInstance connection : feature.getDstConnectionInstances()) {
			sb.append(new DeclarationMemberStatement().generate(connection));
		}
		return sb.toString().trim();
	}
	
	private static String getReceiveInputMethods(FeatureInstance feature){
		StringBuilder sb = new StringBuilder();
		//for all connections we have to create an import statement
		// actually this should only be one, as we don't consider modes 
		for (ConnectionInstance connection : feature.getDstConnectionInstances()) {
			sb.append(new ReceiveInputMethod().generate(feature, connection));
		}
		return sb.toString().trim();
	}
	
	private static String getConstructorParameters(FeatureInstance feature){
		List<ConnectionInstance> connections = feature.getDstConnectionInstances();
		// for each connection we have to create a parameter
		if(connections.size() > 0){
			StringBuilder sb = new StringBuilder();
			for(ConnectionInstance connection : connections){
				sb.append(new MethodParameterStatement().generate(connection));
			}
			// delete pending commata ", "
			sb.delete(sb.length()-2, sb.length());
			return sb.toString().trim();
		}
		return "";
	}
	
	private static String getConstructorMemberAssignments(FeatureInstance feature){
		List<ConnectionInstance> connections = feature.getDstConnectionInstances();
		// for each connection we have to create an assignment
		if(connections.size() > 0){
			StringBuilder sb = new StringBuilder();
			for(ConnectionInstance connection : connections){
				sb.append(new ConstructorAssignmentStatement().generate(connection));
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
	public String generate(FeatureInstance feature)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getPackageName(feature));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(getDataTypeImportStatement(feature));
    stringBuffer.append(TEXT_4);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(getConnectionImportStatements(feature));
    stringBuffer.append(TEXT_6);
    stringBuffer.append(getClassName(feature));
    stringBuffer.append(TEXT_7);
    stringBuffer.append(getDataType(feature));
    stringBuffer.append(TEXT_8);
    stringBuffer.append(getConnectionMemberStatements(feature));
    stringBuffer.append(TEXT_9);
    stringBuffer.append(getClassName(feature));
    stringBuffer.append(TEXT_10);
    stringBuffer.append(getConstructorParameters(feature));
    stringBuffer.append(TEXT_11);
    stringBuffer.append(getConstructorMemberAssignments(feature));
    stringBuffer.append(TEXT_12);
    stringBuffer.append(getDataType(feature));
    stringBuffer.append(TEXT_13);
    stringBuffer.append(getReceiveInputMethods(feature));
    stringBuffer.append(TEXT_14);
    return stringBuffer.toString();
  }
}