package de.uniaugsburg.smds.aadl2rtsj.utils;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.AADL_Project_Time_Units_Milli_Seconds;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.AADL_Project_Time_Units_Nano_Seconds;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.Communication_Properties;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.Communication_Properties_Transmission_Type;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.Communication_Properties_Transmission_Type_Push;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.Thread_Properties;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.Thread_Properties_Dispatch_Protocol;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.Thread_Properties_Priority;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.Timing_Properties;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.Timing_Properties_Period;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.Connection;
import org.osate.aadl2.DataImplementation;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.IntegerLiteral;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;

public class Utils {
	private static final Logger log = Logger.getLogger( Utils.class.getName() );
	
	/**
	 * Creates a class name for a given NamedElement by turning the first letter of its name to upper case.
	 * @param object the object the class name is created for
	 * @return the result of object.getName() with its first letter in upper case
	 */
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
	
	public static String getDataPortTransmissionType(FeatureInstance feature){
		List<PropertyExpression> transmissionTypeProperties = null;
		transmissionTypeProperties = feature.getPropertyValues(Communication_Properties, Communication_Properties_Transmission_Type);
		
		String transmissionType = null;
		
        if(transmissionTypeProperties.size() > 0){
        	EnumerationLiteral transmissionTypeProperty = (EnumerationLiteral)((NamedValue)transmissionTypeProperties.get(0)).getNamedValue();// we don't consider modes at the moment
 		   	transmissionType = transmissionTypeProperty.getName();
        }
        else{
        	//default value is push
        	transmissionType = Communication_Properties_Transmission_Type_Push;
        }
        return transmissionType;
	}
	
	public static String getDispatchProtocol(ComponentInstance object){
		String dispatchProtocol = null;
		// get Thread Type: Periodic, Aperiodic, Sporadic, Hybrid, Timed or Background
		List<PropertyExpression> dispatchProtocolPropertyList = object.getPropertyValues(Thread_Properties, Thread_Properties_Dispatch_Protocol);
		// only do something if the type of thread was defined
		if(dispatchProtocolPropertyList.size() > 0){
			PropertyExpression dispatchProtocolProperty = dispatchProtocolPropertyList.get(0); // we don't consider modes at the moment
			if(dispatchProtocolProperty instanceof NamedValue){
				EnumerationLiteral namedValue = (EnumerationLiteral)((NamedValue)dispatchProtocolProperty).getNamedValue();
				dispatchProtocol = namedValue.getName();// should be "Periodic" or "Aperiodic" or one of the others
			}
		}
		return dispatchProtocol;
	}
	
	public static Connection getConnection(ConnectionInstance connection){
		return connection.getConnectionReferences().get(0).getConnection();
	}
	
	public static boolean createJavaClass(String packageName, String className, String sourceCode, IProgressMonitor monitor, IPackageFragmentRoot root) {
		IPackageFragment fragment = null;
		try {
			// get or create the package
			fragment = root.createPackageFragment(packageName, false, monitor);
			
			// create class
			ICompilationUnit cu = fragment.createCompilationUnit(className+".java", sourceCode, false, null);
			cu.save(monitor, true);
		} catch (JavaModelException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}