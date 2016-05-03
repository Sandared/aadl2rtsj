package de.uniaugsburg.smds.aadl2rtsj.generation.utils;

import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.FeatureInstance;

import de.uniaugsburg.smds.aadl2rtsj.generation.services.common.CommonHelper;
import util.OffsetTime;

public class SimpleStatements {

	public static String receiveInputStatement(FeatureInstance fi){
		return CommonHelper.getObjectName(fi) + ".receiveInput();";
	}
	
	public static String sendOutputStatement(FeatureInstance fi, ConnectionInstance ci){
		return CommonHelper.getObjectName(fi) + ".sendOutputOn" + CommonHelper.getClassName(CommonHelper.getConnection(ci)) + "();";
	}
	
	public static String iOViaHandlerStatement(FeatureInstance fi, OffsetTime time, boolean atDeadline){
		StringBuilder sb = new StringBuilder();
		sb.append("Timer timerFor")
		.append(getHandlerClassName(fi, time))
		.append(" = new OneShotTimer(");
		if(!atDeadline){
			sb.append("new RelativeTime(")
			.append(time.getMs())
			.append(", ")
			.append(time.getNs())
			.append(")");
		}
		else{
			sb.append("timer.getFireTime()");
		}
		sb.append(", new ")
		.append(getHandlerClassName(fi, time))
		.append("(")
		.append(CommonHelper.getObjectName(fi))
		.append("));")
		.append("\n")
		.append("timerFor")
		.append(getHandlerClassName(fi, time))
		.append(".start();");
		return sb.toString();
	}
	
	private static String getHandlerClassName(FeatureInstance feature, OffsetTime time){
		return CommonHelper.getClassName(feature) + "Handler_" + time.getUniqueId();
	}
}
