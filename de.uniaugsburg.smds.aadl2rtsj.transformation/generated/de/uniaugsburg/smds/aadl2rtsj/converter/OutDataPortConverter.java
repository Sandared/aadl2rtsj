package de.uniaugsburg.smds.aadl2rtsj.converter;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

import java.util.List;
import java.util.logging.Logger;

import org.osate.aadl2.Classifier;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.FeatureInstance ;

public class OutDataPortConverter{
  protected static String nl;
  public static synchronized OutDataPortConverter create(String lineSeparator)
  {
    nl = lineSeparator;
    OutDataPortConverter result = new OutDataPortConverter();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL;
  protected final String TEXT_3 = NL;
  protected final String TEXT_4 = NL;
  protected final String TEXT_5 = NL + NL + "public class ";
  protected final String TEXT_6 = "{" + NL + "\t" + NL + "\tprivate ";
  protected final String TEXT_7 = " value = null;" + NL + "\t";
  protected final String TEXT_8 = NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_9 = "(";
  protected final String TEXT_10 = "){" + NL + "\t\t";
  protected final String TEXT_11 = NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * This method stores the given value and everytime a <code>sendOutputOnXXX()</code> occurs,<br>" + NL + "\t * then this value is written to the respective connection" + NL + "\t * @param the value that shall be written to a connection, the next time <code>sendOuntuptOnXXX()</code> is called" + NL + "\t */" + NL + "\tpublic void putValue(";
  protected final String TEXT_12 = " value){" + NL + "\t\tthis.value = value;" + NL + "\t}" + NL + "\t" + NL + "\t";
  protected final String TEXT_13 = NL + "}";

	private static final Logger log = Logger.getLogger( OutDataPortConverter.class.getName() );
	
	private static String getDataTypeImportStatement(FeatureInstance feature){
		Classifier classifier = feature.getFeature().getClassifier();
		// if it is a base type, then we don't have to import anything, as Integer, Boolean etc are part of java.lang
		if(isBaseType(classifier))
			return "";
		return new ImportStatement().generate(classifier);
	}
	
	private static String getConnectionImportStatements(FeatureInstance feature){
		StringBuilder sb = new StringBuilder();
		//for all connections we have to create an import statement
		for (ConnectionInstance connection : feature.getSrcConnectionInstances()) {
			sb.append(new ImportStatement().generate(connection));
		}
		return sb.toString().trim();
	}
	
	private static String getConnectionMemberStatements(FeatureInstance feature){
		StringBuilder sb = new StringBuilder();
		//for all connections we have to create an import statement
		for (ConnectionInstance connection : feature.getSrcConnectionInstances()) {
			sb.append(new DeclarationMemberStatement().generate(connection));
		}
		return sb.toString().trim();
	}
	
	private static String getSendOuputMethods(FeatureInstance feature){
		StringBuilder sb = new StringBuilder();
		//for all connections we have to create an import statement
		for (ConnectionInstance connection : feature.getSrcConnectionInstances()) {
			sb.append(new SendOutputMethod().generate(feature, connection));
		}
		return sb.toString().trim();
	}
	
	private static String getConstructorParameters(FeatureInstance feature){
		List<ConnectionInstance> connections = feature.getSrcConnectionInstances();
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
		List<ConnectionInstance> connections = feature.getSrcConnectionInstances();
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
    stringBuffer.append(getConnectionImportStatements(feature));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(getClassName(feature));
    stringBuffer.append(TEXT_6);
    stringBuffer.append(getDataType(feature));
    stringBuffer.append(TEXT_7);
    stringBuffer.append(getConnectionMemberStatements(feature));
    stringBuffer.append(TEXT_8);
    stringBuffer.append(getClassName(feature));
    stringBuffer.append(TEXT_9);
    stringBuffer.append(getConstructorParameters(feature));
    stringBuffer.append(TEXT_10);
    stringBuffer.append(getConstructorMemberAssignments(feature));
    stringBuffer.append(TEXT_11);
    stringBuffer.append(getDataType(feature));
    stringBuffer.append(TEXT_12);
    stringBuffer.append(getSendOuputMethods(feature));
    stringBuffer.append(TEXT_13);
    return stringBuffer.toString();
  }
}