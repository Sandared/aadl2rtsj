package de.uniaugsburg.aadl2rtsj.converter;

import de.uniaugsburg.aadl2rtsj.utils.Utils;
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
  protected final String TEXT_3 = NL + "\t/*" + NL + "\t * Subcomponents" + NL + "\t */ ";
  protected final String TEXT_4 = NL + "\t";
  protected final String TEXT_5 = " ";
  protected final String TEXT_6 = " = new ";
  protected final String TEXT_7 = "();" + NL + "\t\t";
  protected final String TEXT_8 = NL + "\t";
  protected final String TEXT_9 = NL + "\t";
  protected final String TEXT_10 = NL + "\t/*" + NL + "\t * FEATURES" + NL + "\t */\t";
  protected final String TEXT_11 = NL + "\t";
  protected final String TEXT_12 = " ";
  protected final String TEXT_13 = " = new ";
  protected final String TEXT_14 = "();" + NL + "\t\t";
  protected final String TEXT_15 = NL + "\t";
  protected final String TEXT_16 = NL + "}";
  protected final String TEXT_17 = NL;

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ComponentInstance component)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(Utils.className(component.getName()));
    stringBuffer.append(TEXT_2);
    if(component.getAllComponentInstances().size() > 1)/*the component itself is always in this list*/{
    stringBuffer.append(TEXT_3);
    for(ComponentInstance subcomponent : component.getAllComponentInstances()){
    stringBuffer.append(TEXT_4);
    stringBuffer.append(Utils.className(subcomponent.getName()));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(subcomponent.getName());
    stringBuffer.append(TEXT_6);
    stringBuffer.append(Utils.className(subcomponent.getName()));
    stringBuffer.append(TEXT_7);
    }
    stringBuffer.append(TEXT_8);
    }
    stringBuffer.append(TEXT_9);
    if(component.getAllFeatureInstances().size() > 0){
    stringBuffer.append(TEXT_10);
    for(FeatureInstance feature : component.getAllFeatureInstances()){
    stringBuffer.append(TEXT_11);
    stringBuffer.append(Utils.className(feature.getName()));
    stringBuffer.append(TEXT_12);
    stringBuffer.append(feature.getName());
    stringBuffer.append(TEXT_13);
    stringBuffer.append(Utils.className(feature.getName()));
    stringBuffer.append(TEXT_14);
    }
    stringBuffer.append(TEXT_15);
    }
    stringBuffer.append(TEXT_16);
    stringBuffer.append(TEXT_17);
    return stringBuffer.toString();
  }
}