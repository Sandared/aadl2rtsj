package classifier.autopilot.external.autopilot;

//########## Classifier Imports ##########
import classifier.autopilot.external.autopilot.Autopilot;
import classifier.autopilot.external.autopilotprocess.autopilotProcessimpl;
import classifier.autopilot.external.x_plane.x_planeimpl;

//########## Port Variable Import ##########

/**
 * This class represents the model element autopilot.impl.</br>
 * For each defined subcomponent it defines a member variable, which is visible to extending classes.</br>
 * For each DATA PORT that has no connections defined within autopilot.impl, a member variable is defined, that can store incoming or outgoing values</br>
 * For each IN DATA PORT a method is defined, that handles the incoming data.
 * 
 * @author thomas.driessen@informatik.uni-augsburg.de
 */
public abstract class autopilotimpl implements Autopilot {

	//########## Subcomponent Variables ##########
	/**
	 * A member variable that is used to store the subcomponent <code>simulation</code>
	 */
	protected x_planeimpl simulation;

	/**
	 * A member variable that is used to store the subcomponent <code>autopilot</code>
	 */
	protected autopilotProcessimpl autopilot;

	//########## Data Port Variables ###########

	//########## Inherited Methods from Autopilot ###########
}
