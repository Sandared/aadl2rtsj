package de.uniaugsburg.aadl2rtsj.converter;

import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.FeatureInstance;

public class ThreadConverter{
  protected static String nl;
  public static synchronized ThreadConverter create(String lineSeparator)
  {
    nl = lineSeparator;
    ThreadConverter result = new ThreadConverter();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = NL + "public class ";
  protected final String TEXT_2 = "{" + NL + "\t";
  protected final String TEXT_3 = NL + "\t/*" + NL + "\t * FEATURES" + NL + "\t */" + NL + "\t";
  protected final String TEXT_4 = NL + "\t" + NL + "\t";
  protected final String TEXT_5 = NL + "\t";
  protected final String TEXT_6 = NL + "}";
  protected final String TEXT_7 = NL;

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ComponentInstance thread)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(thread.getName());
    stringBuffer.append(TEXT_2);
    if(thread.getAllFeatureInstances().size() > 0){
    stringBuffer.append(TEXT_3);
    for(FeatureInstance feature : thread.getAllFeatureInstances()){
    stringBuffer.append(TEXT_4);
    }
    stringBuffer.append(TEXT_5);
    }
    stringBuffer.append(TEXT_6);
    stringBuffer.append(TEXT_7);
    return stringBuffer.toString();
  }
}