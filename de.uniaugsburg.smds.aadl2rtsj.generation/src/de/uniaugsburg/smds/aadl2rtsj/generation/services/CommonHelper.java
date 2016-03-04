package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import org.osate.aadl2.Classifier;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.Connection;
import org.osate.aadl2.DataImplementation;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.ConnectionReference;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;

public class CommonHelper {
	
	public static String getClassName(NamedElement object){
		if(object instanceof ConnectionInstance)
			object = getConnection((ConnectionInstance) object);
		// Data is a special case, as its "class" is always determined by the classifier
		if(object instanceof ComponentInstance){
			ComponentInstance component = (ComponentInstance)object;
			if(component.getCategory().equals(ComponentCategory.DATA)){
				object = component.getComponentClassifier(); // we assume, that it is a DataImplementation, not a DataType
				// if it's a DataType the aadl model was not specific enough and this will result in an error
				//TODO: log warning?
			}
		}
		StringBuilder b = new StringBuilder(object.getName());
		if(object instanceof ComponentImplementation){
			//replace the ".impl" part with "Impl"
			b.replace(b.lastIndexOf(".impl"), b.length(), "Impl");
		}
		
		//generally we replace "_xxx" with "Xxx"
		int index;
		while((index = b.indexOf("_")) != -1){
			b.replace(index, index+2, b.substring(index+1, index+2).toUpperCase());
		}
		b.replace(0, 1, b.substring(0,1).toUpperCase());
		return b.toString();
	}
	
	public static String getObjectName(NamedElement object){
		if(object instanceof ConnectionInstance)//
			object = getConnection((ConnectionInstance) object);
		return object.getName();
	}
	
	public static String getPackageName(NamedElement element){
		StringBuffer pkg = new StringBuffer();
		
		if(element instanceof InstanceObject){
			// special case: Data- InstanceObject: we need just the data package 
			if(element instanceof ComponentInstance && ((ComponentInstance)element).getCategory().equals(ComponentCategory.DATA)){
				Classifier classifier = ((ComponentInstance)element).getComponentClassifier();
				if(classifier instanceof DataImplementation)
					pkg.append("data");
				//else: do nothing, because the aadl model was not specific enough and we just use Object, where no package is needed
				//TODO: log warning?
			}
				
			else{
				//in case of an instanceobject we need its path within the model
				pkg.append("instance.");
				pkg.append(getHierarchyName((InstanceObject) element));
			}
		}else{
			// in case of a type we merely need the data package
			pkg.append("data");
		}
		return pkg.toString().toLowerCase();
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
	private static String getHierarchyName(InstanceObject object){
		StringBuffer buffer = null;
		buffer = new StringBuffer(object.getInstanceObjectPath());
		// if its a feature instance we have to omit the last part of the path as we want the feature in the same package as its parent component
		if(object instanceof FeatureInstance){
			buffer.delete(buffer.lastIndexOf("."), buffer.length());
		}
		//if it is a ConnectionInstance we have to omit the name part of the path, as it is not the name, but xxx.port -> yyy.port
		if(object instanceof ConnectionInstance){
			buffer.delete(buffer.indexOf(object.getName())-1, buffer.length()); // -1 because otherwise we would have an extra dot at the end
		}
		
		int pkgEnd = buffer.indexOf(".");
		// if the buffer is already the package
		if(pkgEnd == -1)
			pkgEnd = buffer.length();
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
		return buffer.toString();
	}
	
	
	public static String getDataType(FeatureInstance feature){
		Classifier classifier = feature.getFeature().getClassifier();
		//if no classifier is given, then return default type object
		if(classifier == null)
			return "Object";
		
		String type = null;
		type = getBaseType(classifier);
		
		//if it is not a base type, then return the class name of this datatype
		if(type == null)
			type = getClassName(classifier);
		
		return type;
	}
	
	private static String getBaseType(Classifier classifier){
		String type = null;
		// determine if it is one of the predefined AADL Base_Types
		if(isBaseType(classifier)){
			Classifier currentSuperType = classifier;
			Classifier superType = classifier;
			while(currentSuperType != null){
				// go up the inheritance tree if any
				superType = currentSuperType;
				currentSuperType = currentSuperType.getExtended();
			}
			type = superType.getName();
		}
		return type;
	}
	
	public static boolean isBaseType(Classifier classifier){
		return getNameSpace(classifier).equals("Base_Types");
	}
	
	private static String getNameSpace(Classifier classifier){
		StringBuffer namespace = new StringBuffer(classifier.getNamespace().getFullName());
		int visibilityPos;
		if((visibilityPos = namespace.indexOf("_public")) != -1)
			namespace.delete(visibilityPos, visibilityPos + "_public".length());
		else{
			if((visibilityPos = namespace.indexOf("_private")) != -1)
				namespace.delete(visibilityPos, visibilityPos + "_private".length());
		}
		return namespace.toString();
	}
	
	// if there are multiple connectionreferences, then the one upmost in the system hierarchy is returned
	public static Connection getConnection(ConnectionInstance connection){
		ConnectionReference upmost = connection.getConnectionReferences().get(0);
		for (ConnectionReference current: connection.getConnectionReferences()) {
			if(!current.equals(upmost)){
				ComponentInstance currentContext = current.getContext();
				ComponentInstance upmostContext = upmost.getContext();
				if(currentContext.getAllComponentInstances().contains(upmostContext))
					upmost = current;
				else
					break;
			}
		}
		return upmost.getConnection();
	}

}
