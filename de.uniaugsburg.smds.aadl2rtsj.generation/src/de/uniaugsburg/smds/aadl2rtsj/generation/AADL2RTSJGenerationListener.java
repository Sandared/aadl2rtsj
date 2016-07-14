package de.uniaugsburg.smds.aadl2rtsj.generation;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.acceleo.engine.event.AbstractAcceleoTextGenerationListener;
import org.eclipse.acceleo.engine.event.AcceleoTextGenerationEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

/**
 * This whole class is a workaround in order to create CompilationUnits instead of plain text files during generation.
 * Writing your own GenerationStrategy might be a more elegant way, but I had not enough time to dig into this further
 * @author Thomas Driessen
 *
 */
public class AADL2RTSJGenerationListener extends AbstractAcceleoTextGenerationListener {
	private final static String SOURCE_PKG = "src";
	private IProgressMonitor monitor;
	private IPackageFragmentRoot root;
	private Map<String, String> srcFiles = new HashMap<String, String>();

	public AADL2RTSJGenerationListener(IProgressMonitor monitor, IPackageFragmentRoot root) {
		this.monitor = monitor;
		this.root = root;
	}

	@Override
	public void fileGenerated(AcceleoTextGenerationEvent event) {
		try {
			if(event.getText().endsWith(".java")){
				String sourceCode = new String(Files.readAllBytes(Paths.get(event.getText())));
				srcFiles.put(event.getText(), sourceCode);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean createJavaClass(String packageName, String className, String sourceCode) {
		IPackageFragment fragment = null;
		try {
			// get or create the package
			fragment = root.getPackageFragment(packageName);
			if (!fragment.exists())
				fragment = root.createPackageFragment(packageName, false, monitor);

			ICompilationUnit cu = fragment.getCompilationUnit(className);
			if (!cu.exists())
				cu = fragment.createCompilationUnit(className, sourceCode, false, monitor);

			// create class
			cu.save(monitor, true);
		} catch (JavaModelException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void generationEnd(AcceleoTextGenerationEvent event) {
		// clear src folder
		try {
			Path directory = Paths.get(root.getCorrespondingResource().getLocationURI().getPath());
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}

			});
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		// recreate it as Java files
		for (Map.Entry<String, String> srcFile : srcFiles.entrySet()) {
			String path = srcFile.getKey();
			String packageName = path.substring(path.lastIndexOf(SOURCE_PKG) + SOURCE_PKG.length());
			String className = packageName.substring(packageName.lastIndexOf(File.separator) + File.separator.length());
			packageName = packageName.substring(1, packageName.lastIndexOf(File.separator)).replace(File.separatorChar, '.');
			String sourceCode = srcFile.getValue();
			
			createJavaClass(packageName, className, sourceCode);
		}

		// clear the map
		srcFiles.clear();
	}

	@Override
	public boolean listensToGenerationEnd() {
		return true;
	}

}
