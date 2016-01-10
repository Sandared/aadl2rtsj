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
  protected final String TEXT_2 = ";" + NL + "" + NL + "// some imports " + NL + "// DataType of \"value\"";
  protected final String TEXT_3 = NL;
  protected final String TEXT_4 = NL + "// Connections if any";
  protected final String TEXT_5 = NL;
  protected final String TEXT_6 = NL + NL + "public class ";
  protected final String TEXT_7 = "{" + NL + "\t";
  protected final String TEXT_8 = " value = null;" + NL + "\t";
  protected final String TEXT_9 = NL + "\t" + NL + "\tpublic void putValue(";
  protected final String TEXT_10 = " value){" + NL + "\t\tthis.value = value;" + NL + "\t}" + NL + "\t" + NL + "\t";
  protected final String TEXT_11 = NL + "}";

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
			sb.append(new MemberStatement().generate(connection));
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
    stringBuffer.append(getDataType(feature));
    stringBuffer.append(TEXT_10);
    stringBuffer.append(getSendOuputMethods(feature));
    stringBuffer.append(TEXT_11);
    return stringBuffer.toString();
  }
}