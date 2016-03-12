package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.ConnectionInstance;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

public class SynchronisationNotifyStatement{
  protected static String nl;
  public static synchronized SynchronisationNotifyStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    SynchronisationNotifyStatement result = new SynchronisationNotifyStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t\tsynchronized(";
  protected final String TEXT_2 = "){" + NL + "\t\t\t";
  protected final String TEXT_3 = ".notifyAll();" + NL + "\t\t}";
  protected final String TEXT_4 = NL;

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ConnectionInstance connection)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getSynchronisationObjectName(connection));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(getSynchronisationObjectName(connection));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}