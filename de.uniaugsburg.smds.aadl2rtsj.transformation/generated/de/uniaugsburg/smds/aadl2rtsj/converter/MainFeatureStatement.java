package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.DirectionType;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

import java.util.List;
import java.util.Map;

public class MainFeatureStatement{
	
  protected static String nl;
  public static synchronized MainFeatureStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    MainFeatureStatement result = new MainFeatureStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t\t";
  protected final String TEXT_2 = ".";
  protected final String TEXT_3 = " feature_";
  protected final String TEXT_4 = " = new ";
  protected final String TEXT_5 = ".";
  protected final String TEXT_6 = "(";
  protected final String TEXT_7 = ");";
  protected final String TEXT_8 = NL;

	private static String getParameters(FeatureInstance object, Map<InstanceObject, String> names){
		StringBuilder sb = new StringBuilder();
		
		List<ConnectionInstance> connections = null;
		
		// decide if it is an ingoing or outgoing dataport
		DirectionType direction = object.getDirection();
		if(direction.incoming() && !direction.outgoing()){
			connections = object.getDstConnectionInstances();
		}
		if(direction.outgoing() && !direction.incoming()){
			connections = object.getSrcConnectionInstances();
		}
		
		for (ConnectionInstance connection : connections) {
			sb.append(names.get(connection) + ", ");
			
			//consider SynchronisationObjects
			//must only be checked for out data ports
			if(direction.outgoing() && !direction.incoming())
				if(getTiming(connection).equals(Communication_Properties_Timing_Immediate))
					sb.append(names.get(connection) + "_Sync, ");
		}
		
		if(sb.length() != 0)
			sb.delete(sb.length() - 2, sb.length()); // delete pending commata
		
		// TODO: in out port
		return sb.toString().trim();
	}
	
	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(FeatureInstance object, long counter, Map<InstanceObject, String> names)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getPackageName(object));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(getClassName(object));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(counter);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(getPackageName(object));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(getClassName(object));
    stringBuffer.append(TEXT_6);
    stringBuffer.append(getParameters(object, names));
    stringBuffer.append(TEXT_7);
    stringBuffer.append(TEXT_8);
    return stringBuffer.toString();
  }
}