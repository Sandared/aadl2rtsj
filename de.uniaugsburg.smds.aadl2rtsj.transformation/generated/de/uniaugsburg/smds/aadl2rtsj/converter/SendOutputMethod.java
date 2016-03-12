package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.InstanceObject;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

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
  protected final String TEXT_1 = "\t/**" + NL + "\t * Sends the currently available value to connection <code>";
  protected final String TEXT_2 = "</code><br>" + NL + "\t * The value has to be made available by calling <code>putValue()</code> before" + NL + "\t */" + NL + "\tpublic void sendOutputOn";
  protected final String TEXT_3 = "(){" + NL + "\t\t";
  protected final String TEXT_4 = ".putValue(value);" + NL + "\t\t";
  protected final String TEXT_5 = NL + "\t}" + NL;
  protected final String TEXT_6 = NL;

	private static String getNotifyStatement(ConnectionInstance connection){
		StringBuilder sb = new StringBuilder();
		String timing = getTiming(connection);
		if(timing.equals(Communication_Properties_Timing_Immediate)){
			sb.append(new SynchronisationNotifyStatement().generate(connection)); 
		}
		return sb.toString().trim();
	}
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
    stringBuffer.append(getNotifyStatement(connection));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(TEXT_6);
    return stringBuffer.toString();
  }
}