package de.uniaugsburg.smds.aadl2rtsj.converter;

import de.uniaugsburg.smds.aadl2rtsj.utils.Utils;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.FeatureInstance;

public class DataPortConverter{
  protected static String nl;
  public static synchronized DataPortConverter create(String lineSeparator)
  {
    nl = lineSeparator;
    DataPortConverter result = new DataPortConverter();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "public class ";
  protected final String TEXT_2 = "{" + NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_3 = " getValue(){" + NL + "\t\t//TODO: generate logic for freezing etc." + NL + "\t\treturn null;" + NL + "\t}" + NL + "\t" + NL + "}";

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(FeatureInstance feature)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(Utils.getClassName(feature));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(Utils.getDataType(feature.getFeature().getClassifier()));
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}