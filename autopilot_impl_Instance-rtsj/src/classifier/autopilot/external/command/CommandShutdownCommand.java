package classifier.autopilot.external.command;

//########## Classifier Imports ##########
import classifier.autopilot.external.command.Command;
import classifier.de.uniaugsburg.smds.aadl2rtsj.generation.basetypes.external.booleandata.BooleanData;

//########## Realized Classifier Import ##########
import classifier.autopilot.external.command.Command;

public class CommandShutdownCommand implements Command {

	//########### Subcomponents ###########
	/**
	 * A member variable to store the value for subcomponent "shutdown"
	 */
	private BooleanData shutdown = new BooleanData();

	//########### Getter/Setter ###########
	/**
	 * @return the value of shutdown
	 */
	public BooleanData getShutdown() {
		return shutdown;
	}

	/**
	 * @param shutdown the new value for shutdown
	 */
	public void setShutdown(BooleanData shutdown) {
		this.shutdown = shutdown;
	}

	@Override
	public CommandShutdownCommand deepCopy() {
		CommandShutdownCommand result = new CommandShutdownCommand();
		result.shutdown = shutdown.deepCopy();
		return result;
	}
}
