package de.uniaugsburg.smds.aadl2rtsj.utils;

public class OffsetTime {
	
	private long ms;
	private long ns;
	private int uniqueID;
	private String ioTime;
	
	public long getMs() {
		return ms;
	}

	public long getNs() {
		return ns;
	}
	
	public int getUniqueId(){
		return uniqueID;
	}
	
	public OffsetTime(long ms, long ns, int uniqueId, String ioTime){
		this.ms = ms;
		this.ns = ns;
		this.uniqueID = uniqueId;
		this.ioTime = ioTime;
	}
	
	public OffsetTime(){}

	public String getIOTime() {
		return ioTime;
	}

}
