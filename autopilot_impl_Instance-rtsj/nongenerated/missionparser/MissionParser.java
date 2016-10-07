package missionparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * 
 * @author Manuel Hoss (manuel.hoss@hotmail.de)
 */
public class MissionParser {

	private static final Logger log = Logger.getLogger(MissionParser.class.getName());

	private String fileName = "MissionInfo.waypoints";
	private String filePath = "missioninfo" + File.separator + fileName;

	/**
	 * Creates parser object
	 * 
	 * @param fileName
	 *            fileName of the text file which has the way point- and loiter
	 *            command information
	 */
	public MissionParser(String fileName) {
		this.fileName = fileName;
		this.filePath = "missioninfo" + File.separator + fileName;
	}

	/**
	 * Reads the text file (which can be created with Mission Planner) and
	 * parses each line to a {@link MissionCommand}
	 * 
	 * @return Queue of {@link MissionCommand} in the Order they should be
	 *         executed
	 */
	public Queue<MissionCommand> parseMissionCommands() {
		Queue<MissionCommand> missionCommands = new LinkedList<MissionCommand>();
		List<String> commandLines = new ArrayList<String>();

		// Read file into List of Strings
		try {
			commandLines = readLines();
		} catch (IOException e) {
			log.severe(e.toString());
		}

		// Parse out the MissionCommand Objects out of the Strings
		for (int i = 0; i < commandLines.size(); i++) {
			MissionCommand missionCommand = parseMissionCommand(commandLines.get(i));
			missionCommands.add(missionCommand);
		}

		return missionCommands;
	}

	/**
	 * Reads lines out of the MissionDescription file into List of Strings Each
	 * line is one {@link MissionCommand} - Order is important!
	 * 
	 * @return List of Strings, each string is one {@link MissionCommand}
	 * @throws IOException
	 */
	public List<String> readLines() throws IOException {
		FileReader fileReader = new FileReader(filePath);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		int i = 0;
		while ((line = bufferedReader.readLine()) != null) {
			// First two lines of the file are not needed
			if (i > 1) {
				lines.add(line);
			}
			i++;
		}
		bufferedReader.close();
		return lines;
	}

	/**
	 * Gets a String and parses a {@link MissionCommand} object out of it
	 * 
	 * @param missionCommandString
	 *            String with all information about one {@link MissionCommand}
	 * @return {@link MissionCommand} parsed out of the string
	 */
	public MissionCommand parseMissionCommand(String missionCommandString) {

		// Get single parameters seperated with tabs
		String[] parts = missionCommandString.split("\t");
		MissionCommand missionCommand = new MissionCommand();

		missionCommand.setWayPointid(Integer.valueOf(parts[0]));
		if (Integer.valueOf(parts[3]) == 19) {
			missionCommand.setCommandType(MissionCommandType.MAV_CMD_NAV_LOITER_TIME);
		} else if (Integer.valueOf(parts[3]) == 16) {
			missionCommand.setCommandType(MissionCommandType.MAV_CMD_NAV_WAYPOINT);
		} else if (Integer.valueOf(parts[3]) == 42) {
			missionCommand.setCommandType(MissionCommandType.MAV_CMD_NAV_SKIP);
		}
		missionCommand.setSpecificParameter1(Float.valueOf(parts[4]));
		missionCommand.setSpecificParameter2(Float.valueOf(parts[5]));
		missionCommand.setSpecificParameter3(Float.valueOf(parts[6]));
		missionCommand.setSpecificParameter4(Float.valueOf(parts[7]));
		missionCommand.setLatitude(Float.valueOf(parts[8]));
		missionCommand.setLongitude(Float.valueOf(parts[9]));
		missionCommand.setAltitude(Float.valueOf(parts[10]));

		return missionCommand;
	}
}
