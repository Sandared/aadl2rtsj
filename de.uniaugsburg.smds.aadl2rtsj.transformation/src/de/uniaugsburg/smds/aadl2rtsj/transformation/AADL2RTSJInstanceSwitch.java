package de.uniaugsburg.smds.aadl2rtsj.transformation;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Constants.*;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.ClassifierFeature;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.Element;
import org.osate.aadl2.Mode;
import org.osate.aadl2.ModeFeature;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.PropertyValue;
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

import de.uniaugsburg.smds.aadl2rtsj.converter.ActiveDirectedConnectionConverter;
import de.uniaugsburg.smds.aadl2rtsj.converter.IOHandlerConverter;
import de.uniaugsburg.smds.aadl2rtsj.converter.InDataPortConverter;
import de.uniaugsburg.smds.aadl2rtsj.converter.OutDataPortConverter;
import de.uniaugsburg.smds.aadl2rtsj.converter.PassiveDirectedConnectionConverter;
import de.uniaugsburg.smds.aadl2rtsj.converter.PeriodicThreadConverter;
import de.uniaugsburg.smds.aadl2rtsj.utils.OffsetTime;

public class AADL2RTSJInstanceSwitch extends InstanceSwitch<String> {
	
	private IPackageFragmentRoot root;
	private IProgressMonitor monitor;

	public AADL2RTSJInstanceSwitch(IPackageFragmentRoot root, IProgressMonitor monitor){
		super();
		this.root = root;
		this.monitor = monitor;
	}
	
	private Set<Classifier> usedClassifier = new TreeSet<Classifier>(new ClassifierComparator());
	private List<InstanceObject> visitedObjects = new ArrayList<InstanceObject>();
	
	public Set<Classifier> getUsedClassifer(){
		return usedClassifier;
	}
	
	public List<InstanceObject> getVisitedObjects(){
		return visitedObjects;
	}
	
	// we just need something to abort switch execution for a given object. Otherwise the switch would traverse up the whole inheritance tree
	private static final String DONE = "";
	
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
					// create all handlers needed by this thread
					for (FeatureInstance feature : object.getAllFeatureInstances()) {
						// there will be one time if there is a immediate/delayed connection or an input connection
						// there will be one to multiple times if it is outgoing and sampled or untyped connection, or outgoing with several connections
						List<OffsetTime> times = getTimes(feature);
						for (OffsetTime time : times) {
							// only do something if there is an offset or if it is at deadline, at deadline we always have to use handlers
							if(time.getIOTime().equals(Communication_Properties_IO_Reference_Time_Deadline) || (time.getMs() != 0 || time.getNs() != 0)){
								String handlerCode = new IOHandlerConverter().generate(object, feature, time);
								createJavaClass(getPackageName(object), getHandlerClassName(feature, time), handlerCode, monitor, root);
							}
							//else: this will result in a method call within PeriodicThread, not the creation of a handler
						}
					}
					visitedObjects.add(object);
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
		case DATA:
			usedClassifier.add(object.getComponentClassifier());
			visitedObjects.add(object);
			break;
		default:
			break;
		}
		if(sourceCode != null)
			createJavaClass(getPackageName(object), getClassName(object), sourceCode, monitor, root);
		System.out.println("AADL2RTSJInstanceSwitch.caseComponentInstance()" + object);
		return DONE;
	}
	
	@Override
	public String caseConnectionInstance(ConnectionInstance object) {
		String sourceCode = null;
		boolean isActive = isActive(object);
		// TODO: we currently do not use a if/else, because there might be special types like data... 
		// passive directed connection if the next component in line is a thread, which reads actively from this connection
		if(!isActive)
			sourceCode = new PassiveDirectedConnectionConverter().generate(object);
		// active directed connection if the next component in line is a non_thread, which does not read actively from this connection
		if(isActive)
			sourceCode = new ActiveDirectedConnectionConverter().generate(object);
		createJavaClass(getPackageName(object), getClassName(object), sourceCode, monitor, root);
		visitedObjects.add(object);
		System.out.println("AADL2RTSJInstanceSwitch.caseConnectionInstance()" + object);
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
			//currently it doesn't make sense to generate data ports for components other than threads
			if(object.getComponentInstance().getCategory().equals(ComponentCategory.THREAD)){
				// store classifier for datatype generation
				Classifier classifier = object.getFeature().getClassifier();
				if(classifier != null)
					usedClassifier.add(classifier);
				
				DirectionType direction = object.getDirection();
				// in port
				if(direction.incoming() && !direction.outgoing())
					sourceCode = new InDataPortConverter().generate(object);
				// out port
				if(!direction.incoming() && direction.outgoing())
					sourceCode = new OutDataPortConverter().generate(object);
				//TODO: in out port
				visitedObjects.add(object);
			}
			break;

		default:
			System.out.println("AADL2RTSJInstanceSwitch.caseFeatureInstance()");
			break;
		}
		if(sourceCode != null)
			createJavaClass(getPackageName(object), getClassName(object), sourceCode, monitor, root);
		System.out.println("AADL2RTSJInstanceSwitch.caseFeatureInstance()" + object);
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
