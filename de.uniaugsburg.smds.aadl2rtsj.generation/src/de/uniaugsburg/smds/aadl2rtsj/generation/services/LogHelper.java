package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import java.util.logging.Logger;

public class LogHelper {
	
	private static final Logger log = Logger.getLogger(LogHelper.class.getName());
	
	public static void info(String msg){
		log.info(msg);
	}
	
	public static void warning(String msg){
		log.warning(msg);
	}
	
	public static void severe(String msg){
		log.severe(msg);
	}

}
