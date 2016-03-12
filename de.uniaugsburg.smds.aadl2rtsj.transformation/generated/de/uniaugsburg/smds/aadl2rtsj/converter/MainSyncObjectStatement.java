package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.ConnectionInstance;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.Map;

public class MainSyncObjectStatement{
  protected static String nl;
  public static synchronized MainSyncObjectStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    MainSyncObjectStatement result = new MainSyncObjectStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t\tObject connection_";
  protected final String TEXT_2 = "_Sync = new Object();";
  protected final String TEXT_3 = NL;

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ConnectionInstance object, long counter)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(counter);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}