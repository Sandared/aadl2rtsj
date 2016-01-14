package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.List;
import java.util.Map;

public class MainComponentStatement{
	
  protected static String nl;
  public static synchronized MainComponentStatement create(String lineSeparator)
  {
    nl = lineSeparator;
    MainComponentStatement result = new MainComponentStatement();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t\t";
  protected final String TEXT_2 = " component_";
  protected final String TEXT_3 = " = new ";
  protected final String TEXT_4 = "(";
  protected final String TEXT_5 = ");";
  protected final String TEXT_6 = NL;

	private static String getParameters(ComponentInstance object, Map<InstanceObject, String> names){
		StringBuilder sb = new StringBuilder();
		
		List<FeatureInstance> features = object.getFeatureInstances();
		List<ComponentInstance> subcomponents = object.getAllComponentInstances();
		
		// remove first one, because this is always the component itself
		subcomponents.remove(0);
		
		for (FeatureInstance feature : features) {
			sb.append(names.get(feature) + ", ");
		}
		
		for (ComponentInstance subcomponent : subcomponents) {
			sb.append(names.get(subcomponent) + ", ");
		}
		
		if(sb.length() != 0)
			sb.delete(sb.length() - 2, sb.length()); // delete pending commata
		
		return sb.toString().trim();
	}
	
	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(ComponentInstance object, long counter, Map<InstanceObject, String> names)
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