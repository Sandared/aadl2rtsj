package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.InstanceObject;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.List;
import java.util.Map;

public class MainThreadStartStatement{
  protected static String nl;
  public static synchronized MainThreadStartStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    MainThreadStartStatement result = new MainThreadStartStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t\t";
  protected final String TEXT_2 = ".startExecution(); ";
  protected final String TEXT_3 = NL;

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ComponentInstance component, Map<InstanceObject, String> names)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(names.get(component));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}