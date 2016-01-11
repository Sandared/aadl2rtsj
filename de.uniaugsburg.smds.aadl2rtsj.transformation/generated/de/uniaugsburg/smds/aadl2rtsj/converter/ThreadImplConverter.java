package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.ThreadImplementation;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.List;

public class ThreadImplConverter{
	
  protected static String nl;
  public static synchronized ThreadImplConverter create(String lineSeparator)
  {
    nl = lineSeparator;
    ThreadImplConverter result = new ThreadImplConverter();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ThreadImplementation component)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    return stringBuffer.toString();
  }
}