package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import static de.uniaugsburg.smds.aadl2rtsj.generation.utils.Constants.Thread_Properties_Dispatch_Protocol_Periodic;

import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.DataImplementation;
import org.osate.aadl2.DirectedFeature;
import org.osate.aadl2.Feature;
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
	
	public static EList<Feature> getFeatures(ComponentClassifier cc){
		return cc.getAllFeatures();
	}
	
	public static EList<Subcomponent> getSubComponents(ComponentImplementation ci){
		return ci.getAllSubcomponents();
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
	 * @return A Set of unique DataPort Classifiers for the given ComponentClassifier
	 */
	public static Set<ComponentClassifier> getFeatureClassifiers(ComponentClassifier cc){
		Set<ComponentClassifier> classifiers = new TreeSet<ComponentClassifier>(new ClassifierComparator());
		if (!isBaseType(cc)) {
			for (Feature feature : cc.getAllFeatures()) {
				classifiers.add(getClassifier(feature));
				if(isIncoming(feature) && isRefined(feature))
					classifiers.add(getRefinedClassifier(feature));
			}
		}
		return classifiers;
	}

}
