package de.uniaugsburg.smds.aadl2rtsj.transformation;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.osate.aadl2.Element;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.modelsupport.modeltraversal.AadlProcessingSwitchWithProgress;
import org.osate.ui.actions.AaxlReadOnlyActionAsJob;

import de.uniaugsburg.smds.aadl2rtsj.converter.MainConverter;
import de.uniaugsburg.smds.aadl2rtsj.utils.Utils;

public class DoRTSJGeneration extends AaxlReadOnlyActionAsJob {

	private IPackageFragmentRoot rootPackage = null;

	@Override
	protected String getActionName() {
		return "RTSJ Generation";
	}

	@Override
	protected void doAaxlAction(IProgressMonitor monitor, Element root) {
		System.out.println("DoRTSJGeneration.doAaxlAction()");

		monitor.beginTask("Generating RTSJ Code", IProgressMonitor.UNKNOWN);

		// Get the system instance (if any)
		SystemInstance si;
		if (root instanceof InstanceObject)
			si = ((InstanceObject) root).getSystemInstance();
		else
			si = null;

		// create instance switch, aadl switch is not needed so we use the
		// default
		AadlProcessingSwitchWithProgress mySwitch = new AadlProcessingSwitchWithProgress(monitor, errManager) {
			

			@Override
			protected void initSwitches() {
				AADL2RTSJInstanceSwitch myInstanceSwitch = new AADL2RTSJInstanceSwitch(rootPackage, monitor);
				instanceSwitch = myInstanceSwitch;
				aadl2Switch = new AADL2RTSJAADLSwitch(rootPackage, monitor, myInstanceSwitch.getUsedClassifer());
			}
		};
		

		// Setup a new Java project
		setupJavaProject(monitor);

		// run traversal which does the actual generation
		if (si != null) {
			mySwitch.defaultTraversal(si);
			mySwitch.defaultTraversalAllDeclarativeModels();
			createMainClass(((AADL2RTSJInstanceSwitch)mySwitch.getInstanceSwitch()).getVisitedObjects(), monitor);
		} else {
			System.err.println("Not an InstanceObject!");
		}
		
		

	}

	private void createMainClass(List<InstanceObject> visitedObjects, IProgressMonitor monitor) {
		if(visitedObjects != null){
			String sourceCode = new MainConverter().generate(visitedObjects);
			Utils.createJavaClass("main", "Main", sourceCode, monitor, rootPackage);
		}
	}

	// based on
	// https://sdqweb.ipd.kit.edu/wiki/JDT_Tutorial:_Creating_Eclipse_Java_Projects_Programmatically
	private void setupJavaProject(IProgressMonitor monitor) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("test" + "-" + "name" + "-rtsj");

		IJavaProject javaProject = null;
		IFolder sourceFolder = null;
		try {
			if (project.exists()) // if this project was generated once -> delete it first
				project.delete(true, monitor);
			project.create(monitor);
			if (!project.isOpen())
				project.open(monitor);
			// set java nature
			IProjectDescription description = project.getDescription();
			description.setNatureIds(new String[] { JavaCore.NATURE_ID });
			project.setDescription(description, monitor);

			// create java project
			javaProject = JavaCore.create(project);

			// set output location of compiler
			IFolder binFolder = project.getFolder("bin");
			if (!binFolder.exists())
				binFolder.create(false, true, monitor);
			javaProject.setOutputLocation(binFolder.getFullPath(), monitor);

			// define classpath entries
			List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
			IClasspathEntry defaultJREContainerEntry = JavaRuntime.getDefaultJREContainerEntry();
			entries.add(defaultJREContainerEntry);

			// add libs to project class path
			javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), monitor);

			// create a source folder
			sourceFolder = project.getFolder("src");
			if (!sourceFolder.exists())
				sourceFolder.create(false, true, monitor);

			// add source folder to classpath entries
			rootPackage = javaProject.getPackageFragmentRoot(sourceFolder);
			IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = JavaCore.newSourceEntry(rootPackage.getPath());
			javaProject.setRawClasspath(newEntries, monitor);

		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}

}