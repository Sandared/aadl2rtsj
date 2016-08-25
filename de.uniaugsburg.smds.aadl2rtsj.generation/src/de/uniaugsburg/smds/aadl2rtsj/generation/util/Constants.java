package de.uniaugsburg.smds.aadl2rtsj.generation.util;

public class Constants {
	
	public static final String AADL_Project_Time_Units_Pico_Seconds = "ps";
	public static final String AADL_Project_Time_Units_Nano_Seconds = "ns";
	public static final String AADL_Project_Time_Units_Micro_Seconds = "us";
	public static final String AADL_Project_Time_Units_Milli_Seconds = "ms";
	public static final String AADL_Project_Time_Units_Seconds = "sec";
	public static final String AADL_Project_Time_Units_Minutes = "min";
	public static final String AADL_Project_Time_Units_Hour = "hr";
	
	
	public static final String Communication_Properties = "Communication_Properties";
	
	public static final String Communication_Properties_Timing = "Timing";
	public static final String Communication_Properties_Timing_Delayed = "delayed";
	public static final String Communication_Properties_Timing_Sampled = "sampled";
	public static final String Communication_Properties_Timing_Immediate = "immediate";
	
	public static final String Communication_Properties_Input_Time = "Input_Time";
	public static final String Communication_Properties_Output_Time = "Output_Time";
	
	public static final String Communication_Properties_IO_Reference_Time_Dispatch = "Dispatch";
	public static final String Communication_Properties_IO_Reference_Time_NoIO = "NoIO";
	public static final String Communication_Properties_IO_Reference_Time_Start = "Start";
	public static final String Communication_Properties_IO_Reference_Time_Deadline = "Deadline";
	public static final String Communication_Properties_IO_Reference_Time_Completion = "Completion";
	
	public static final String Communication_Properties_Transmission_Type = "Transmission_Type";
	public static final String Communication_Properties_Transmission_Type_Push = "push";
	public static final String Communication_Properties_Transmission_Type_Pull = "pull";
	
	
	public static final String Thread_Properties = "Thread_Properties";
	
	public static final String Thread_Properties_Priority = "Priority";
	
	public static final String Thread_Properties_Dispatch_Protocol = "Dispatch_Protocol";
	public static final String Thread_Properties_Dispatch_Protocol_Timed = "Timed";
	public static final String Thread_Properties_Dispatch_Protocol_Hybrid = "Hybrid";
	public static final String Thread_Properties_Dispatch_Protocol_Periodic = "Periodic";
	public static final String Thread_Properties_Dispatch_Protocol_Sporadic = "Sporadic";
	public static final String Thread_Properties_Dispatch_Protocol_Aperiodic = "Aperiodic";
	public static final String Thread_Properties_Dispatch_Protocol_Background = "Background";
	
	public static final String Memory_Properties = "Memory_Properties";
	public static final String Memory_Properties_Access_Right = "Access_Right";
	public static final String Memory_Properties_Access_Right_Read_Write = "read_write";
	public static final String Memory_Properties_Access_Right_Read_Only = "read_only";
	public static final String Memory_Properties_Access_Right_Write_Only = "write_only";
	
	public static final String Timing_Properties = "Timing_Properties";
	
	public static final String Timing_Properties_Period = "Period";
	
	
	//the following are my own constants, none of them are in the standard
	public static final String ConnectionType_Thread_To_Thread = "t2t";
	public static final String ConnectionType_Thread_To_Non_Thread = "t2nt";
	public static final String ConnectionType_Non_Thread_To_Thread = "nt2t";
	public static final String ConnectionType_Non_Thread_To_Non_Thread = "nt2nt";

}
