package de.uniaugsburg.smds.aadl2rtsj.transformation;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.ClassifierFeature;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.Element;
import org.osate.aadl2.Mode;
import org.osate.aadl2.ModeFeature;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.Property;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.PropertyValue;
import org.osate.aadl2.Type;
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

import de.uniaugsburg.smds.aadl2rtsj.converter.DataPortConverter;
import de.uniaugsburg.smds.aadl2rtsj.converter.DirectedConnectionConverter;
import de.uniaugsburg.smds.aadl2rtsj.converter.OutDataPortConverter;
import de.uniaugsburg.smds.aadl2rtsj.converter.PeriodicThreadConverter;

public class AADL2RTSJInstanceSwitch extends InstanceSwitch<String> {
	
	private IPackageFragmentRoot root;
	private IProgressMonitor monitor;

	public AADL2RTSJInstanceSwitch(IPackageFragmentRoot root, IProgressMonitor monitor){
		super();
		this.root = root;
		this.monitor = monitor;
	}
	
	private Set<Classifier> usedClassifier = new HashSet<Classifier>();
	
	public Set<Classifier> getUsedClassifer(){
		return usedClassifier;
	}
	
	// we just need something to abort switch execution for a given object. Otherwise the switch would traverse up the whole inheritance tree
	private static final String DONE = "";
	
	private boolean createJavaClass(String packageName, String className, String sourceCode) {
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

	@Override
	public String caseAnnexInstance(AnnexInstance object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseAnnexInstance()" + object);
		return DONE;
	}

	@Override
	public String caseClassifierFeature(ClassifierFeature object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseClassifierFeature()" + object);		
		return DONE;
	}

	@Override
	public String caseComponentInstance(ComponentInstance object) {
		String sourceCode = null;
		switch (object.getCategory()) {
		case THREAD:
			
			String dispatchProtocol = getDispatchProtocol(object);
			if(dispatchProtocol != null)	
				switch (dispatchProtocol) {
				case Thread_Properties_Dispatch_Protocol_Periodic:
					sourceCode = new PeriodicThreadConverter().generate(object);
					break;
				case Thread_Properties_Dispatch_Protocol_Aperiodic:
					//TODO: implement
					break;
				case Thread_Properties_Dispatch_Protocol_Sporadic:
					//TODO: implement
					break;
				case Thread_Properties_Dispatch_Protocol_Timed:
					//TODO: implement
					break;
				case Thread_Properties_Dispatch_Protocol_Hybrid:
					//TODO: implement
					break;
				case Thread_Properties_Dispatch_Protocol_Background:
					//TODO: implement
					break;
				default:
					break;
				}
			break;
			
		default:
			break;
		}
		if(sourceCode != null)
			createJavaClass(getPackageName(object), getClassName(object), sourceCode);
		return DONE;
	}
	
	@Override
	public String caseConnectionInstance(ConnectionInstance object) {
		String sourceCode = new DirectedConnectionConverter().generate(object);;
		createJavaClass(getPackageName(object), getClassName(object), sourceCode);
		return DONE;
	}

	@Override
	public String caseConnectionInstanceEnd(ConnectionInstanceEnd object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseConnectionInstanceEnd()" + object);
		return DONE;
	}

	@Override
	public String caseConnectionReference(ConnectionReference object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseConnectionReference()" + object);
		return DONE;
	}

	@Override
	public String caseElement(Element object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseElement()" + object);
		return DONE;
	}

	@Override
	public String caseEndToEndFlowInstance(EndToEndFlowInstance object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseEndToEndFlowInstance()" + object);
		return DONE;
	}

	@Override
	public String caseFeatureInstance(FeatureInstance object) {
		String sourceCode = null;
		switch (object.getCategory()) {
		case DATA_PORT:
			// store classifier for datatype generation
			Classifier classifier = object.getFeature().getClassifier();
			if(classifier != null)
				usedClassifier.add(classifier);
			
			DirectionType direction = object.getDirection();
			// in port
			if(direction.incoming() && !direction.outgoing())
				sourceCode = new DataPortConverter().generate(object);
			// out port
			if(!direction.incoming() && direction.outgoing())
				sourceCode = new OutDataPortConverter().generate(object);
			//TODO: in out port
			break;

		default:
			System.out.println("AADL2RTSJInstanceSwitch.caseFeatureInstance()");
			break;
		}
		if(sourceCode != null)
			createJavaClass(getPackageName(object), getClassName(object), sourceCode);
		return DONE;
	}

	@Override
	public String caseFlowElementInstance(FlowElementInstance object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseFlowElementInstance()" + object);
		return DONE;
	}

	@Override
	public String caseFlowSpecificationInstance(FlowSpecificationInstance object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseFlowSpecificationInstance()" + object);
		return DONE;
	}

	@Override
	public String caseInstanceObject(InstanceObject object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseInstanceObject()" + object);
		return DONE;
	}

	@Override
	public String caseInstanceReferenceValue(InstanceReferenceValue object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseInstanceReferenceValue()" + object);
		return DONE;
	}

	@Override
	public String caseMode(Mode object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseMode()" + object);
		return DONE;
	}

	@Override
	public String caseModeFeature(ModeFeature object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseModeFeature()" + object);
		return DONE;
	}

	@Override
	public String caseModeInstance(ModeInstance object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseModeInstance()" + object);
		return DONE;
	}

	@Override
	public String caseModeTransitionInstance(ModeTransitionInstance object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseModeTransitionInstance()" + object);
		return DONE;
	}

	@Override
	public String caseNamedElement(NamedElement object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseNamedElement()" + object);
		return DONE;
	}

	@Override
	public String casePropertyAssociation(PropertyAssociation object) {
		System.out.println("AADL2RTSJInstanceSwitch.casePropertyAssociation()" + object);
		return DONE;
	}

	@Override
	public String casePropertyAssociationInstance(PropertyAssociationInstance object) {
		System.out.println("AADL2RTSJInstanceSwitch.casePropertyAssociationInstance()" + object);
		Property prop = object.getProperty();
		Type proptype = prop.getType();
		return DONE;
	}

	@Override
	public String casePropertyExpression(PropertyExpression object) {
		System.out.println("AADL2RTSJInstanceSwitch.casePropertyExpression()" + object);
		return DONE;
	}

	@Override
	public String casePropertyValue(PropertyValue object) {
		System.out.println("AADL2RTSJInstanceSwitch.casePropertyValue()" + object);
		return DONE;
	}

	@Override
	public String caseSystemInstance(SystemInstance object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseSystemInstance()" + object);
		return DONE;
	}

	@Override
	public String caseSystemOperationMode(SystemOperationMode object) {
		System.out.println("AADL2RTSJInstanceSwitch.caseSystemOperationMode()" + object);
		return DONE;
	}

}
