package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.InstanceObject;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

public class ReceiveInputStatement{
  protected static String nl;
  public static synchronized ReceiveInputStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    ReceiveInputStatement result = new ReceiveInputStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t";
  protected final String TEXT_2 = ".receiveInput();" + NL + "\t";

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(InstanceObject object)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(object.getName());
    stringBuffer.append(TEXT_2);
    return stringBuffer.toString();
  }
}