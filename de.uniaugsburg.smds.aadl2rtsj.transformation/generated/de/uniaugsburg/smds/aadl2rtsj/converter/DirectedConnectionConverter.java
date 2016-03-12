package de.uniaugsburg.smds.aadl2rtsj.converter;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.logging.Logger;

import org.osate.aadl2.Classifier;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.ConnectionInstanceEnd;
import org.osate.aadl2.instance.FeatureInstance;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

public class DirectedConnectionConverter{
	
  protected static String nl;
  public static synchronized DirectedConnectionConverter create(String lineSeparator)
  {
    nl = lineSeparator;
    DirectedConnectionConverter result = new DirectedConnectionConverter();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL;
  protected final String TEXT_3 = NL;
  protected final String TEXT_4 = NL + NL + "public class ";
  protected final String TEXT_5 = "{" + NL + "\t" + NL + "\tprivate volatile ";
  protected final String TEXT_6 = " value = null;" + NL + "\tprivate boolean isDirty = false;" + NL + "\t" + NL + "\t/**" + NL + "\t * Write the value which can then be read be the target of this connection" + NL + "\t * @param value the new value for this connection" + NL + "\t */" + NL + "\tpublic void putValue(";
  protected final String TEXT_7 = " value){" + NL + "\t\tthis.value = value;" + NL + "\t\tisDirty = true;" + NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * Read the value which was written by the source of this connection." + NL + "\t * @return the value which was set by <code>putValue</code>" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_8 = " getValue(){" + NL + "\t\tisDirty = false;" + NL + "\t\treturn value;" + NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * Indicates whether this connection provides a new value or still the old, already read one." + NL + "\t * @return true if there's a new value, false otherwise" + NL + "\t */" + NL + "\tpublic boolean isDirty(){" + NL + "\t\treturn isDirty;" + NL + "\t}" + NL + "}";

	private static String getDataTypeImportStatement(ConnectionInstance connection){
		// we don't consider splitting or aggregation of data types currently, so it's sufficient to check one connectionend for its datatype
		// the end must be a FeatureInstance as we only consider data ports at the moment
		FeatureInstance feature = (FeatureInstance) connection.getSource();
		Classifier classifier = feature.getFeature().getClassifier();
		// if it is a base type, then we don't have to import anything, as Integer, Boolean etc are part of java.lang
		if(classifier == null || isBaseType(classifier))
			return "";
		return new ImportStatement().generate(classifier);
	}
	
	private static String getConnectionDataType(ConnectionInstance connection){
		// we don't consider splitting or aggregation of data types currently, so it's sufficient to check one connectionend for its datatype
		// the end must be a FeatureInstance as we only consider data ports at the moment
		return getDataType((FeatureInstance)connection.getSource());
	}
	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ConnectionInstance connection)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getPackageName(connection));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(getDataTypeImportStatement(connection));
    stringBuffer.append(TEXT_4);
    stringBuffer.append(getClassName(connection));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(getConnectionDataType(connection));
    stringBuffer.append(TEXT_6);
    stringBuffer.append(getConnectionDataType(connection));
    stringBuffer.append(TEXT_7);
    stringBuffer.append(getConnectionDataType(connection));
    stringBuffer.append(TEXT_8);
    return stringBuffer.toString();
  }
}