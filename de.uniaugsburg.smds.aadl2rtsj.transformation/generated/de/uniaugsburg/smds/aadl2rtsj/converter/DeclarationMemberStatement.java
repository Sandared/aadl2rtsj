package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.NamedElement;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

public class DeclarationMemberStatement{
  protected static String nl;
  public static synchronized DeclarationMemberStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    DeclarationMemberStatement result = new DeclarationMemberStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\tprivate ";
  protected final String TEXT_2 = " ";
  protected final String TEXT_3 = ";";

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(NamedElement object)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getClassName(object));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(getObjectName(object));
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}