package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class MainConverter{
	
  protected static String nl;
  public static synchronized MainConverter create(String lineSeparator)
  {
    nl = lineSeparator;
    MainConverter result = new MainConverter();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package main;" + NL + "" + NL + "public class Main{" + NL + "\t" + NL + "\tpublic static void main(String[] args){" + NL + "\t\t";
  protected final String TEXT_2 = NL + "\t}" + NL + "}";

	private static String getAssignmentStatements(List<InstanceObject> objects){
		StringBuilder sb = new StringBuilder();
		List<ConnectionInstance> connections = new ArrayList<ConnectionInstance>();
		List<InstanceObject> componentsAndFeatures = new ArrayList<InstanceObject>();
		List<ComponentInstance> threads = new ArrayList<ComponentInstance>();
		
		// we need to give each object a unique name
		// as full qualified names might be unreadable, we just name them connection_X, feature_X, component_X
		long connectionCounter = 0;
		long featureCounter = 0;
		long componentCounter = 0;
		
		// now we have to remeber which object received which number
		Map<InstanceObject, String> names = new HashMap<InstanceObject, String>(); 
		
		// objects are collected top-down, but we have to create them bottom-up, so that each constructor's parameters are created before they are assigned
		Collections.reverse(objects);
		
		// connections are treated differently, so sort them into distinct lists
		for (InstanceObject object : objects) {
			if(object instanceof ConnectionInstance)
				connections.add((ConnectionInstance)object);
			if(object instanceof FeatureInstance)
				componentsAndFeatures.add(object);
			if(object instanceof ComponentInstance)
				componentsAndFeatures.add(object);
		}
		
		// do connections first, as they have parameterless constructors
		MainConnectionStatement connnectionStatement = new MainConnectionStatement();
		MainSyncObjectStatement syncObjectStatement = new MainSyncObjectStatement();
		
		for (ConnectionInstance connection : connections) {
			sb.append(connnectionStatement.generate(connection, connectionCounter));
			names.put(connection, "connection_" + connectionCounter);
			//if this connection is immediate we have to create a synchronisationobject
			if(getTiming(connection).equals(Communication_Properties_Timing_Immediate))
				sb.append(syncObjectStatement.generate(connection, connectionCounter));
			connectionCounter++;
		}
		
		MainFeatureStatement featureStatement = new MainFeatureStatement();
		MainComponentStatement componentStatement = new MainComponentStatement();
		
		//now we can create the other statements bottom-up
		for (InstanceObject object : componentsAndFeatures) {
			// we have to decide between features and components, because features can only have connection parameters, 
			// whereas components can have feature and/or component parameters
			if(object instanceof FeatureInstance){
				sb.append(featureStatement.generate((FeatureInstance)object, featureCounter, names));
				names.put(object, "feature_" + featureCounter);
				featureCounter++;
			}
			else{
				// must be a ComponentInstance
				ComponentInstance component = (ComponentInstance) object;
				sb.append(componentStatement.generate(component, componentCounter, names));
				names.put(object, "component_" + componentCounter);
				componentCounter++;
				// save threads to start them later
				if(component.getCategory().equals(ComponentCategory.THREAD))
					threads.add(component);
			}
		}
		
		//TODO: einschränken auf periodische Threads, ist zur Zeit noch egal, da wir keine anderen betrachten
		MainThreadStartStatement startStatement = new MainThreadStartStatement();
		for (ComponentInstance thread : threads) {
			sb.append(startStatement.generate(thread, names));
		}
		return sb.toString().trim();
	}
	
	/*
	 * (non-javadoc)
	 * 
	 * @see IGenerator#generate(Object)
	 */
	public String generate(List<InstanceObject> objects)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(getAssignmentStatements(objects));
    stringBuffer.append(TEXT_2);
    return stringBuffer.toString();
  }
}