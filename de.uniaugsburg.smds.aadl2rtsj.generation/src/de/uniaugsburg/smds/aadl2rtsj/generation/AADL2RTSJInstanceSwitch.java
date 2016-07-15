package de.uniaugsburg.smds.aadl2rtsj.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.osate.aadl2.ClassifierFeature;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.Element;
import org.osate.aadl2.Mode;
import org.osate.aadl2.ModeFeature;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.PropertyValue;
import org.osate.aadl2.impl.ComponentImplementationImpl;
import org.osate.aadl2.instance.AnnexInstance;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.ConnectionInstanceEnd;
import org.osate.aadl2.instance.ConnectionReference;
import org.osate.aadl2.instance.EndToEndFlowInstance;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.FlowElementInstance;
import org.osate.aadl2.instance.FlowSpecificationInstance;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.InstanceReferenceValue;
import org.osate.aadl2.instance.ModeInstance;
import org.osate.aadl2.instance.ModeTransitionInstance;
import org.osate.aadl2.instance.PropertyAssociationInstance;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.instance.SystemOperationMode;
import org.osate.aadl2.instance.util.InstanceSwitch;

import de.uniaugsburg.smds.aadl2rtsj.generation.services.ComponentClassifierHelper;

public class AADL2RTSJInstanceSwitch extends InstanceSwitch<String> {
	
	public AADL2RTSJInstanceSwitch(){
		super();
	}
	
	private Set<ComponentClassifier> usedClassifier = new TreeSet<ComponentClassifier>(new ClassifierComparator());
	private List<InstanceObject> visitedObjects = new ArrayList<InstanceObject>();
	
	public Set<ComponentClassifier> getUsedClassifer(){
		return usedClassifier;
	}
	
	public List<InstanceObject> getVisitedObjects(){
		return visitedObjects;
	}
	
	// we just need something to abort switch execution for a given object. Otherwise the switch would traverse up the whole inheritance tree
	private static final String DONE = "";
	
	@Override
	public String caseAnnexInstance(AnnexInstance object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseAnnexInstance()" + object);
		return DONE;
	}

	@Override
	public String caseClassifierFeature(ClassifierFeature object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseClassifierFeature()" + object);		
		return DONE;
	}

	@Override
	public String caseComponentInstance(ComponentInstance object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseComponentInstance()" + object);
		addHierarchyClassifers(object.getComponentClassifier());
		return DONE;
	}
	
	private void addHierarchyClassifers(ComponentClassifier classifier){
		if(classifier != null && !ComponentClassifierHelper.isBaseType(classifier)){
			EcoreUtil.resolveAll(classifier);// resolve possible proxies
			usedClassifier.add(classifier);
			ComponentClassifier extension = (ComponentClassifier) classifier.getExtended();// Extension
			if(classifier instanceof ComponentImplementationImpl){
				ComponentType realization = ((ComponentImplementationImpl)classifier).basicGetType();// Realization
				usedClassifier.add(realization);
				//in caseof an implementation realizing a type but not extending another implementation
				//there might still be a hierarchy upwards the realized type
				if(extension == null)
					extension = realization;
			}
			addHierarchyClassifers(extension);
		}
	}
	
	@Override
	public String caseConnectionInstance(ConnectionInstance object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseConnectionInstance()" + object);
		return DONE;
	}

	@Override
	public String caseConnectionInstanceEnd(ConnectionInstanceEnd object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseConnectionInstanceEnd()" + object);
		return DONE;
	}

	@Override
	public String caseConnectionReference(ConnectionReference object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseConnectionReference()" + object);
		return DONE;
	}

	@Override
	public String caseElement(Element object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseElement()" + object);
		return DONE;
	}

	@Override
	public String caseEndToEndFlowInstance(EndToEndFlowInstance object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseEndToEndFlowInstance()" + object);
		return DONE;
	}

	@Override
	public String caseFeatureInstance(FeatureInstance object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseFeatureInstance()" + object);
		addHierarchyClassifers((ComponentClassifier) object.getFeature().getClassifier());
		return DONE;
	}

	@Override
	public String caseFlowElementInstance(FlowElementInstance object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseFlowElementInstance()" + object);
		return DONE;
	}

	@Override
	public String caseFlowSpecificationInstance(FlowSpecificationInstance object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseFlowSpecificationInstance()" + object);
		return DONE;
	}

	@Override
	public String caseInstanceObject(InstanceObject object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseInstanceObject()" + object);
		return DONE;
	}

	@Override
	public String caseInstanceReferenceValue(InstanceReferenceValue object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseInstanceReferenceValue()" + object);
		return DONE;
	}

	@Override
	public String caseMode(Mode object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseMode()" + object);
		return DONE;
	}

	@Override
	public String caseModeFeature(ModeFeature object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseModeFeature()" + object);
		return DONE;
	}

	@Override
	public String caseModeInstance(ModeInstance object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseModeInstance()" + object);
		return DONE;
	}

	@Override
	public String caseModeTransitionInstance(ModeTransitionInstance object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseModeTransitionInstance()" + object);
		return DONE;
	}

	@Override
	public String caseNamedElement(NamedElement object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseNamedElement()" + object);
		return DONE;
	}

	@Override
	public String casePropertyAssociation(PropertyAssociation object) {
//		System.out.println("AADL2RTSJInstanceSwitch.casePropertyAssociation()" + object);
		return DONE;
	}

	@Override
	public String casePropertyAssociationInstance(PropertyAssociationInstance object) {
//		System.out.println("AADL2RTSJInstanceSwitch.casePropertyAssociationInstance()" + object);
		return DONE;
	}

	@Override
	public String casePropertyExpression(PropertyExpression object) {
//		System.out.println("AADL2RTSJInstanceSwitch.casePropertyExpression()" + object);
		return DONE;
	}

	@Override
	public String casePropertyValue(PropertyValue object) {
//		System.out.println("AADL2RTSJInstanceSwitch.casePropertyValue()" + object);
		return DONE;
	}

	@Override
	public String caseSystemInstance(SystemInstance object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseSystemInstance()" + object);
		addHierarchyClassifers(object.getComponentClassifier());
		return DONE;
	}

	@Override
	public String caseSystemOperationMode(SystemOperationMode object) {
//		System.out.println("AADL2RTSJInstanceSwitch.caseSystemOperationMode()" + object);
		return DONE;
	}

}
