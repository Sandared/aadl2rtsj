package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.ConnectionInstance;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

public class SynchronisationObjectMethodParameterStatement{
  protected static String nl;
  public static synchronized SynchronisationObjectMethodParameterStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    SynchronisationObjectMethodParameterStatement result = new SynchronisationObjectMethodParameterStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "Object ";
  protected final String TEXT_2 = ", ";

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
    return stringBuffer.toString();
  }
}