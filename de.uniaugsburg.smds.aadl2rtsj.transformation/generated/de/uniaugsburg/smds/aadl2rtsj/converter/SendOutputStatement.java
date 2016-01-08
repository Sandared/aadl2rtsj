package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.InstanceObject;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

public class SendOutputStatement{
  protected static String nl;
  public static synchronized SendOutputStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    SendOutputStatement result = new SendOutputStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t";
  protected final String TEXT_2 = ".sendOutputOn";
  protected final String TEXT_3 = "();";
  protected final String TEXT_4 = NL;

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(InstanceObject object, ConnectionInstance connection)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(object.getName());
    stringBuffer.append(TEXT_2);
    stringBuffer.append(getClassName(connection.getConnectionReferences().get(0).getConnection()));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}