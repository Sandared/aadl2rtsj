package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import java.util.logging.Logger;

public class LogHelper {
	
	private static final Logger log = Logger.getLogger(LogHelper.class.getName());
	
	/**
	 * @param msg the message to log as INFO
	 */
	public static void info(String msg){
		log.info(msg);
	}
	
	/**
	 * @param msg the message to log as WARNING
	 */
	public static void warning(String msg){
		log.warning(msg);
	}
	
	/**
	 * @param msg the message to log as SEVERE
	 */
	public static void severe(String msg){
		log.severe(msg);
	}

}
