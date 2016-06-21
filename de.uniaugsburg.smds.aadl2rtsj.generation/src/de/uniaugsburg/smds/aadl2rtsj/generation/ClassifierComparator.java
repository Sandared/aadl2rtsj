package de.uniaugsburg.smds.aadl2rtsj.generation;

import java.util.Comparator;

import org.osate.aadl2.Classifier;

public class ClassifierComparator implements Comparator<Classifier> {

	@Override
	public int compare(Classifier o1, Classifier o2) {
		try {
		
			String fullNameO1 = o1.getNamespace().getFullName() + "." + o1.getName();
			String fullNameO2 = o2.getNamespace().getFullName() + "." + o2.getName();
			return fullNameO1.compareTo(fullNameO2);
		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ClassifierComparator.compare()");
		}
		
		return -1;
	}

}
