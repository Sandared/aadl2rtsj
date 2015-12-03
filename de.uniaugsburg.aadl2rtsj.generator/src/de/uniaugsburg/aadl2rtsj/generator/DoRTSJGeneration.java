package de.uniaugsburg.aadl2rtsj.generator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.osate.aadl2.Access;
import org.osate.aadl2.Element;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.modelsupport.modeltraversal.AadlProcessingSwitchWithProgress;
import org.osate.ui.actions.AaxlReadOnlyActionAsJob;

import antlr.Utils;

public class DoRTSJGeneration extends AaxlReadOnlyActionAsJob {

	@Override
	protected String getActionName() {
		return "RTSJ Generation";
	}

	@Override
	protected void doAaxlAction(IProgressMonitor monitor, Element root) {
		System.out.println("DoRTSJGeneration.doAaxlAction()");
		
		
		/*
		 * Doesn't make sense to set the number of work units, because the whole
		 * point of this action is count the number of elements. To set the work
		 * units we would effectively have to count everything twice.
		 */
		monitor.beginTask("Generating RTSJ Code", IProgressMonitor.UNKNOWN);

		// Get the system instance (if any)
		SystemInstance si;
		if (root instanceof InstanceObject)
			si = ((InstanceObject) root).getSystemInstance();
		else
			si = null;
		
		AadlProcessingSwitchWithProgress mySwitch = new AadlProcessingSwitchWithProgress(monitor, errManager) {
			
			@Override
			protected void initSwitches() {
				
				instanceSwitch = new AADL2RTSJInstanceSwitch();
			}
		};
		
		// Setup a new Java project
		setupJavaProject(monitor);
		
		
		if (si != null) {
			mySwitch.defaultTraversal(si);
		}
		else {
			System.err.println("Not an InstanceObject!");
		}
		
	}

	private void setupJavaProject(IProgressMonitor monitor) {
		IProject project =
	            ResourcesPlugin.getWorkspace().getRoot()
	                .getProject("test" + "-" + "name" + "-rtsj");

        IJavaProject javaProject = null;
        IFolder sourceFolder = null;
        try {
          if (!project.exists())
            project.create(monitor);
          if (!project.isOpen())
            project.open(monitor);
          IProjectDescription description = project.getDescription();
          description.setNatureIds(new String[] {JavaCore.NATURE_ID});
          project.setDescription(description, monitor);
          javaProject = JavaCore.create(project);
          IFolder binFolder = project.getFolder("bin");
          if (!binFolder.exists())
            binFolder.create(false, true, monitor);
          javaProject.setOutputLocation(binFolder.getFullPath(), monitor);
          List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
          IClasspathEntry defaultJREContainerEntry = JavaRuntime.getDefaultJREContainerEntry();
          entries.add(defaultJREContainerEntry);
          // add libs to project class path
          javaProject
              .setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), monitor);
          sourceFolder = project.getFolder("src");
          if (!sourceFolder.exists())
            sourceFolder.create(false, true, monitor);
          IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(sourceFolder);
          IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
          IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
          System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
          newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
          javaProject.setRawClasspath(newEntries, monitor);
        } catch (CoreException e1) {
          e1.printStackTrace();
        }
		
        
        if (javaProject != null) {
            // main for rtsj build
            try {
              String mainFolderName = "main";
              IFolder mainFolder = sourceFolder.getFolder(mainFolderName);
              if (!mainFolder.exists()) {
                IPackageFragment mainPackageFrag =
                    javaProject.getPackageFragmentRoot(sourceFolder).createPackageFragment(
                        mainFolderName, false, monitor);
          
                
                mainPackageFrag.getResource().refreshLocal(IResource.DEPTH_INFINITE, monitor);
                ICompilationUnit compilationUnit =
                    mainPackageFrag.createCompilationUnit("Thread1.java",buffer.toString(), true, monitor);
                compilationUnit.save(monitor, true);
              }

              // build.xml
//              IFile xmlFile = project.getFile("build.xml");
//              if (xmlFile.exists()) {
//                xmlFile.delete(true, monitor);
//              }
//              StringBuilder stringBuilder = new StringBuilder();
//              stringBuilder.append(Utils.FIRST_BUILD_XML);
//              String string = project.getLocation().makeAbsolute().toString();
//              stringBuilder.append(string);
//              stringBuilder.append(Utils.SECOND_FIRST_BUILD_XML);
//              InputStream xmlSource = new ByteArrayInputStream(stringBuilder.toString().getBytes());
//              xmlFile.create(xmlSource, true, monitor);


              for (MetaInterface interfaceName : interfaces) {
                String name = "rtsj_gen";
                IPackageFragment packageFrag =
                    javaProject.getPackageFragmentRoot(sourceFolder).createPackageFragment(name,
                        false, monitor);
                String sourceCode = new InterfaceGenerator().generate(interfaceName);
                StringBuffer buffer = new StringBuffer();
                buffer.append("package " + packageFrag.getElementName() + ";\n");
                buffer.append("\n");
                buffer.append(sourceCode);
                packageFrag.getResource().refreshLocal(IResource.DEPTH_INFINITE, monitor);
                ICompilationUnit compilationUnit =
                    packageFrag.createCompilationUnit(interfaceName.getName() + ".java",
                        buffer.toString(), true, monitor);
                compilationUnit.save(monitor, true);

                // TODO maybe update the editor AND save?
              }

              for (MetaClass classfaceName : classes) {
                String name = "rtsj_gen";
                IPackageFragment packageFrag =
                    javaProject.getPackageFragmentRoot(sourceFolder).createPackageFragment(name,
                        false, monitor);
                String sourceCode = new ClassGenerator().generate(classfaceName);
                StringBuffer buffer = new StringBuffer();
                buffer.append("package " + packageFrag.getElementName() + ";\n");
                buffer.append("\n");
                buffer.append(sourceCode);
                ICompilationUnit compilationUnit =
                    packageFrag.createCompilationUnit(classfaceName.getName() + ".java",
                        buffer.toString(), true, monitor);
                compilationUnit.save(monitor, true);

                // TODO maybe update the editor AND save?
              }
            } catch (JavaModelException e1) {
              e1.printStackTrace();
            } catch (CoreException e) {
              e.printStackTrace();
            }
          }
	}

}
