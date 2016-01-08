package de.uniaugsburg.smds.aadl2rtsj.utils;

import java.util.List;
import java.util.logging.Logger;

import org.osate.aadl2.Classifier;
import org.osate.aadl2.IntegerLiteral;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;

public class Utils {
	private static final Logger log = Logger.getLogger( Utils.class.getName() );
	
	/**
	 * Creates a class name for a given NamedElement by turning the first letter of its name to upper case.
	 * @param object the object the class name is created for
	 * @return the result of object.getName() with its first letter in upper case
	 */
	public static String getClassName(NamedElement object){
		String name = object.getName();
		StringBuilder b = new StringBuilder(name);
		b.replace(0, 1, b.substring(0,1).toUpperCase());
		return b.toString();
	}
	
	/**
	 * Uses the instanceobject's path to create a package name from it.<br>
	 * If the instance object is of type FeatureInstance, the part after the last dot is stripped.<br>
	 * If the path contains an "_impl_" part, then this part is stripped.<br>
	 * If the path contains an "_Instance" part, then this part is stripped.<br>
	 * The whole String is returned as lower case.
	 * @param object the object the packagename is created for
	 * @return a String that represents a package name, e.g. path.to.some.package
	 */
	public static String getPackageName(InstanceObject object){
		StringBuffer buffer = new StringBuffer(object.getInstanceObjectPath());
		// if its a feature instance we have to omit the last part of the path as we want the feature in the same package as its parent component
		if(object instanceof FeatureInstance){
			buffer.delete(buffer.lastIndexOf("."), buffer.length());
		}
		
		int pkgEnd = buffer.indexOf(".");
		StringBuffer pkg = new StringBuffer(buffer.substring(0, pkgEnd));//get the part that represents the package
		buffer.delete(0, pkgEnd); // remove the part that represents the package
		
		int implPos;
		if((implPos = pkg.indexOf("_impl_")) != -1){//if the package part contains an _impl_section start deletion here
			pkg = pkg.delete(implPos, pkg.length());
		}
		else{//else start deletion at the _Instance position
			pkg = pkg.delete(pkg.indexOf("_Instance"), pkg.length());
		}
		buffer.insert(0, pkg);//insert package in front of the rest again
		return buffer.toString().toLowerCase();
	}
	
	public static String getDataType(FeatureInstance feature){
		// cases: primitive type(integer, double, etc.)
		// custom type like data? can it be anything else?
		//TODO: other datatypes
		Classifier classifier = feature.getFeature().getClassifier();
		return "Object";
	}
	
	public static String getPeriodMilliSeconds(ComponentInstance component){
		// get period
		List<PropertyExpression> periodPropertyList = component.getPropertyValues(Timing_Properties, Timing_Properties_Period);
		// only do something if period was defined
		if(periodPropertyList.size() > 0){
			PropertyExpression periodProperty = periodPropertyList.get(0); // we don't consider modes at the moment
			Integer period = new Integer((int)((IntegerLiteral)periodProperty).getScaledValue(AADL_Project_Time_Units_Milli_Seconds));
			return period.toString();
		}
		log.warning("No Timing_Properties::Period was given for " + component.getName());
		// TODO: default value?
		return "";
	}
	
	public static String getPeriodNanoSeconds(ComponentInstance component){
		// get period
		List<PropertyExpression> periodPropertyList = component.getPropertyValues(Timing_Properties, Timing_Properties_Period);
		// only do something if period was defined
		if(periodPropertyList.size() > 0){
			PropertyExpression periodProperty = periodPropertyList.get(0); // we don't consider modes at the moment
			// at nanoseconds we only want the part that is below ms, so we modulo by 1 million
			Integer period = new Integer((int)(((IntegerLiteral)periodProperty).getScaledValue(AADL_Project_Time_Units_Nano_Seconds) % 1000000));
			return period.toString();
		}
		log.warning("No Timing_Properties::Period was given for " + component.getName());
		return "";
	}
	
	public static String getPriority(ComponentInstance component){
		// get priority
		List<PropertyExpression> priorityPropertyList = component.getPropertyValues(Thread_Properties, Thread_Properties_Priority);
		// only do something if period was defined
		if(priorityPropertyList.size() > 0){
			PropertyExpression priorityProperty = priorityPropertyList.get(0); // we don't consider modes at the moment
			Integer priority = new Integer((int)(((IntegerLiteral)priorityProperty).getValue()));
			return priority.toString();
		}
		log.warning("No Thread_Properties::Priority was given for " + component.getName());
		// TODO: default value?
		return "";
	}
}