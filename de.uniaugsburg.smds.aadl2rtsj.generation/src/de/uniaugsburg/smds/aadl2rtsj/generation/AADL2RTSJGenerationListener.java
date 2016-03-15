package de.uniaugsburg.smds.aadl2rtsj.generation;

import java.io.File;

import org.eclipse.acceleo.engine.event.AcceleoTextGenerationEvent;
import org.eclipse.acceleo.engine.event.IAcceleoTextGenerationListener;
import org.eclipse.acceleo.model.mtl.FileBlock;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

public class AADL2RTSJGenerationListener implements IAcceleoTextGenerationListener {
	private final static String SOURCE_PKG = "src";
	private StringBuilder sourceCode = new StringBuilder();

	@Override
	public void fileGenerated(AcceleoTextGenerationEvent event) {

	}

	@Override
	public void filePathComputed(AcceleoTextGenerationEvent event) {
//		String path = event.getText();
//		int srcPos = path.indexOf(SOURCE_PKG + File.separator);
//		String srcPath = path.substring(srcPos + SOURCE_PKG.length() + File.separator.length());
//		int classPos = srcPath.lastIndexOf(File.separator);
//		String className = srcPath.substring(classPos + File.separator.length());
//		String packageName = srcPath.substring(0, classPos).replace(File.separator, ".");
//		
//		FileBlock fb = (FileBlock) event.getBlock();
//		String test = fb.toString();
//		createJavaClass(packageName, className, sourceCode.toString(), DoRTSJGeneration._monitor, DoRTSJGeneration._rootPackage);
//		sourceCode = new StringBuilder();
	}

	@Override
	public void generationEnd(AcceleoTextGenerationEvent arg0) {
		// nothing to do here
	}

	@Override
	public boolean listensToGenerationEnd() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void textGenerated(AcceleoTextGenerationEvent arg0) {
//		sourceCode.append(arg0.getText());
	}
	
	private boolean createJavaClass(String packageName, String className, String sourceCode, IProgressMonitor monitor, IPackageFragmentRoot root) {
		IPackageFragment fragment = null;
		try {
			// get or create the package
			fragment = root.getPackageFragment(packageName);
			if(!fragment.exists())
				fragment = root.createPackageFragment(packageName, false, monitor);
			
			ICompilationUnit cu = fragment.getCompilationUnit(className);
			if(!cu.exists())
				cu = fragment.createCompilationUnit(className, sourceCode, false, monitor);
			
			// create class
			cu.save(monitor, true);
		} catch (JavaModelException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
