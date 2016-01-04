package de.uniaugsburg.smds.aadl2rtsj.utils;

import org.osate.aadl2.Classifier;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;

public class Utils {
	
	public static String getClassName(NamedElement object){
		String name = object.getName();
		StringBuilder b = new StringBuilder(name);
		b.replace(0, 1, b.substring(0,1).toUpperCase());
		return b.toString();
	}
	
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
	
	public static String getDataType(Classifier classifier){
		// cases: primitive type(integer, double, etc.)
		// custom type like data? can it be anything else?
		//TODO: other datatypes
		return "Object";
	}
}