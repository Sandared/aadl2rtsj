package missionparser;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author Manuel Hoss (manuel.hoss@hotmail.de)
 */
public class Mission {

	private Queue<MissionCommand> missionCommands;
	private MissionParser missionParser;

	/**
	 * Mission has a Queue of MissionCommands and a MissionParser which extracts
	 * the MissionCommands out of a text file
	 * 
	 * @param missionDataPath
	 *            Path to text file with way point- and loiter commands
	 */
	public Mission(String missionDataPath) {
		missionCommands = new LinkedList<MissionCommand>();
		missionParser = new MissionParser(missionDataPath);
		missionCommands = missionParser.parseMissionCommands();
	}

	public boolean isMissionCommandsEmpty() {
		return missionCommands.isEmpty();
	}

	/**
	 * Returns the next MissionCommand in the MissionCommands Queue
	 * 
	 * @return Next {@link MissionCommand}
	 */
	public MissionCommand getNextMissionCommand() {
		// Retrieves and removes first Element of the List
		return missionCommands.poll();
	}
}