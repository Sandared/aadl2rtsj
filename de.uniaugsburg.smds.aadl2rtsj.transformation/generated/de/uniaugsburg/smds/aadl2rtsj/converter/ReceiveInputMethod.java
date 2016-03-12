package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.InstanceObject;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

public class ReceiveInputMethod{
  protected static String nl;
  public static synchronized ReceiveInputMethod create(String lineSeparator)
  {
    nl = lineSeparator;
    ReceiveInputMethod result = new ReceiveInputMethod();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t/**" + NL + "\t * Freezes the currently available input on this Data Port.<br>" + NL + "\t * Afterwards the frozen value can be retrieved by calling <code>getValue()</code>" + NL + "\t */" + NL + "\tpublic void receiveInput(){" + NL + "\t\tvalue = ";
  protected final String TEXT_2 = ".getValue();" + NL + "\t\tisNew = true;" + NL + "\t}" + NL + "\t";
  protected final String TEXT_3 = NL;

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(InstanceObject object, ConnectionInstance connection)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getObjectName(connection));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}