package de.uniaugsburg.smds.aadl2rtsj.utils;

import org.osate.aadl2.instance.ConnectionInstance;

public class OffsetTime {
	
	private long ms;
	private long ns;
	private int uniqueID;
	private String ioTime;
	private ConnectionInstance connection;
	
	public long getMs() {
		return ms;
	}

	public long getNs() {
		return ns;
	}
	
	public int getUniqueId(){
		return uniqueID;
	}
	
	public ConnectionInstance getConnection(){
		return connection;
	}
	
	public OffsetTime(long ms, long ns, int uniqueId, String ioTime, ConnectionInstance connection){
		this.ms = ms;
		this.ns = ns;
		this.uniqueID = uniqueId;
		this.ioTime = ioTime;
		this.connection = connection;
	}
	
	public OffsetTime(){}

	public String getIOTime() {
		return ioTime;
	}

}
