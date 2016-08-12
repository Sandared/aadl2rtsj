package de.uniaugsburg.smds.aadl2rtsj.generation;

import java.util.Comparator;
import java.util.logging.Logger;

import org.osate.aadl2.Classifier;

public class ClassifierComparator implements Comparator<Classifier> {
	Logger log = Logger.getLogger(ClassifierComparator.class.getName());

	@Override
	public int compare(Classifier o1, Classifier o2) {
		try {
			String fullNameO1 = o1.getNamespace().getFullName() + "." + o1.getName();
			String fullNameO2 = o2.getNamespace().getFullName() + "." + o2.getName();
			return fullNameO1.compareTo(fullNameO2);
		} catch (Exception e) {
			log.severe("An Exception occurred while comparing the classifiers " + o1 + " and " + o2 + "!\nAs Default '-1' was returned!");
			e.printStackTrace();
		}
		return -1;
	}

}
