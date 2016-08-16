package de.uniaugsburg.smds.aadl2rtsj.generation;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.acceleo.engine.AcceleoEnginePlugin;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.osate.aadl2.Aadl2Factory;
import org.osate.aadl2.Aadl2Package;
import org.osate.aadl2.AadlBoolean;
import org.osate.aadl2.ComponentClassifier;
import org.osate.aadl2.DataType;
import org.osate.aadl2.Element;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.modelsupport.modeltraversal.AadlProcessingSwitchWithProgress;
import org.osate.ui.actions.AaxlReadOnlyActionAsJob;

import de.uniaugsburg.smds.aadl2rtsj.generation.main.Main;
import de.uniaugsburg.smds.aadl2rtsj.generation.services.ComponentClassifierHelper;

//import de.uniaugsburg.smds.aadl2rtsj.converter.MainConverter;
//import de.uniaugsburg.smds.aadl2rtsj.utils.Utils;

public class DoRTSJGeneration extends AaxlReadOnlyActionAsJob {

	public static IPackageFragmentRoot root = null;
	public static IProgressMonitor _monitor = null;
	private static final String PROJECTNAME = "test" + "-" + "name" + "-rtsj";

	@Override
	protected String getActionName() {
		return "RTSJ Generation";
	}

	@Override
	protected void doAaxlAction(IProgressMonitor monitor, Element rootElement) {
		System.out.println("DoRTSJGeneration.doAaxlAction()");
		_monitor = monitor;
		
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
			
			// create instance switch to collect all used classifiers
			AADL2RTSJInstanceSwitch myInstanceSwitch = new AADL2RTSJInstanceSwitch();
			AadlProcessingSwitchWithProgress mySwitch = new AadlProcessingSwitchWithProgress(monitor, errManager) {

				@Override
				protected void initSwitches() {
					instanceSwitch = myInstanceSwitch;
				}
			};
			// Resolve all proxies
			EcoreUtil.resolveAll(si);
			//collect all used classifiers
			mySwitch.defaultTraversal(si);
			
			// Use Acceleo to generate code
			try {
				File srcFolder = new File(root.getCorrespondingResource().getLocationURI());
				//retrieve the list of all used classifiers and give it to the generation
				Set<ComponentClassifier> classifier = myInstanceSwitch.getUsedClassifer();
				List<Object> arguments = new ArrayList<Object>();
				arguments.add(classifier);
				Main main = new Main(si, srcFolder, arguments);
				main.addPropertiesFile("platform:/plugin/de.uniaugsburg.smds.aadl2rtsj.generation/de/uniaugsburg/smds/aadl2rtsj/generation/main/aadl2rtsj.properties");
				main.doGenerate(BasicMonitor.toMonitor(monitor));
				importProject(PROJECTNAME);
				formatProjectCode(PROJECTNAME);
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
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECTNAME);

		IJavaProject javaProject = null;
		IFolder sourceFolder = null;
		try {
			if (project.exists()) {
				if (!project.isOpen())
					project.open(monitor);
				sourceFolder = project.getFolder("src");
				root = JavaCore.create(project).getPackageFragmentRoot(sourceFolder);
				return; // project exists and we merely regenerate its contents
			}
			else{
				project.create(monitor);
				if (!project.isOpen())
					project.open(monitor);
			}
			
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
	
	// following code is taken from Obeo's UML 2 Java code generator
	// https://github.com/ObeoNetwork/UML-Java-Generation/blob/master/plugins/org.obeonetwork.pim.uml2.gen.java/src/org/obeonetwork/pim/uml2/gen/java/services/WorkspaceServices.java
	
	/**
	 * Imports a new project, created outside of the workspace, to the workspace.
	 * 
	 * @param projectName
	 *            The name of the project.
	 */
	public void importProject(String projectName) {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			return;
		}

		System.getProperty("line.separator");

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		try {
			IWorkspaceRoot workspaceRoot = workspace.getRoot();
			IProjectDescription descr = workspace.loadProjectDescription(workspaceRoot.getLocation().append(
					projectName).append(".project"));
			IProject project = workspaceRoot.getProject(projectName);
			if (project.exists()) {
				if (!project.isOpen()) {
					project.open(new NullProgressMonitor());
				}
			} else {
				project.create(descr, new NullProgressMonitor());
				project.open(new NullProgressMonitor());
			}
		} catch (CoreException ce) {
			AcceleoEnginePlugin.log(ce, true);
		}
	}
	
	public void formatProjectCode(String projectName) {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			return;
		}

		try {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			Map<String, String> options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();

			// initialize the compiler settings to be able to format 1.5 code
			options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
			options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
			options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);

			// change the option to wrap each enum constant on a new line
			options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ENUM_CONSTANTS,
					DefaultCodeFormatterConstants.createAlignmentValue(true,
							DefaultCodeFormatterConstants.WRAP_ONE_PER_LINE,
							DefaultCodeFormatterConstants.INDENT_ON_COLUMN));

			// instantiate the default code formatter with the given options
			final CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options);

			project.accept(new IResourceVisitor() {

				public boolean visit(IResource resource) throws CoreException {
					if (resource.isAccessible() && resource instanceof IFile
							&& "java".equals(((IFile)resource).getFileExtension())) {
						IFile iFile = (IFile)resource;
						ICompilationUnit compilationUnit = JavaCore.createCompilationUnitFrom(iFile);
						String contents = compilationUnit.getBuffer().getContents();
						final TextEdit edit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT,
								contents, // source to format
								0, // starting position
								contents.length(), // length
								0, // initial indentation
								System.getProperty("line.separator") // line separator
								);

						IDocument document = new Document(contents);
						try {
							if (edit != null) {
								edit.apply(document);
							}
						} catch (MalformedTreeException e) {
							e.printStackTrace();
						} catch (BadLocationException e) {
							e.printStackTrace();
						}

						iFile.setContents(new ByteArrayInputStream(document.get().getBytes()),
								IResource.FORCE, new NullProgressMonitor());
						return true;
					}
					return true;
				}
			});

			project.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}