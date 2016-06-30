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
import org.osate.aadl2.Subcomponent;

import de.uniaugsburg.smds.aadl2rtsj.generation.ClassifierComparator;

public class ComponentClassifierHelper {
	
	private static final Logger log = Logger.getLogger(ComponentClassifierHelper.class.getName());
	
	public static String getClassName(ComponentClassifier cf){
		if(cf == null){
			log.warning("No classifier was given. Default is Object");
			return "Object";
		}
		if(cf instanceof ComponentImplementation)
			return ((ComponentImplementation) cf).getImplementationName();
		return cf.getName();
	}
	
	/**
	 * A ComponentClassifier is always put into the package 'classifier'. The rest of the packagename is constructed as follows:</br>
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
		sb.append("classifier");
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
	
	//TODO: do we need this? check can be done in mtl file via oclIsTypeOf
	public static boolean isData(ComponentClassifier c){
		return c instanceof DataImplementation; // we only consider dataImplementation at the moment as classifiers
	}
	
	public static boolean isPeriodic(ComponentClassifier ti){
		return PropertyHelper.getDispatchProtocol(ti).equals(Thread_Properties_Dispatch_Protocol_Periodic);
	}
	
	public static List<Feature> getFeatures(ComponentClassifier cc){
		return cc.getAllFeatures();
	}
	
	public static List<Subcomponent> getSubComponents(ComponentImplementation ci){
		return ci.getAllSubcomponents();
	}
	
	public static List<Subcomponent> getOwnSubcomponents(ComponentImplementation ci){
		return ci.getOwnedSubcomponents();
	}
	
	/**
	 * All subcomponents of ci are traversed and their classifiers are added to the resulting Set </br>
	 * The respective classifier is not added if:</br>
	 * - the same classifier is already in the Set</br>
	 * - the classifier is a BaseType
	 * @param ci
	 * @return a Set of unique ComponentClassifiers for the subcomponents of the given ci
	 */
	public static Set<ComponentClassifier> getSubComponentClassifiers(ComponentImplementation ci){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		for (Subcomponent subcomponent : getSubComponents(ci)) {
			ComponentClassifier classifier = subcomponent.getClassifier();
			if(classifier != null && !isBaseType(classifier))
				classifiers.add(classifier);
			else
				log.warning("No classifier was given for subcomponent " + subcomponent);
		}
		return classifiers;
	}
	
	public static boolean isBaseType(ComponentClassifier classifier){
		return getNameSpace(classifier).equals("Base_Types");
	}	
	
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
	
	public static ComponentClassifier getRefinedClassifier(Feature f){
		return (ComponentClassifier) f.getRefined().getClassifier();
	}
	
	public static ComponentClassifier getExtendedClassifier(ComponentClassifier cc){
		return (ComponentClassifier) cc.getExtended();
	}
	
	public static ComponentClassifier getRealizedClassifier(ComponentImplementation ci){
		return ci.getType();
	}
	
	public static ComponentClassifier getClassifier(Feature f){
		return (ComponentClassifier) f.getClassifier();
	}
	
	public static ComponentClassifier getClassifier(Subcomponent sc){
		return sc.getClassifier();
	}
	
	/**
	 * All features of cc are traversed and their classifiers are added to the resulting Set </br>
	 * Additionally, if the feature is refined, then the refined classifier is added too</br>
	 * The respective classifier is not added if:</br>
	 * - the same classifier is already in the Set</br>
	 * - the classifier is a BaseType
	 * @param cc 
	 * @return A Set of unique Feature Classifiers for the given ComponentClassifier
	 */
	public static Set<ComponentClassifier> getFeatureClassifiers(ComponentClassifier cc){
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
	
	public static List<Connection> getAllConnections(ComponentImplementation ci){
		return ci.getAllConnections();
	}
	
	/**
	 * @param c
	 * @return either the Subcomponent or the ComponentImplementation that contains the port, that is the target of c
	 */
	public static NamedElement getTargetComponent(Connection c){
		ConnectedElement destination = c.getDestination();
		ConnectionEnd allDestination = c.getAllDestination();
		Context allDestinationContext = c.getAllDestinationContext();
		NamedElement allDstContextComponent = c.getAllDstContextComponent();
		Classifier containingClassifier = destination.getContainingClassifier();
		ComponentImplementation containingComponentImpl = destination.getContainingComponentImpl();
		Context context = destination.getContext();
		return c.getAllDstContextComponent();
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
	 * @param c
	 * @return either the Subcomponent or the ComponentImplementation that contains the port, that is the source of c
	 */
	public static NamedElement getSourceComponent(Connection c){
		return c.getAllSrcContextComponent();
	}	
	
	/**
	 * Internally calls:</br>
	 * - getAllFeatureClassifiers(cc)</br>
	 * - getExtendedClassifier(cc)</br>
	 * And, only if <code>(cc instanceof ComponentImplementation) == true):</code></br>
	 * - getRealizedClassifier(cc)</br>
	 * - getAllSubcomponentClassifiers(cc)</br>
	 * - for each Subcomponent sc: getAllFeatureClassifiers(sc).
	 * @param cc
	 * @return a Set of unique ComponentClassifiers that contains all Classifiers that have to be imported by a Component.
	 */
	public static Set<ComponentClassifier> getAllClassifiers(ComponentClassifier cc){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		classifiers.addAll(getFeatureClassifiers(cc));
		if (cc instanceof ComponentImplementation) {
			ComponentImplementation ci = (ComponentImplementation)cc;
			classifiers.add(getRealizedClassifier(ci));
			for (Subcomponent sc : getSubComponents(ci)) {
				ComponentClassifier scc = sc.getClassifier();
				if (scc != null && !isBaseType(scc)) {
					classifiers.add(scc);
					classifiers.addAll(getFeatureClassifiers(scc));
				}
			}
		}
		if(getExtendedClassifier(cc) != null)
			classifiers.add(getExtendedClassifier(cc));
		return classifiers;
	}
	
	/**
	 * @param source Feature we want the outgoing connections for
	 * @param parent the parent ComponentImpl of the ComponentImpl which contains the source Feature
	 * @return A list of connections within parent that have source as their sourceContext
	 */
	public static List<Connection> getExternalOutgoingConnections(Context source, ComponentImplementation parent){
		List<Connection> outgoing = new ArrayList<Connection>();
		for (Connection connection : getAllConnections(parent)) {
			if(connection.getAllSourceContext().equals(source))
				outgoing.add(connection);
		}
		return outgoing;
	}
	
	/**
	 * @param source Feature we want the outgoing connections for
	 * @param parent the ComponentImpl which contains the source Feature
	 * @return A list of connections within t that have source as their sourceContext
	 */
	public static List<Connection> getInternalOutgoingConnections(Context source, ComponentImplementation component){
		List<Connection> outgoing = new ArrayList<Connection>();
		for (Connection connection : getAllConnections(component)) {
			if(connection.getAllSourceContext() != null && connection.getAllSourceContext().equals(source))
				outgoing.add(connection);
		}
		return outgoing;
	}
	
	/**
	 * @param destination Feature we want the incoming connections for
	 * @param parent the ComponentImpl which contains the source Feature
	 * @return A list of connections within t that have destination as their destinationContext
	 */
	public static List<Connection> getInternalIncomingConnections(Context destination, ComponentImplementation component){
		List<Connection> incoming = new ArrayList<Connection>();
		for (Connection connection : getAllConnections(component)) {
			if(connection.getAllDestinationContext().equals(destination))
				incoming.add(connection);
		}
		return incoming;
	}


}
