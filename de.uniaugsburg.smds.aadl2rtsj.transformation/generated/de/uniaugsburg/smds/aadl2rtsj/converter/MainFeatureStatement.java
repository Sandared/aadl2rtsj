package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.DirectionType;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

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
  protected final String TEXT_2 = " feature_";
  protected final String TEXT_3 = " = new ";
  protected final String TEXT_4 = "(";
  protected final String TEXT_5 = ");";
  protected final String TEXT_6 = NL;

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
    stringBuffer.append(getClassName(object));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(counter);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(getClassName(object));
    stringBuffer.append(TEXT_4);
    stringBuffer.append(getParameters(object, names));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(TEXT_6);
    return stringBuffer.toString();
  }
}