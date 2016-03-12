package de.uniaugsburg.smds.aadl2rtsj.converter;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;
import de.uniaugsburg.smds.aadl2rtsj.utils.OffsetTime;

import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.FeatureInstance;

import java.util.List;
import java.util.logging.Logger;

import org.osate.aadl2.Classifier;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.instance.ConnectionInstance;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

public class IOHandlerConverter{	
	
  protected static String nl;
  public static synchronized IOHandlerConverter create(String lineSeparator)
  {
    nl = lineSeparator;
    IOHandlerConverter result = new IOHandlerConverter();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL + "" + NL + "import javax.realtime.PriorityParameters;" + NL + "import javax.realtime.BoundAsyncEventHandler;" + NL + "import ";
  protected final String TEXT_3 = ".";
  protected final String TEXT_4 = ";" + NL + "" + NL + "public class ";
  protected final String TEXT_5 = " extends BoundAsyncEventHandler{" + NL + "\tprivate ";
  protected final String TEXT_6 = " ";
  protected final String TEXT_7 = ";" + NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_8 = "(";
  protected final String TEXT_9 = " ";
  protected final String TEXT_10 = "){" + NL + "\t\tthis.";
  protected final String TEXT_11 = " = ";
  protected final String TEXT_12 = ";" + NL + "\t\tsetSchedulingParameters(new PriorityParameters(";
  protected final String TEXT_13 = "));" + NL + "\t\t// TODO: AperiodicParameters for deadline/cost/misshandler/overrunhandler?" + NL + "\t}" + NL + "\t" + NL + "\t@Override" + NL + "\tpublic void handleAsyncEvent() {" + NL + "\t\t";
  protected final String TEXT_14 = NL + "\t}" + NL + "}";

	private static String getIOStatement(FeatureInstance feature, OffsetTime time){
		DirectionType direction = feature.getDirection();
		if(direction.incoming() && !direction.outgoing())
			return new ReceiveInputStatement().generate(feature).trim();
		if(direction.outgoing() && !direction.incoming())
			return new SendOutputStatement().generate(feature, time.getConnection()).trim();
		// TODO: In Out Data port!
		return "In Out data port must still be done";
	}
	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ComponentInstance component, FeatureInstance feature, OffsetTime time)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getPackageName(component));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(getPackageName(feature));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(getClassName(feature));
    stringBuffer.append(TEXT_4);
    stringBuffer.append(getHandlerClassName(feature, time));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(getClassName(feature));
    stringBuffer.append(TEXT_6);
    stringBuffer.append(getObjectName(feature));
    stringBuffer.append(TEXT_7);
    stringBuffer.append(getHandlerClassName(feature, time));
    stringBuffer.append(TEXT_8);
    stringBuffer.append(getClassName(feature));
    stringBuffer.append(TEXT_9);
    stringBuffer.append(getObjectName(feature));
    stringBuffer.append(TEXT_10);
    stringBuffer.append(getObjectName(feature));
    stringBuffer.append(TEXT_11);
    stringBuffer.append(getObjectName(feature));
    stringBuffer.append(TEXT_12);
    stringBuffer.append(getPriority(component));
    stringBuffer.append(TEXT_13);
    stringBuffer.append(getIOStatement(feature, time));
    stringBuffer.append(TEXT_14);
    return stringBuffer.toString();
  }
}