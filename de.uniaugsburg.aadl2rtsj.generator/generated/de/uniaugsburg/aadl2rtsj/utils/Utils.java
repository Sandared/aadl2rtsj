package de.uniaugsburg.aadl2rtsj.utils;

public class Utils {
	
	/**
	 * Turns strings into a form that is expected in Java to be a class name, e.g. first letter upper cased
	 * @param name the (probably lowercased) String 
	 * @return a string that looks like a class name
	 */
	public static String className(String name){
		StringBuilder b = new StringBuilder(name);
		b.replace(0, 1, b.substring(0,1).toUpperCase());
		return b.toString();
	}

}
