package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.Connection;
import org.osate.aadl2.DataImplementation;
import org.osate.aadl2.DataPort;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.Element;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.Feature;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.RangeValue;
import org.osate.aadl2.RecordValue;
import org.osate.aadl2.impl.ComponentImplementationImpl;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.ConnectionInstanceEnd;
import org.osate.aadl2.instance.ConnectionReference;
import org.osate.aadl2.instance.FeatureCategory;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;

import util.OffsetTime;
import util.UtilFactory;

public class ComponentInstanceHelper {
	
	private static final Logger log = Logger.getLogger(ComponentInstanceHelper.class.getName());
	
	/**
	 * In order to create a valid Java package name for the given InstanceObject we take the</br>
	 * element.getInstanceObjectPath() which is always in form of 'System_Type_Name' + 'System_Implementation_Name' + '_Instance' + '.path.within.systemtree'</br>
	 * The '_Instance' part gets removed and the rest of this path is returned as is.</br>
	 * The packagename is prepended with 'instance.'
	 * @param element the instance one wants the package name for
	 * @return a unique, valid Java package name for the given InstanceObject
	 */
	public static String getPackageName(InstanceObject element){
		StringBuffer pkg = new StringBuffer();
		//TODO: this might change if we generate Data Elements, that have inner connections and are not only used as classifiers
		// special case: Data- InstanceObject: we need just the classifier package 
		if(element instanceof ComponentInstance && ((ComponentInstance)element).getCategory().equals(ComponentCategory.DATA)){
			ComponentClassifier cc = ((ComponentInstance)element).getComponentClassifier();
			return ComponentClassifierHelper.getPackageName(cc);
		}
		else{
			pkg.append("instance.");
			pkg.append(getHierarchyName(element));
		}
		return pkg.toString().toLowerCase();
	}
	
	/**
	 * In order to create a valid Java package name for the given InstanceObject's user code we take the</br>
	 * element.getInstanceObjectPath() which is always in form of 'System_Type_Name' + 'System_Implementation_Name' + '_Instance' + '.path.within.systemtree'</br>
	 * The '_Instance' part gets removed and the rest of this path is returned as is.</br>
	 * The packagename is prepended with 'user.'
	 * @param element the instance one wants the package name for
	 * @return a unique, valid Java package name for the given InstanceObject
	 */
	public static String getUserCodePackageName(InstanceObject element){
		StringBuffer pkg = new StringBuffer();
		pkg.append("user.");
		pkg.append(getHierarchyName(element));
		return pkg.toString().toLowerCase();
	}
	
	private static String getHierarchyName(InstanceObject object){
		StringBuffer buffer = new StringBuffer(object.getInstanceObjectPath()); // returns a String in form of <System_Type_Name><System_Implementation_Name>_Instance.<path>.<within>.<systemtree>
		int pkgEnd = buffer.indexOf(".");// find the first occurence of '.'
		// if the buffer is already the <System_Type_Name><System_Implementation_Name> part
		if(pkgEnd == -1)
			pkgEnd = buffer.length();
		StringBuffer pkg = new StringBuffer(buffer.substring(0, pkgEnd));//get the part <System_Type_Name><System_Implementation_Name>
		buffer.delete(0, pkgEnd); // remove the part <System_Type_Name><System_Implementation_Name>
		pkg = pkg.delete(pkg.indexOf("_Instance"), pkg.length());//delete the "_Instance" part
		buffer.insert(0, pkg);//insert <System_Type_Name><System_Implementation_Name> in front of the rest again
		return buffer.toString();
	}
	
	/**
	 * @param ne the NamedElement one wants a class name for
	 * @return ne.getName(). If ne is a Data Component, ComponentClassifierHelper.getClassName(ne.getClassifier()) is called.
	 */
	public static String getClassName(NamedElement ne){
		// Data is a special case, as its "class" is always determined by the classifier
		if(ne instanceof ComponentInstance && ((ComponentInstance)ne).getCategory().equals(ComponentCategory.DATA)){
			ComponentClassifier cc = ((ComponentInstance)ne).getComponentClassifier();
				return ComponentClassifierHelper.getClassName(cc);
		}
		return ne.getName();
	}
	
	/**
	 * @param ne NamedElement one wants a user class name for
	 * @return ne.getName() concatenated with 'UserClass'
	 */
	public static String getUserCodeClassName(NamedElement ne){
		return ne.getName() + "UserCode";
	}
	
	/**
	 * @param time OffsetTime one wants a handler class name for
	 * @return 'Handler_' concatenated with the time's unique ID
	 */	
	public static String getHandlerClassName(OffsetTime time){
		return "Handler_" + time.getUniqueId();
	}
	
	/**
	 * @param ci the ComponentInstance one wants the children for
	 * @return a list with all children of the given ci, like subcomponents, connections, etc.
	 */
	public static List<Element> getChildren(ComponentInstance ci){
		return ci.getChildren();
	}
	
	/**
	 * @param ci the ComponentInstance one wants the parent for
	 * @return the parent ComponentInstnace if there is any, <code>null</code> otherwise.
	 */
	public static ComponentInstance getParent(ComponentInstance ci){
		return ci.getContainingComponentInstance();
	}
	
	/**
	 * @param fi the FeatureInstance one wants the Classifier for
	 * @return the ComponentClassifier for the given FeatureInstance, <code>null</code> if none was specified</br>
	 * Usually this is a user-defined DataType/DataImplementation or one of the Basic Types, provided by OSATE
	 */
	public static ComponentClassifier getClassifier(FeatureInstance fi){
		return (ComponentClassifier) fi.getFeature().getClassifier();
	}
	
	/**
	 * @param ci the ComponentInstance one wants the Classifier for
	 * @return the ComponentClassifier for the given ComponentInstance,</br>
	 * e.g. ThreadType/ThreadImplementation for a ComponentInstance whose ComponentCategory equals 'thread'
	 */
	public static ComponentClassifier getClassifier(ComponentInstance ci){
		return ci.getComponentClassifier();
	}
	
	/**
	 * Usually one uses this method to determine if a thread is a blocking one, which has to wait for another thread to finish its work. </br>
	 * This is the case if the thread has at least one ingoing feature, whose incoming connection is immediate.
	 * @param component the ComponentInstance (usually with ComponentCategory == 'thread') one wants an ingoing, immediate ConnectionInstance for, if any is present
	 * @return the immediate ConenctionInstance, if any, otherwise <code>null</code>
	 */
	//für alle eingeheneden features die connections raussuchen, falls eine davon immediate ist warten wir
	public static ConnectionInstance getImmediateConnection(ComponentInstance component){
		List<FeatureInstance> features = component.getAllFeatureInstances();
		for (FeatureInstance feature : features) {
			DirectionType direction = feature.getDirection();
			if(direction.incoming()){
				for (ConnectionInstance connection : feature.getDstConnectionInstances()) {
					String timing = PropertyHelper.getTiming(connection);
					if(timing.equals(Communication_Properties_Timing_Immediate))
						return connection;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param coni the ConnectionInstance one wants the destination of
	 * @return the destination of the given ConnectionsInstance, usually this is a FeatureInstance
	 */
	public static ConnectionInstanceEnd getConnectionDestination(ConnectionInstance coni){
		return coni.getDestination();
	}
}
