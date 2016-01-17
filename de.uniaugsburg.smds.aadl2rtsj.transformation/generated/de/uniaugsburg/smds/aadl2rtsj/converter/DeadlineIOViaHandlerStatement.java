package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.NamedElement;
import org.osate.aadl2.instance.FeatureInstance;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;
import de.uniaugsburg.smds.aadl2rtsj.utils.OffsetTime;

public class DeadlineIOViaHandlerStatement{
  protected static String nl;
  public static synchronized DeadlineIOViaHandlerStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    DeadlineIOViaHandlerStatement result = new DeadlineIOViaHandlerStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t\tTimer timerFor";
  protected final String TEXT_2 = " = new OneShotTimer(timer.getFireTime(), new ";
  protected final String TEXT_3 = "(";
  protected final String TEXT_4 = "));" + NL + "\t\ttimerFor";
  protected final String TEXT_5 = ".start();";
  protected final String TEXT_6 = NL;

	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(FeatureInstance feature, OffsetTime time)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getHandlerClassName(feature, time));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(getHandlerClassName(feature, time));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(getObjectName(feature));
    stringBuffer.append(TEXT_4);
    stringBuffer.append(getHandlerClassName(feature, time));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(TEXT_6);
    return stringBuffer.toString();
  }
}