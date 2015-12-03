package de.uniaugsburg.aadl2rtsj.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.ClassifierFeature;
import org.osate.aadl2.Element;
import org.osate.aadl2.IntegerLiteral;
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

import de.uniaugsburg.aadl2rtsj.converter.ThreadConverter;

public class AADL2RTSJInstanceSwitch extends InstanceSwitch<String> {
	
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
		switch (object.getCategory()) {
		case THREAD:
			File file = new File("C:\\Users\\Administrator\\Desktop\\test.java");
			if(!file.exists())
				try {
					if(!file.createNewFile()){
						System.err.println("Could not create File");
					}
					else{
						FileOutputStream fos = new FileOutputStream(file);
						fos.write(ThreadConverter.create("").generate(object).getBytes());
						fos.flush();
						fos.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;

		default:
			EList<FeatureInstance> features = object.getAllFeatureInstances(); // Features
			System.out.println("features: " + features);
			EList<ComponentInstance> subcomponents = object.getAllComponentInstances(); // Subcomponents (including the component itself at the first place of the resulting list)
			System.out.println("subcomponents: " + subcomponents);
			EList<PropertyAssociation> properties = object.getOwnedPropertyAssociations(); // property associations
			System.out.println("properties: " + properties);
			
			for (PropertyAssociation asso : properties) {
				Property prop = asso.getProperty(); // the actual property
				String name = prop.getName(); // name of the property
				PropertyExpression valueExpression = object.getSimplePropertyValue(prop); // easiest way to get the value of single-valued, non-modal properties like Period
				Long value = null;
				if(valueExpression instanceof IntegerLiteral)
					value = ((IntegerLiteral)valueExpression).getValue();
				System.out.println("name: " + name);
				System.out.println("value: " + value);
			}
			System.out.println(object.getNamespace()); // returns null -> nutzlos?
			System.out.println(object.getName()); // entspricht variablenname oder wenn der erste Buchstabe groﬂ geschrieben wird, der Klasse 
			System.out.println(object.getComponentInstancePath()); // entspricht dem package name, ohne den systemnamen
			System.out.println(object.getComponentClassifier());
			System.out.println(object.getSystemInstance()); // system.getName() ohne die Endung "_impl_Instance" ist der Anfang jedes packagenamens
			break;
		}
		
		
		
		
		return DONE;
	}

	@Override
	public String caseConnectionInstance(ConnectionInstance object) {
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
