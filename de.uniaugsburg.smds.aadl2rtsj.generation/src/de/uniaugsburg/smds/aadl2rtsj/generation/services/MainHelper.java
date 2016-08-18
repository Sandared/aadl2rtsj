package de.uniaugsburg.smds.aadl2rtsj.generation.services;

import java.util.ArrayList;
import java.util.List;

import org.osate.aadl2.Element;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.SystemInstance;

import com.google.common.collect.Lists;

public class MainHelper {
	
	public static List<ComponentInstance> getReverseOrderedBFSSystemTree(SystemInstance system){
		List<ComponentInstance> result = new ArrayList<ComponentInstance>();
		result.add(system);
		int position = 0;
		while (position < result.size()){
			ComponentInstance current = result.get(position);
			for (Element child : current.getChildren()) {
				if (child instanceof ComponentInstance) {
					result.add((ComponentInstance)child);
				}
			}
			position++;
		}
		return Lists.reverse(result);
	}
	
	public static String getUniqueId(ComponentInstance ci){
		return ""+System.identityHashCode(ci);
	}
}
