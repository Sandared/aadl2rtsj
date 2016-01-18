package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.NamedElement;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

public class PutValueStatement{
  protected static String nl;
  public static synchronized PutValueStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    PutValueStatement result = new PutValueStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t";
  protected final String TEXT_2 = ".putValue(value);" + NL + "\t";

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(NamedElement object)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getObjectName(object));
    stringBuffer.append(TEXT_2);
    return stringBuffer.toString();
  }
}