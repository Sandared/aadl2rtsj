package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import static de.uniaugsburg.smds.aadl2rtsj.generation.util.Constants.Thread_Properties_Dispatch_Protocol_Periodic;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.ConnectedElement;
import org.osate.aadl2.Connection;
import org.osate.aadl2.ConnectionEnd;
import org.osate.aadl2.Context;
import org.osate.aadl2.DataImplementation;
import org.osate.aadl2.DataPort;
import org.osate.aadl2.DirectedFeature;
import org.osate.aadl2.Feature;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.Port;
import org.osate.aadl2.ProcessImplementation;
import org.osate.aadl2.Subcomponent;

import de.uniaugsburg.smds.aadl2rtsj.generation.ClassifierComparator;

public class ComponentClassifierHelper {
	
	private static final Logger log = Logger.getLogger(ComponentClassifierHelper.class.getName());
	
	//TODO: do we need this? check can be done in mtl file via oclIsTypeOf
	public static boolean isData(ComponentClassifier c){
		return c instanceof DataImplementation; // we only consider dataImplementation at the moment as classifiers
	}
	
	public static boolean isPeriodic(ComponentClassifier ti){
		return PropertyHelper.getDispatchProtocol(ti).equals(Thread_Properties_Dispatch_Protocol_Periodic);
	}
	
	public static List<Feature> getAllFeatures(ComponentClassifier cc){
		EList<Feature> result = cc.getAllFeatures();
		return cc.getAllFeatures();
	}
	
	public static List<Subcomponent> getAllSubComponents(ComponentImplementation ci){
		return ci.getAllSubcomponents();
	}
	
	public static List<Subcomponent> getOwnSubcomponents(ComponentImplementation ci){
		EList<Subcomponent> subcomponents = ci.getOwnedSubcomponents();
		return ci.getOwnedSubcomponents();
	}
	
	public static boolean isBaseType(ComponentClassifier classifier){
		return getNameSpace(classifier).equals("Base_Types");
	}	
	
	//TODO: kann man bestimmt weglassen
	private static String getNameSpace(ComponentClassifier classifier){
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
	
	public static boolean isRefined(Feature f){
		return f.getRefined() != null;
	}
	
	public static ComponentClassifier getClassifier(Subcomponent sc){
		return sc.getClassifier();
	}
	

	
	public static List<Connection> getAllConnections(ComponentImplementation ci){
		return ci.getAllConnections();
	}
	
	/**
	 * @param c
	 * @return either the Subcomponent or the ComponentImplementation that contains the port, that is the target of c
	 */
	public static NamedElement getTargetComponent(Connection c){
		NamedElement dest = c.getAllDstContextComponent();
		// although JavaDoc of getAllDstContextComponent states otherwise, it returns null, if the ContextComponent of c.destination is the enclosing ComponentImplementation
		if(dest == null)
			dest = c.getContainingComponentImpl();
		return dest;
	}
	
	/**
	 * @param c
	 * @return either the Subcomponent or the ComponentImplementation that contains the port, that is the source of c
	 */
	public static NamedElement getSourceComponent(Connection c){
		NamedElement src = c.getAllSrcContextComponent();
		// although JavaDoc of getAllSrcContextComponent states otherwise, it returns null, if the ContextComponent of c.destination is the enclosing ComponentImplementation
		if(src == null)
			src = c.getContainingComponentImpl();
		return src;
	}	
	
	/**
	 * @param c
	 * @return the target feature of c, which is either a DataPort or an EventDataPort, or an EventPort, or...
	 */
	public static ConnectionEnd getTargetFeature(Connection c){
		return c.getAllDestination();
	}
	
	/**
	 * @param c
	 * @return the source feature of c, which is either a DataPort or an EventDataPort, or an EventPort, or...
	 */
	public static ConnectionEnd getSourceFeature(Connection c){
		return c.getAllSource();
	}
	
	/**
	 * @param source Feature we want the outgoing connections for
	 * @param parent the ComponentImpl which contains the source Feature
	 * @return A list of connections within t that have source as their sourceContext
	 */
	public static List<Connection> getOutgoingConnections(Context source, ComponentImplementation component){
		List<Connection> outgoing = new ArrayList<Connection>();
		for (Connection connection : getAllConnections(component)) {
			if(connection.getAllSourceContext() != null && getSourceFeature(connection).equals(source))
				outgoing.add(connection);
		}
		return outgoing;
	}
	
	/**
	 * @param destination Feature we want the incoming connections for
	 * @param parent the ComponentImpl which contains the source Feature
	 * @return A list of connections within t that have destination as their destinationContext
	 */
	public static List<Connection> getIncomingConnections(Context destination, ComponentImplementation component){
		List<Connection> incoming = new ArrayList<Connection>();
		for (Connection connection : getAllConnections(component)) {
			if(connection.getAllDestinationContext() != null && getTargetFeature(connection).equals(destination))
				incoming.add(connection);
		}
		return incoming;
	}
	
	// ##############################################################
	// DEFINITLY NEEDED
	// ##############################################################
	
	/**
	 * Retrieves a Set of unique classifiers for the given ComponentType, encompassing:</br>
	 *  - the Classifier it is extending, if any</br>
	 *  - the Classifiers of its features</br>
	 *  - the Classifiers of the refined incoming features</br>
	 * @param ct
	 * @return a Set of unique classifiers that have to be imported by a Java class representing the given ComponentType
	 */
	public static Set<ComponentClassifier> getAllClassifiersForType(ComponentType ct){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		ComponentClassifier extended = getExtendedClassifier(ct);
		// if this ct is the upmost super type, then it has no extended classifier
		if(extended != null)
			classifiers.add(extended);
		classifiers.addAll(getAllFeatureClassifiers(ct));
		classifiers.addAll(getAllIncomingRefinedFeatureClassifier(ct));
		return classifiers;
	}
	
	/**
	 * Retrieves a Set of unique classifiers for the given ComponentImplementation, encompassing:</br>
	 *  - the Classifier it is extending, if any</br>
	 *  - the Classifier it realizes </br>
	 *  - the Classifiers of its subcomponents </br>
	 *  - the Classifiers of its features</br>
	 * @param ci
	 * @return a Set of unique classifiers that have to be imported by a Java class representing the given ComponentImplementation
	 */
	public static Set<ComponentClassifier> getAllClassifiersForImplementation(ComponentImplementation ci){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		ComponentClassifier extended = getExtendedClassifier(ci);
		// if this ci is the upmost super type, then it has no extended classifier
		if(extended != null)
			classifiers.add(extended);
		// a ComponentImplementation ALWAYS has a realized classifier
		classifiers.add(getRealizedClassifier(ci));
		classifiers.addAll(getAllSubcomponentClassifier(ci));
		classifiers.addAll(getAllFeatureClassifiers(ci));
		return classifiers;
	}
	
	/**
	 * Retrieves a Set of unique classifiers for the given ComponentImplementation, encompassing:</br>
	 *  - the Classifier of ci itself</br>
	 *  - the Classifiers of its subcomponents </br>
	 *  - the Classifiers of its outgoing features</br>
	 *  - the Classifiers of all its subcomponents' ingoing features</br>
	 * @param ci
	 * @return a Set of unique classifiers that have to be imported by a ConnectionBroker for the given ComponentImplementation
	 */
	public static Set<ComponentClassifier> getAllClassifiersForConnectionBroker(ComponentImplementation ci){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		classifiers.add(ci);
		classifiers.addAll(getAllSubcomponentClassifier(ci));
		classifiers.addAll(getAllOutgoingFeatureClassifier(ci));
		for (Subcomponent subcomponent : getAllSubComponents(ci)) {
			classifiers.addAll(getAllIncomingFeatureClassifier(subcomponent.getClassifier()));
		}
		return classifiers;
	}
	
	/**
	 * Retrieves a Set of unique classifiers for the given ComponentImplementation, encompassing:</br>
	 *  - the Classifier ci itself, as it is the extended type for an instance</br>
	 *  - the Classifiers of its outgoing features</br>
	 *  - the Classifiers of its subcomponents </br>
	 * @param ci
	 * @return a Set of unique classifiers that have to be imported by an instance of the given ComponentImplementation
	 */
	public static Set<ComponentClassifier> getAllClassifiersForInstance(ComponentImplementation ci){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		classifiers.add(ci);
		classifiers.addAll(getAllOutgoingFeatureClassifier(ci));
		classifiers.addAll(getAllSubcomponentClassifier(ci));
		return classifiers;
	}
	
	/**
	 * @param cc the ComopnentClassifier one wants the extended Classifier for
	 * @return The Classifier of the extended ComponentClassifier or null if no other ComponentClassifier was extended
	 */
	public static ComponentClassifier getExtendedClassifier(ComponentClassifier cc){
		return (ComponentClassifier) cc.getExtended();
	}
	
	/**
	 * All features of cc are traversed and their classifiers are added to the resulting Set</br>
	 * The respective classifier is not added if:</br>
	 * - the same classifier is already in the Set</br>
	 * - the classifier is a BaseType</br>
	 * - the classifier is only outgoing
	 * @param cc the ComponentClassifier one wants all incoming feature classifiers for
	 * @return a Set of unique classifiers of all incoming features of the given ComponentClassifier
	 */
	public static Set<ComponentClassifier> getAllIncomingFeatureClassifier(ComponentClassifier cc){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		for (Feature feature : cc.getAllFeatures()) {
			if(isIncoming(feature)){
				ComponentClassifier classifier = getClassifier(feature);
				if (!isBaseType(classifier)) {
					classifiers.add(getClassifier(feature));
				}
			}
		}
		return classifiers;
	}
	
	/**
	 * All features of cc are traversed and if they are incoming and refined, then their refined classifiers are added to the resulting Set</br>
	 * The respective classifier is not added if:</br>
	 * - the same classifier is already in the Set</br>
	 * - the classifier is a BaseType</br>
	 * - the classifier is only outgoing
	 * @param cc the ComponentClassifier one wants all incoming, refined feature classifiers for
	 * @return a Set of unique classifiers of all incoming, refined features of the given ComponentClassifier
	 */
	public static Set<ComponentClassifier> getAllIncomingRefinedFeatureClassifier(ComponentClassifier cc){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		for (Feature feature : cc.getAllFeatures()) {
			if(isIncoming(feature)){
				ComponentClassifier classifier = getClassifier(feature);
				if (!isBaseType(classifier)) {
					ComponentClassifier refined = getRefinedClassifier(feature);
					if(refined != null)
						classifiers.add(refined);
				}
			}
		}
		return classifiers;
	}
	
	/**
	 * All features of cc are traversed and their classifiers are added to the resulting Set</br>
	 * The respective classifier is not added if:</br>
	 * - the same classifier is already in the Set</br>
	 * - the classifier is a BaseType</br>
	 * - the classifier is only incoming
	 * @param cc the ComponentClassifier one wants all incoming feature classifiers for
	 * @return a Set of unique classifiers of all incoming features of the given ComponentClassifier
	 */
	public static Set<ComponentClassifier> getAllOutgoingFeatureClassifier(ComponentClassifier cc){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		for (Feature feature : cc.getAllFeatures()) {
			if(isOutgoing(feature)){
				ComponentClassifier classifier = getClassifier(feature);
				if (!isBaseType(classifier)) {
					classifiers.add(getClassifier(feature));
				}
			}
		}
		return classifiers;
	}
	
	/**
	 * @param f the feature one wants a ComponentClassifier for
	 * @return the refined classifier for the given feature or <code>null</code> if the feature is not refined
	 */
	public static ComponentClassifier getRefinedClassifier(Feature f){
		Feature refined = f.getRefined();
		if (refined != null)
			return (ComponentClassifier) refined.getClassifier();
		else 
			return null;
	}
	
	/**
	 * According to AADL every ComponentImplementation MUST have a ComponentType which it realizes.</br>
	 * The classifier of this ComponentType is retrieved by this method.
	 * @param ci the ComponentImplementation one wants the realized classifier for
	 * @return the realized classifier for the given ComponentImplementation. 
	 */
	public static ComponentClassifier getRealizedClassifier(ComponentImplementation ci){
		return ci.getType();
	}
	
	/**
	 * @param f the feature one wants the classifier for.
	 * @return the classifier for the given feature or <code>null</code> if no classifier was defined
	 */
	public static ComponentClassifier getClassifier(Feature f){
		return (ComponentClassifier) f.getClassifier();
	}
	
	/**
	 * All features of cc are traversed and their classifiers are added to the resulting Set </br>
	 * Additionally, if the feature is incoming and refined, then the refined classifier is added too</br>
	 * The respective classifier is not added if:</br>
	 * - the same classifier is already in the Set</br>
	 * - the classifier is a BaseType
	 * @param cc 
	 * @return A Set of unique Feature Classifiers for the given ComponentClassifier
	 */
	public static Set<ComponentClassifier> getAllFeatureClassifiers(ComponentClassifier cc){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		for (Feature feature : cc.getAllFeatures()) {
			ComponentClassifier classifier = getClassifier(feature);
			if (!isBaseType(classifier)) {
				classifiers.add(getClassifier(feature));
				if(isIncoming(feature) && isRefined(feature))
					classifiers.add(getRefinedClassifier(feature));
			}
		}
		return classifiers;
	}	
	
	/**
	 * All subcomponents of ci are traversed and their classifiers are added to the resulting Set </br>
	 * The respective classifier is not added if:</br>
	 * - the same classifier is already in the Set</br>
	 * - the classifier is a BaseType
	 * @param ci
	 * @return a Set of unique ComponentClassifiers for the subcomponents of the given ci
	 */
	public static Set<ComponentClassifier> getAllSubcomponentClassifier(ComponentImplementation ci){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		for (Subcomponent subcomponent : getAllSubComponents(ci)) {
			ComponentClassifier classifier = subcomponent.getClassifier();
			if(classifier != null && !isBaseType(classifier))
				classifiers.add(classifier);
			else
				log.warning("No classifier was given for subcomponent " + subcomponent);
		}
		return classifiers;
	}
	
	/**
	 * This method removes the usual dot between type and implementation part of the ComponentImplementation name
	 * @param ci the ComponentImplementation one wants a class name for
	 * @return a String representing the class name for the given ComponentImplementation or 'Object' if <b>ci</b> is <code>null</code>
	 */
	public static String getClassNameForImpl(ComponentImplementation ci){
		return (ci == null)? getDefaultClassName() : ci.getName().replace(".", "");
	}
	
	/**
	 * @param ct the ComponentType one wants a class name for
	 * @return a String representing the class name for the given ComponentType or 'Object' if <b>ci</b> is <code>null</code>
	 */
	public static String getClassNameForType(ComponentType ct){
		return (ct == null)? getDefaultClassName() : ct.getName();
	}
	
	/**
	 * @param cc the ComponentClassifier one wants a class name for
	 * @return a String representing the class name for the given ComponentClassifier or 'Object' if <b>cc</b> is <code>null</code>
	 */
	public static String getClassName(ComponentClassifier cc){
		if(cc instanceof ComponentType)
			return getClassNameForType((ComponentType)cc);
		else
			return getClassNameForImpl((ComponentImplementation)cc);
	}
	
	private static String getDefaultClassName(){
		log.warning("No classifier was given. Default is Object");
		return "Object";
	}
	
	/**
	 * A ComponentClassifier is always put into the package 'classifier'. The rest of the packagename is constructed as follows:</br>
	 * We take two values of the AADL model into consideration:</br>
	 * - <code>cc.getNamespace.getName()</code> which is in the form 'packagename'_'visibility'</br>
	 * - <code>cc.getQualifiedName()</code> which is in the form 'packagename'::'typename'</br>
	 * 'visibility' public/private is replaced by internal/external in order to form a valid java package identifier
	 * The concatenated String is composed in the form of 'packagename'+'visibility'+'typename'</br>
	 * All '_' in packagename and visibility are replaced by '.'</br>
	 * All '::' in packagename and visibility are replaced by '.' </br> 
	 * @param cc 
	 * @return a valid java namespace for cc. 
	 */
	public static String getPackageName(ComponentClassifier cc){
		StringBuilder sb = new StringBuilder();
		sb.append("classifier");
		sb.append(".");
		
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
		
		sb.append(aadlPkg + typeName);		
		return sb.toString().toLowerCase();
	}
	
	public static boolean isIncoming(Feature f){
		if(f instanceof DirectedFeature)
			return ((DirectedFeature) f).getDirection().incoming();
		log.warning("Feature " + f  + " is not a Directed Feature! Default 'true' is given for incoming");
		return true;// Default
	}
	
	public static boolean isOutgoing(Feature f){
		if(f instanceof DirectedFeature)
			return ((DirectedFeature) f).getDirection().outgoing();
		log.warning("Feature " + f  + " is not a Directed Feature! Default 'true' is given for outgoing");
		return true;// Default
	}
}
