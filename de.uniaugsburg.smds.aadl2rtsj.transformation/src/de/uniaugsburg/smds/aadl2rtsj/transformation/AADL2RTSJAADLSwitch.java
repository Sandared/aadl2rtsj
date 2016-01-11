package de.uniaugsburg.smds.aadl2rtsj.transformation;

import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.getClassName;
import static de.uniaugsburg.smds.aadl2rtsj.utils.Utils.getPackageName;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.DataImplementation;
import org.osate.aadl2.Subcomponent;
import org.osate.aadl2.ThreadImplementation;
import org.osate.aadl2.util.Aadl2Switch;

import de.uniaugsburg.smds.aadl2rtsj.converter.DataConverter;
import de.uniaugsburg.smds.aadl2rtsj.converter.DataImplConverter;

public class AADL2RTSJAADLSwitch extends Aadl2Switch<String>{
	
	private IPackageFragmentRoot root;
	private IProgressMonitor monitor;
	private Set<Classifier> usedClassifier;

	public AADL2RTSJAADLSwitch(IPackageFragmentRoot root, IProgressMonitor monitor, Set<Classifier> usedClassifier){
		super();
		this.root = root;
		this.monitor = monitor;
		this.usedClassifier = usedClassifier;
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
	public String caseDataImplementation(DataImplementation object) {
		if(usedClassifier.contains(object)){
			String sourceCode = new DataImplConverter().generate(object);
			createJavaClass(getPackageName(object), getClassName(object), sourceCode);
		}
		return DONE;
	}
}
