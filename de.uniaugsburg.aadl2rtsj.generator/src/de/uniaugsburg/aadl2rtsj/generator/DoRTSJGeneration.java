package de.uniaugsburg.aadl2rtsj.generator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.osate.aadl2.Element;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.modelsupport.modeltraversal.AadlProcessingSwitchWithProgress;
import org.osate.ui.actions.AaxlReadOnlyActionAsJob;

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
		
		
		if (si != null) {
			mySwitch.defaultTraversal(si);
		}
		else {
			System.err.println("Not an InstanceObject!");
		}
		
	}

}
