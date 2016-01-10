package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.InstanceObject;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

public class SendOutputMethod{
  protected static String nl;
  public static synchronized SendOutputMethod create(String lineSeparator)
  {
    nl = lineSeparator;
    SendOutputMethod result = new SendOutputMethod();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t/**" + NL + "\t * Sends the currently available value, which was made available through putValue(), to the connection ";
  protected final String TEXT_2 = NL + "\t */" + NL + "\tpublic void sendOutputOn";
  protected final String TEXT_3 = "(){" + NL + "\t\t";
  protected final String TEXT_4 = ".putValueFor";
  protected final String TEXT_5 = "(value);" + NL + "\t}" + NL;
  protected final String TEXT_6 = NL;

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
    stringBuffer.append(getClassName(getConnection(connection)));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(getObjectName(connection));
    stringBuffer.append(TEXT_4);
    stringBuffer.append(getClassName(object));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(TEXT_6);
    return stringBuffer.toString();
  }
}