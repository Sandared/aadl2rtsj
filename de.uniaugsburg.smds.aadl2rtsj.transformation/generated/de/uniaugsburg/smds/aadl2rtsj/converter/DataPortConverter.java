package de.uniaugsburg.smds.aadl2rtsj.converter;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.logging.Logger;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

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
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL + "" + NL + "public class ";
  protected final String TEXT_3 = "{" + NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_4 = " getValue(){" + NL + "\t\t//TODO: generate logic for freezing etc." + NL + "\t\treturn null;" + NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * Freezes the current data for next dispatch execution, see AADL Standard 8.3.2 (21)" + NL + "\t */" + NL + "\tpublic void receiveInput(){" + NL + "\t\t" + NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * Returns the amount of data " + NL + "\t * @return number of data received at this port" + NL + "\t */" + NL + "\tpublic int getCount(){" + NL + "\t\treturn -1;" + NL + "\t}" + NL + "\t" + NL + "}";

	private static final Logger log = Logger.getLogger( DataPortConverter.class.getName() );
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
    stringBuffer.append(getClassName(feature));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(getDataType(feature));
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}