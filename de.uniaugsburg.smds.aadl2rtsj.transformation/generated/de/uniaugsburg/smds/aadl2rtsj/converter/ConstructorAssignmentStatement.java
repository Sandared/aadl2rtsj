package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.NamedElement;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

public class ConstructorAssignmentStatement{
  protected static String nl;
  public static synchronized ConstructorAssignmentStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    ConstructorAssignmentStatement result = new ConstructorAssignmentStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t\tthis.";
  protected final String TEXT_2 = " = ";
  protected final String TEXT_3 = ";";
  protected final String TEXT_4 = NL;

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
    stringBuffer.append(getObjectName(object));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}