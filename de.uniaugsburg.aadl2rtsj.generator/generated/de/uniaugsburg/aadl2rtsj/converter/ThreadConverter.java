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
  protected final String TEXT_1 = NL + "import javax.realtime.Timer;" + NL + "import javax.realtime.AsynchronousEventHandler;" + NL;
  protected final String TEXT_2 = NL + "// Import all needed features";
  protected final String TEXT_3 = NL + "import ";
  protected final String TEXT_4 = NL + NL + NL + NL + "public class ";
  protected final String TEXT_5 = "{" + NL + "\t";
  protected final String TEXT_6 = NL + "\t/*" + NL + "\t * FEATURES" + NL + "\t */" + NL + "\t\t";
  protected final String TEXT_7 = NL + "\t";
  protected final String TEXT_8 = " ";
  protected final String TEXT_9 = " = new ";
  protected final String TEXT_10 = "();" + NL + "\t" + NL + "\t" + NL + "\t\t";
  protected final String TEXT_11 = NL + "\t";
  protected final String TEXT_12 = NL + "}";
  protected final String TEXT_13 = NL;

	/**
	 * Turns strings into a form that is expected in Java to be a class name, e.g. first letter upper cased
	 * @param name the (probably lowercased) String 
	 * @return a string that looks like a class name
	 */
	private String className(String name){
		StringBuilder b = new StringBuilder(name);
		b.replace(0, 1, b.substring(0,1).toUpperCase());
		return b.toString();
	}
	
	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ComponentInstance thread)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    if(thread.getAllFeatureInstances().size() > 0){
    stringBuffer.append(TEXT_2);
    for(FeatureInstance feature : thread.getAllFeatureInstances()){
    stringBuffer.append(TEXT_3);
    stringBuffer.append(thread.getNamespace() + "." + feature.getName());
    }
    }
    stringBuffer.append(TEXT_4);
    stringBuffer.append(thread.getName());
    stringBuffer.append(TEXT_5);
    if(thread.getAllFeatureInstances().size() > 0){
    stringBuffer.append(TEXT_6);
    for(FeatureInstance feature : thread.getAllFeatureInstances()){
    stringBuffer.append(TEXT_7);
    stringBuffer.append(className(feature.getName()));
    stringBuffer.append(TEXT_8);
    stringBuffer.append(feature.getName());
    stringBuffer.append(TEXT_9);
    stringBuffer.append(className(feature.getName()));
    stringBuffer.append(TEXT_10);
    }
    stringBuffer.append(TEXT_11);
    }
    stringBuffer.append(TEXT_12);
    stringBuffer.append(TEXT_13);
    return stringBuffer.toString();
  }
}