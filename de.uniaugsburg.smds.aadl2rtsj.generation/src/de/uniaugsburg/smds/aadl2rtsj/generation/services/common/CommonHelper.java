package de.uniaugsburg.smds.aadl2rtsj.generation.services.common;

import static de.uniaugsburg.smds.aadl2rtsj.generation.utils.Constants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.Connection;
import org.osate.aadl2.DataImplementation;
import org.osate.aadl2.DataSubcomponent;
import org.osate.aadl2.DirectedFeature;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.Feature;
import org.osate.aadl2.IntegerLiteral;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.Namespace;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.RangeValue;
import org.osate.aadl2.RecordValue;
import org.osate.aadl2.Subcomponent;
import org.osate.aadl2.ThreadImplementation;
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



public class CommonHelper {
	
	private static final Logger log = Logger.getLogger(CommonHelper.class.getName());
	
	public static String getClassName(InstanceObject io){
		NamedElement object = io; // need the broader reference for stuff like connections
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
			//replace the ".xxx" part with "Xxx"
			int dotIndex = b.lastIndexOf(".");
			if(dotIndex != -1){
				b.deleteCharAt(dotIndex);
				b.replace(dotIndex, dotIndex + 1, b.substring(dotIndex, dotIndex + 1).toUpperCase());
			}
		}
		
		//generally we replace "_xxx" with "Xxx"
		int index;
		while((index = b.indexOf("_")) != -1){
			b.replace(index, index+2, b.substring(index+1, index+2).toUpperCase());
		}
		b.replace(0, 1, b.substring(0,1).toUpperCase());
		return b.toString();
	}
	
	public static String getClassName(ComponentClassifier cf){
		if(cf instanceof ComponentImplementation)
			return ((ComponentImplementation) cf).getImplementationName();
		return cf.getName();
	}
	
	public static String getObjectName(NamedElement object){
		if(object instanceof ConnectionInstance)//
			object = getConnection((ConnectionInstance) object);
		return object.getName();
	}
	
	public static String getPackageName(InstanceObject element){
		StringBuffer pkg = new StringBuffer();
		
		// special case: Data- InstanceObject: we need just the data package 
		if(element instanceof ComponentInstance && ((ComponentInstance)element).getCategory().equals(ComponentCategory.DATA)){
			ComponentClassifier cc = ((ComponentInstance)element).getComponentClassifier();
			if(cc instanceof DataImplementation)
				return getPackageName(cc);
			//else: do nothing, because the aadl model was not specific enough and we just use Object, where no package is needed
			//TODO: log warning?
		}
		else{
			//in case of an instanceobject we need its path within the model
			pkg.append("instance.");
			pkg.append(getHierarchyName((InstanceObject) element));
		}
		
		return pkg.toString().toLowerCase();
	}
	
	/**
	 * A ComponentClassifier is always put into the package 'types'. The rest o f the packagename is constructed as follows:</br>
	 * We take two values of the AADL model into consideration:</br>
	 * - <code>cc.getNamespace.getName()</code> which is in the form 'packagename'_'visibility'</br>
	 * - <code>cc.getQualifiedName()</code> which is in the form 'packagename'::'typename</br>
	 * 'visibility' public/private is replaced by internal/external in order to form a valid java package identifier
	 * The concatenated String is composed in the form of 'packagename'+'visibility'+'typename'</br>
	 * All '_' in packagename and visibility are replaced by '.'</br>
	 * All '::' in packagename and visibility are replaced by '.' </br> 
	 * @param cc 
	 * @return a valid java namespace for cc. 
	 */
	public static String getPackageName(ComponentClassifier cc){
		StringBuilder sb = new StringBuilder();
		sb.append("types");//it's a classifier so it gets into the type package
		sb.append(".");
		sb.append(getAADLPackageName(cc));		
		return sb.toString().toLowerCase();
	}
	

	private static String getAADLPackageName(ComponentClassifier cc){
		//if it is an Implementation it gets the same package as its type
		if(cc instanceof ComponentImplementation)
			cc = ((ComponentImplementation) cc).getType();
		
		String aadlPkg = cc.getNamespace().getName();
		
		int visibilityIndex = aadlPkg.lastIndexOf("_"); // namespace looks always like 'some::name::space'_'visibility'
		String visibility = aadlPkg.substring(visibilityIndex + 1);
		if(visibility.equals("public"))
			visibility = "external";
		else
			visibility = "internal";
		aadlPkg = aadlPkg.substring(0, visibilityIndex);
		
		String typeName = cc.getQualifiedName();
		
		int typeIndex = typeName.lastIndexOf("::"); // qualified name always looks like 'some::name::space'::'typeName'
		typeName = typeName.substring(typeIndex + 2);
		
		aadlPkg = aadlPkg + "." + visibility + ".";
		aadlPkg = aadlPkg.replace("::", ".");
		aadlPkg = aadlPkg.replace("_", ".");
		
		return aadlPkg.toLowerCase() + typeName.toLowerCase();
	}
	
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
	
	public static String getHandlerClassName(FeatureInstance feature, OffsetTime time){
		return getClassName(feature) + "IOHandler_" + time.getUniqueId();
	}
	
	public static String getDataType(FeatureInstance feature){
		ComponentClassifier classifier = (ComponentClassifier) feature.getFeature().getClassifier();
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
	
	public static String getSynchronisationObjectName(ConnectionInstance connection){
		ConnectionInstanceEnd source = connection.getSource();
		ConnectionInstanceEnd target = connection.getDestination();
		return getObjectName(source) + "2" + getClassName(target) + "Sync";
	}
	
	public static List<OffsetTime> getTimes(FeatureInstance feature, ConnectionInstance connection, String IOReferenceTime, Boolean isInput){
		OffsetTime time = null;
		List<OffsetTime> times = new ArrayList<OffsetTime>();
		
		// if immediate/delayed connections are present, then (partially) ignore Input/Output_Time
		if(connection != null){
			time = getTimeForConnection(feature, connection, isInput);
			// special case: if there's a connection timing then this overwrites all other possible timings
			if(time != null){
				times.add(time);
				return times;
			}
		}
		
		// if NOT immediate/delayed consider Time part of Input/Output_Time and IGNORE offset
		if(time == null)
			times = getTimeForReferenceTime(feature, IOReferenceTime, false, isInput, connection);
        
		if(times.size() == 0){
    	   // if there has been no Input/Output_Time on the feature, check for Input/Output_Time on thread, see AADL Standard 8.3.2 (18)
    	   ComponentInstance component = feature.getContainingComponentInstance();
    	   times = getTimeForReferenceTime(component, IOReferenceTime, false, isInput, connection);
        }
		
		if(times.size() == 0)
			if(isInput){
				//if time is still null, then default is Dispatch for Input, see AADL Standard 8.3.2 (17)). 
				OffsetTime ot = UtilFactory.eINSTANCE.createOffsetTime();
				ot.setMs(0);
				ot.setNs(0);
				ot.setUniqueId(System.identityHashCode(feature));
				ot.setIoTime(Communication_Properties_IO_Reference_Time_Dispatch);
				ot.setConnection(connection);
				times.add(ot);
			}
			else{
				//if time is still null, then default is Completion for Output, see AADL Standard 8.3.2 (25)
				OffsetTime ot = UtilFactory.eINSTANCE.createOffsetTime();
				ot.setMs(0);
				ot.setNs(0);
				ot.setUniqueId(System.identityHashCode(feature));
				ot.setIoTime(Communication_Properties_IO_Reference_Time_Completion);
				ot.setConnection(connection);
				times.add(ot);
			}
		return times;
	}
	
	private static OffsetTime getTimeForConnection(NamedElement element, ConnectionInstance connection, boolean isInput){
		OffsetTime time = null;
		String timing = CommonHelper.getTiming(connection); // sampled, immediate, delayed
		if(timing.equals(Communication_Properties_Timing_Immediate))
			if(isInput){
				// see AADL Standard 9.2.5 (50)
				time = UtilFactory.eINSTANCE.createOffsetTime();
				time.setMs(0);
				time.setNs(0);
				time.setUniqueId(System.identityHashCode(connection));
				time.setIoTime(Communication_Properties_IO_Reference_Time_Start);
				time.setConnection(connection);
			}
			else{
				// if there is a single valued Output_Time, then take it, otherwise the time is assumed to be Completion, 
				// see AADL Standard 9.2.5 (50)
				List<OffsetTime> times = getTimeForReferenceTime(element, null, true, false, connection);
				if(times.size() == 0){
					time = UtilFactory.eINSTANCE.createOffsetTime();
					time.setMs(0);
					time.setNs(0);
					time.setUniqueId(System.identityHashCode(connection));
					time.setIoTime(Communication_Properties_IO_Reference_Time_Completion);
					time.setConnection(connection);
				}
			}
		if(timing.equals(Communication_Properties_Timing_Delayed))
			if(isInput){
				// see AADL Standard 9.2.5 (51)
				time = UtilFactory.eINSTANCE.createOffsetTime();
				time.setMs(0);
				time.setNs(0);
				time.setUniqueId(System.identityHashCode(connection));
				time.setIoTime(Communication_Properties_IO_Reference_Time_Dispatch);
				time.setConnection(connection);
			}
			else{
				// see AADL Standard 9.2.5 (51)
				time = UtilFactory.eINSTANCE.createOffsetTime();
				time.setMs(0);
				time.setNs(0);
				time.setUniqueId(System.identityHashCode(connection));
				time.setIoTime(Communication_Properties_IO_Reference_Time_Deadline);
				time.setConnection(connection);
			}
		// sampled has no special semantic meaning for input and output timing
		return time;
	}
	
	private static List<OffsetTime> getTimeForReferenceTime(NamedElement element, final String IOReferenceTime, boolean forceSingleValued, boolean isInput, ConnectionInstance connection){
		ArrayList<OffsetTime> times = new ArrayList<OffsetTime>();
		List<PropertyExpression> timeProperties = null;
		if(isInput)
			timeProperties = element.getPropertyValues(Communication_Properties, Communication_Properties_Input_Time);
		else
			timeProperties = element.getPropertyValues(Communication_Properties, Communication_Properties_Output_Time);
		
		OffsetTime time = null;
		
        if(timeProperties.size() > 0){
        	if(forceSingleValued)// needed for the special case, when this method is called during connection-specific timing determination
        		if(timeProperties.size() != 1)
        			return new ArrayList<OffsetTime>();//empty list
     	   // Input/Output_Time might be a list, so Input can be frozen multiple times during a dispatch, see AADL Standard 8.3.2 (20)
     	   for (PropertyExpression timeProperty : timeProperties) {
     		   // Input/Output_Time consists of a Time Part, which is an EnumerationLiteral and an Offset Part, which is a RangeValue
     		   NamedValue timePart = (NamedValue)((RecordValue)timeProperty).getOwnedFieldValues().get(0).getOwnedValue();
     		   String ioTime = ((EnumerationLiteral)timePart.getNamedValue()).getName();
     		   RangeValue offsetPart = (RangeValue)((RecordValue)timeProperty).getOwnedFieldValues().get(1).getOwnedValue();
     		   long minimumMs = (long) offsetPart.getMinimumValue().getScaledValue(AADL_Project_Time_Units_Milli_Seconds);
     		   long minimumNs = (long) offsetPart.getMinimumValue().getScaledValue(AADL_Project_Time_Units_Nano_Seconds) % 1000000;
     		   time = UtilFactory.eINSTANCE.createOffsetTime();
			   time.setMs(minimumMs);
			   time.setNs(minimumNs);
			   time.setUniqueId(System.identityHashCode(timeProperty));
			   time.setIoTime(ioTime);
			   time.setConnection(connection);
     		   
     		   if(IOReferenceTime == null || time.getIoTime().equals(IOReferenceTime)){
     			  times.add(time);
     		   }
     	   }
        }
        return times;
	}
	
	public static ConnectionInstance getImmediateConnection(ComponentInstance component){
		List<FeatureInstance> features = component.getAllFeatureInstances();
		for (FeatureInstance feature : features) {
			DirectionType direction = feature.getDirection();
			List<ConnectionInstance> connections = null;
			if(direction.incoming() && !direction.outgoing())
				connections = feature.getDstConnectionInstances();
			if(direction.outgoing() && !direction.incoming())
				connections = feature.getSrcConnectionInstances();
			for (ConnectionInstance connection : connections) {
				String timing = CommonHelper.getTiming(connection);
				if(timing.equals(Communication_Properties_Timing_Immediate))
					return connection;
			}
		}
		return null;
	}
	
	public static EList<OffsetTime> getTimes(FeatureInstance feature){
		EList<OffsetTime> times = new BasicEList<OffsetTime>();
		EList<ConnectionInstance> connections = null;
		boolean isInput = false;
		DirectionType direction = feature.getDirection();
		if(direction.incoming() && !direction.outgoing()){
			connections = feature.getDstConnectionInstances();
			isInput = true;
		}
		if(direction.outgoing() && !direction.incoming()){
			connections = feature.getSrcConnectionInstances();
			isInput = false;
		}
		if (connections == null) {
			System.out.println("no connections");
		}
		for (ConnectionInstance connection : connections) {
			times.addAll(getTimes(feature, connection, null, isInput)); // no referencetime is needed if we want all times for one feature
		}
		return times;
	}
	
	// ########## AADL Model Navigation #########
	
	public static boolean isDataPort(FeatureInstance fi){
		return fi.getCategory() == FeatureCategory.DATA_PORT;
	}
	
	public static boolean isIncoming(FeatureInstance fi){
		return fi.getDirection().incoming();
	}
	
	public static boolean isIncoming(Feature f){
		if(f instanceof DirectedFeature)
			return ((DirectedFeature) f).getDirection().incoming();
		return true;// Default
	}
	
	public static boolean isOutgoing(FeatureInstance fi){
		return fi.getDirection().outgoing();
	}
	
	public static boolean isThread(ComponentInstance ci){
		return ci.getCategory() == ComponentCategory.THREAD;
	}
	
	public static boolean isData(Classifier c){
		return c instanceof DataImplementation; // we only consider dataImplementation at the moment as classifiers
	}
	
	public static boolean isPeriodic(ComponentInstance ci){
		return getDispatchProtocol(ci).equals(Thread_Properties_Dispatch_Protocol_Periodic);
	}
	
	public static boolean isPeriodic(ComponentClassifier ti){
		return getDispatchProtocol(ti).equals(Thread_Properties_Dispatch_Protocol_Periodic);
	}
	
	public static boolean isImmediate(ConnectionInstance connection){
		return getTiming(connection).equals(Communication_Properties_Timing_Immediate);
	}
	
	public static boolean isAtDeadline(OffsetTime time){
		return time.getIoTime().equals(Communication_Properties_IO_Reference_Time_Deadline);
	}
	
	public static ComponentInstance getComponentInstance(ConnectionInstanceEnd conie){
		return conie.getComponentInstance();
	}
	
	public static EList<ComponentInstance> getSubcomponents(ComponentInstance ci){
		EList<ComponentInstance> subcomponents = ci.getAllComponentInstances();
		subcomponents.remove(0); // the first one is always the Component itself, thus no check is needed
		return subcomponents;
	}
	
	public static EList<FeatureInstance> getFeatures(ComponentInstance ci){
		return ci.getFeatureInstances();
	}
	
	public static EList<Feature> getFeatures(ComponentClassifier cc){
		return cc.getAllFeatures();
	}
	
	public static ConnectionInstanceEnd getConnectionDestination(ConnectionInstance coni){
		return coni.getDestination();
	}
	
	public static EList<ConnectionInstance> getDstConnectionInstances(FeatureInstance fi){
		return fi.getDstConnectionInstances();
	}
	
	public static EList<ConnectionInstance> getSrcConnectionInstances(FeatureInstance fi){
		return fi.getSrcConnectionInstances();
	}
	
	public static ComponentClassifier getClassifier(FeatureInstance fi){
		return (ComponentClassifier) fi.getFeature().getClassifier();
	}
	
	public static ComponentClassifier getClassifier(ComponentInstance ci){
		return ci.getComponentClassifier();
	}
	
	public static Classifier getRealizedTypeClassifier(ComponentInstance ci){
		//we assume ci to be an implementation declaration, otherwise it's a type declaration that doesn't realize anything
		ComponentImplementationImpl ciClassifier = (ComponentImplementationImpl) getClassifier(ci);
		return ciClassifier.basicGetType();
	}
	
	public static EList<ComponentClassifier> getDataSubcomponents(DataImplementation dataImpl){
		EList<ComponentClassifier> classifiers = new BasicEList<ComponentClassifier>();
		for (Subcomponent subcomponent : dataImpl.getOwnedDataSubcomponents()) {
			classifiers.add(subcomponent.getClassifier());
		}
		return classifiers;
	}
	
	public static EList<ConnectionInstance> getConnectionInstances(ComponentInstance ci){
		return ci.getAllEnclosingConnectionInstances();
	}
	
	//######### Methods for Properties #############
	
	public static String getTiming(ConnectionInstance connection){
		String timing = null;
		List<PropertyExpression> timingProperties = connection.getPropertyValues(Communication_Properties, Communication_Properties_Timing);
		if(timingProperties.size() > 0){
			EnumerationLiteral timingProperty = (EnumerationLiteral)((NamedValue)timingProperties.get(0)).getNamedValue();
			timing = timingProperty.getName(); // sampled, immediate, delayed
		}
		else{
			//default
			timing = Communication_Properties_Timing_Sampled;
		}
		return timing;
	}
	
	public static String getDispatchProtocol(ComponentInstance ci){
		String dispatchProtocol = "";
		// get Thread Type: Periodic, Aperiodic, Sporadic, Hybrid, Timed or Background
		List<PropertyExpression> dispatchProtocolPropertyList = ci.getPropertyValues(Thread_Properties, Thread_Properties_Dispatch_Protocol);
		// only do something if the type of thread was defined
		if(dispatchProtocolPropertyList.size() > 0){
			PropertyExpression dispatchProtocolProperty = dispatchProtocolPropertyList.get(0); //TODO we don't consider modes at the moment
			if(dispatchProtocolProperty instanceof NamedValue){
				EnumerationLiteral namedValue = (EnumerationLiteral)((NamedValue)dispatchProtocolProperty).getNamedValue();
				dispatchProtocol = namedValue.getName();// should be "Periodic" or "Aperiodic" or one of the others
			}
		}
		if(dispatchProtocol.equals(""))
			log.warning("No Thread_Properties::Dispatch_Protocol was given for " + ci.getName() + ". Default is periodic");
		return Thread_Properties_Dispatch_Protocol_Periodic;
	}
	
	public static String getDispatchProtocol(ComponentClassifier cf){
		String dispatchProtocol = "";
		// get Thread Type: Periodic, Aperiodic, Sporadic, Hybrid, Timed or Background
		List<PropertyExpression> dispatchProtocolPropertyList = cf.getPropertyValues(Thread_Properties, Thread_Properties_Dispatch_Protocol);
		// only do something if the type of thread was defined
		if(dispatchProtocolPropertyList.size() > 0){
			PropertyExpression dispatchProtocolProperty = dispatchProtocolPropertyList.get(0); //TODO we don't consider modes at the moment
			if(dispatchProtocolProperty instanceof NamedValue){
				EnumerationLiteral namedValue = (EnumerationLiteral)((NamedValue)dispatchProtocolProperty).getNamedValue();
				dispatchProtocol = namedValue.getName();// should be "Periodic" or "Aperiodic" or one of the others
			}
		}
		if(dispatchProtocol.equals(""))
			log.warning("No Thread_Properties::Dispatch_Protocol was given for " + cf.getName() + ". Default is periodic");
		return Thread_Properties_Dispatch_Protocol_Periodic;
	}
	
	public static String getPeriodMilliSeconds(ComponentInstance ci){
		// get period
		List<PropertyExpression> periodPropertyList = ci.getPropertyValues(Timing_Properties, Timing_Properties_Period);
		// only do something if period was defined
		if(periodPropertyList.size() > 0){
			PropertyExpression periodProperty = periodPropertyList.get(0); //TODO: we don't consider modes at the moment
			Integer period = new Integer((int)((IntegerLiteral)periodProperty).getScaledValue(AADL_Project_Time_Units_Milli_Seconds));
			return period.toString();
		}
		log.warning("No Timing_Properties::Period was given for " + ci.getName() + ". Default for millisecond part is 200 ms");
		return "200";
	}
	
	public static String getPeriodNanoSeconds(ComponentInstance ci){
		// get period
		List<PropertyExpression> periodPropertyList = ci.getPropertyValues(Timing_Properties, Timing_Properties_Period);
		// only do something if period was defined
		if(periodPropertyList.size() > 0){
			PropertyExpression periodProperty = periodPropertyList.get(0); //TODO: we don't consider modes at the moment
			// at nanoseconds we only want the part that is below ms, so we modulo by 1 million
			Integer period = new Integer((int)(((IntegerLiteral)periodProperty).getScaledValue(AADL_Project_Time_Units_Nano_Seconds) % 1000000));
			return period.toString();
		}
		log.warning("No Timing_Properties::Period was given for " + ci.getName() + ". Default for nanosecond part is 0 ns");
		return "0";
	}
	
	public static String getPriority(ComponentInstance ci){
		// get priority
		List<PropertyExpression> priorityPropertyList = ci.getPropertyValues(Thread_Properties, Thread_Properties_Priority);
		// only do something if period was defined
		if(priorityPropertyList.size() > 0){
			PropertyExpression priorityProperty = priorityPropertyList.get(0); //TODO: we don't consider modes at the moment
			Integer priority = new Integer((int)(((IntegerLiteral)priorityProperty).getValue()));
			return priority.toString();
		}
		log.warning("No Thread_Properties::Priority was given for " + ci.getName() + ". Default is 5");
		return "5";
	}
}
