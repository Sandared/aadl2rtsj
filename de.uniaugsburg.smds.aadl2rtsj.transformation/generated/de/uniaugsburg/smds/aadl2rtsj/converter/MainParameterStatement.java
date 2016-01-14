package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.InstanceObject;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.List;
import java.util.Map;

public class MainParameterStatement{
  protected static String nl;
  public static synchronized MainParameterStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    MainParameterStatement result = new MainParameterStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(InstanceObject object, String name)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    return stringBuffer.toString();
  }
}