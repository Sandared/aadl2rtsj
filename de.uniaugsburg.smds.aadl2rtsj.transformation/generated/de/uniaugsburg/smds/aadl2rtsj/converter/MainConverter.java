package de.uniaugsburg.smds.aadl2rtsj.converter;

import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

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
  protected final String TEXT_1 = "package main;" + NL;
  protected final String TEXT_2 = NL;
  protected final String TEXT_3 = NL + NL + "public class Main{" + NL + "\t" + NL + "\tpublic static void main(String[] args){" + NL + "\t\t";
  protected final String TEXT_4 = NL + "\t}" + NL + "}";

	private static String getImportStatements(List<InstanceObject> objects){
		StringBuilder sb = new StringBuilder();
		if(objects.size() != 0){
			ImportStatement statement = new ImportStatement();
			for (InstanceObject object : objects) {
				sb.append(statement.generate(object));
			}
		}
		return sb.toString().trim();
	}
	
	private static String getAssignmentStatements(List<InstanceObject> objects){
		StringBuilder sb = new StringBuilder();
		List<ConnectionInstance> connections = new ArrayList<ConnectionInstance>();
		List<InstanceObject> componentsAndFeatures = new ArrayList<InstanceObject>();
		
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
		
		// do passive connections first, as they have parameterless constructors
		MainPassiveConnectionStatement connnectionStatement = new MainPassiveConnectionStatement();
		
		// we need to remember which connections already have been transformed into a statement, especially for active connections
		Set<ConnectionInstance> workedConnections = new HashSet<ConnectionInstance>();
		for (ConnectionInstance connection : connections) {
			// only do something if this connection wasn't already transformed into a statement
			if(!workedConnections.contains(connection)){
				boolean isActive = isActive(connection);
				if(!isActive){
					sb.append(connnectionStatement.generate(connection, connectionCounter));
					names.put(connection, "connection_" + connectionCounter);
					workedConnections.add(connection);
					connectionCounter++;
				}
				else{
					sb.append(createActiveConnection(connection, workedConnections, connectionCounter, names));
				}
			}
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
				sb.append(componentStatement.generate((ComponentInstance)object, componentCounter, names));
				names.put(object, "component_" + componentCounter);
				componentCounter++;
			}
		}
		return sb.toString().trim();
	}
	
	private static String createActiveConnection(ConnectionInstance connection, Set<ConnectionInstance> workedConnections, long connectionCounter, Map<InstanceObject, String> names){
		StringBuilder sb = new StringBuilder();
		List<ConnectionInstance> successors = new ArrayList<ConnectionInstance>();
		List<ConnectionInstance> targets = new ArrayList<ConnectionInstance>();
		successors.add(connection);
		// search for the target of this connection as it is active. Adds each connection in line, that is not active (has an active thread as target)
		while(successors.size() > 0){
			ConnectionInstance currentConnection = successors.remove(0);// get the leading element
			boolean isActive = isActive(currentConnection);
			if(!isActive)
				// we have found a target connection
				targets.add(currentConnection);
			else{
				// add the successor connections for the target of the current connection
				successors.addAll(currentConnection.getDestination().getSrcConnectionInstances());
			}
		}
		
		// first generate all found passive target connections
		MainPassiveConnectionStatement passiveConnnectionStatement = new MainPassiveConnectionStatement();
		for (ConnectionInstance target : targets) {
			// but only if it has not already been generated
			if(!workedConnections.contains(target)){
				sb.append(passiveConnnectionStatement.generate(target, connectionCounter));
				names.put(target, "connection_" + connectionCounter);
				workedConnections.add(target);
				connectionCounter++;
			}
		}
		
		// now take the targets as starting point to reverse-generate the statements for the active connections
		MainActiveConnectionStatement activeConnectionStatement = new MainActiveConnectionStatement();
		for (ConnectionInstance target : targets) {
			// dataports only have one ingoing connection per mode and we don't consider modes at the moment
			ConnectionInstance predecessor = target.getSource().getDstConnectionInstances().get(0);
			while(predecessor != null){
				// only generate if it has not already been generated
				if(!workedConnections.contains(predecessor)){
					sb.append(activeConnectionStatement.generate(connection, connectionCounter, names));
					names.put(target, "connection_" + connectionCounter);
					workedConnections.add(target);
					connectionCounter++;
				}
				
				List<ConnectionInstance> predecessors = predecessor.getSource().getDstConnectionInstances();
				if(predecessors != null && predecessors.size() > 0)
					predecessor = predecessors.get(0);
				else
					predecessor = null;
			}
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
    stringBuffer.append(TEXT_2);
    stringBuffer.append(getImportStatements(objects));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(getAssignmentStatements(objects));
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}