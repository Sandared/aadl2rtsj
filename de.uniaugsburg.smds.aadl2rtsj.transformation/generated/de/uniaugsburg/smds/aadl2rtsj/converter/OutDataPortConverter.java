package de.uniaugsburg.smds.aadl2rtsj.converter;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

import java.util.logging.Logger;
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
  protected final String TEXT_4 = NL + "// Receiver if any";
  protected final String TEXT_5 = NL;
  protected final String TEXT_6 = NL + NL + "public class ";
  protected final String TEXT_7 = "{" + NL + "\t";
  protected final String TEXT_8 = NL + "\t";
  protected final String TEXT_9 = NL + "\t" + NL + "\tpublic void putValue(";
  protected final String TEXT_10 = " value){" + NL + "\t\t" + NL + "\t}" + NL + "\t" + NL + "\t";
  protected final String TEXT_11 = NL + "}";

	private static final Logger log = Logger.getLogger( OutDataPortConverter.class.getName() );
	
	private static String getDataTypeImportStatement(FeatureInstance feature){
		
		return null;
	}
	
	private static String getReceiverImportStatements(FeatureInstance feature){
		
		return null;
	}
	
	private static String getPortVariableMemberStatements(FeatureInstance feature){
		// TODO: pro connection zwei port variablen? Alter und neuer wert?
		
		return null;
	}

	private static String getReceiverMemberStatements(FeatureInstance feature){
		
		return null;
	}
	
	private static String getSendOuputMethods(FeatureInstance feature){
		return null;
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
    stringBuffer.append(getReceiverImportStatements(feature));
    stringBuffer.append(TEXT_6);
    stringBuffer.append(getClassName(feature));
    stringBuffer.append(TEXT_7);
    stringBuffer.append(getPortVariableMemberStatements(feature));
    stringBuffer.append(TEXT_8);
    stringBuffer.append(getReceiverMemberStatements(feature));
    stringBuffer.append(TEXT_9);
    stringBuffer.append(getDataType(feature));
    stringBuffer.append(TEXT_10);
    stringBuffer.append(getSendOuputMethods(feature));
    stringBuffer.append(TEXT_11);
    return stringBuffer.toString();
  }
}