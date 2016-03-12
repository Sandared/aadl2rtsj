package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.ConnectionInstance;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

public class SynchronisationObjectDeclarationMemberStatement{
  protected static String nl;
  public static synchronized SynchronisationObjectDeclarationMemberStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    SynchronisationObjectDeclarationMemberStatement result = new SynchronisationObjectDeclarationMemberStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\tprivate Object ";
  protected final String TEXT_2 = ";";
  protected final String TEXT_3 = NL;

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
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}