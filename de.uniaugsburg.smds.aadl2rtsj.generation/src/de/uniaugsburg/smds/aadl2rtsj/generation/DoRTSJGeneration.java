package de.uniaugsburg.smds.aadl2rtsj.generation;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.osate.aadl2.Element;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.ui.actions.AaxlReadOnlyActionAsJob;

import de.uniaugsburg.smds.aadl2rtsj.generation.main.Main;

//import de.uniaugsburg.smds.aadl2rtsj.converter.MainConverter;
//import de.uniaugsburg.smds.aadl2rtsj.utils.Utils;

public class DoRTSJGeneration extends AaxlReadOnlyActionAsJob {

	public static IPackageFragmentRoot root = null;
	public static IProgressMonitor _monitor = null;

	@Override
	protected String getActionName() {
		return "RTSJ Generation";
	}

	@Override
	protected void doAaxlAction(IProgressMonitor monitor, Element rootElement) {
		System.out.println("DoRTSJGeneration.doAaxlAction()");
		_monitor = monitor;

		monitor.beginTask("Generating RTSJ Code", IProgressMonitor.UNKNOWN);

		// Get the system instance (if any)
		SystemInstance si;
		if (rootElement instanceof InstanceObject)
			si = ((InstanceObject) rootElement).getSystemInstance();
		else
			si = null;

		// Setup a new Java project
		setupJavaProject(monitor);

		// run traversal which does the actual generation
		if (si != null) {
			
			// Use Acceleo instead of Osate Switches and JET
			try {
				File srcFolder = new File(root.getCorrespondingResource().getLocationURI());
				Main main = new Main(si, srcFolder, new ArrayList<Object>());
				
				main.addGenerationListener(new AADL2RTSJGenerationListener(monitor, root));
				main.doGenerate(BasicMonitor.toMonitor(monitor));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.err.println("Not an InstanceObject!");
		}
		// TODO error log if no si was found
	}

//	private void createMainClass(List<InstanceObject> visitedObjects, IProgressMonitor monitor) {
//		if(visitedObjects != null){
////			String sourceCode = new MainConverter().generate(visitedObjects);
////			Utils.createJavaClass("main", "Main", sourceCode, monitor, rootPackage);
//		}
//	}

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
			
			//search for RTSJ
			IVMInstallType jamaicaVM = JavaRuntime.getVMInstallType("com.aicas.jamaica.eclipse.launcher.vm.JamaicaVMType");
			if(jamaicaVM != null){
				// found the JamaicaVM
				IPath containerPath = new Path(JavaRuntime.JRE_CONTAINER);
				IVMInstall[] vmInstalls = jamaicaVM.getVMInstalls();
				String vmname = (vmInstalls.length > 0)? vmInstalls[0].getName() : jamaicaVM.getName(); // jamaicaVM.getName() returns s.th. like "JAMAICA_VM" but should be s.th. like "JAMAICA_VM-6"
				IPath vmPath = containerPath.append(jamaicaVM.getId()).append(vmname);
				// add classpathentry
				entries.add(JavaCore.newContainerEntry(vmPath));
			}
			else{
				// if no JamaicaVM was found just use the default JRE ... there will be a lot of compilation errors due to the missing rt libraries
				IClasspathEntry defaultJREContainerEntry = JavaRuntime.getDefaultJREContainerEntry(); 
				// add classpathentry
				entries.add(defaultJREContainerEntry);
			}

			// add libs to project class path
			javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), monitor);

			// create a source folder
			sourceFolder = project.getFolder("src");
			if (!sourceFolder.exists())
				sourceFolder.create(false, true, monitor);

			// add source folder to classpath entries
			root = javaProject.getPackageFragmentRoot(sourceFolder);
			IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
			javaProject.setRawClasspath(newEntries, monitor);

		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}

}