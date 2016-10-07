package main;

// Start of user code main user imports
import bus.StatusBus;
import bus.MyBusListener;
import java.io.IOException;
import common.Configuration;
import xplane.XplaneUdpSender;
import xplane.XplaneUdpListener;
import xplane.parser.XPlaneUdpParserImpl;
import xplane.parser.factories.LatLonAltitudePacketFactory;
import xplane.parser.factories.AngularOrientationPacketFactory;
// End of user code

public class Main {

	public static void main(String[] args) {

		// Start of user code main user code start
		StatusBus bus = new StatusBus();

		Configuration config = new Configuration();

		LatLonAltitudePacketFactory latLonAltFactory = new LatLonAltitudePacketFactory();
		AngularOrientationPacketFactory angularFactory = new AngularOrientationPacketFactory();

		XPlaneUdpParserImpl parser = new XPlaneUdpParserImpl();
		parser.registerPacketFactory(latLonAltFactory);
		parser.registerPacketFactory(angularFactory);

		XplaneUdpListener listener = new XplaneUdpListener();
		listener.setConfiguration(config);
		listener.setParser(parser);
		listener.setStatusBus(bus);

		XplaneUdpSender sender = new XplaneUdpSender();
		sender.setConfiguration(config);

		bus.registerListener(sender);

		// End of user code

		//create components
		instance.autopilot_impl.autopilot.datalogger.dataLogger dataLogger_ID951044074 = new instance.autopilot_impl.autopilot.datalogger.dataLogger();
		instance.autopilot_impl.autopilot.headingcontrol.headingControl headingControl_ID976793710 = new instance.autopilot_impl.autopilot.headingcontrol.headingControl();
		instance.autopilot_impl.autopilot.altitudecontrol.altitudeControl altitudeControl_ID804771714 = new instance.autopilot_impl.autopilot.altitudecontrol.altitudeControl();
		instance.autopilot_impl.autopilot.positioncontrol.positionControl positionControl_ID415771835 = new instance.autopilot_impl.autopilot.positioncontrol.positionControl();
		instance.autopilot_impl.autopilot.flightmissionexecutioncontrol.flightMissionExecutionControl flightMissionExecutionControl_ID290740236 = new instance.autopilot_impl.autopilot.flightmissionexecutioncontrol.flightMissionExecutionControl();
		instance.autopilot_impl.autopilot.pitchcontrol.pitchControl pitchControl_ID1566957559 = new instance.autopilot_impl.autopilot.pitchcontrol.pitchControl();
		instance.autopilot_impl.autopilot.mixthrottlescontrol.mixThrottlesControl mixThrottlesControl_ID626839565 = new instance.autopilot_impl.autopilot.mixthrottlescontrol.mixThrottlesControl();
		instance.autopilot_impl.autopilot.rollcontrol.rollControl rollControl_ID459000148 = new instance.autopilot_impl.autopilot.rollcontrol.rollControl();
		instance.autopilot_impl.autopilot.autopilot autopilot_ID708518663 = new instance.autopilot_impl.autopilot.autopilot(
				rollControl_ID459000148, mixThrottlesControl_ID626839565, pitchControl_ID1566957559,
				flightMissionExecutionControl_ID290740236, positionControl_ID415771835, altitudeControl_ID804771714,
				headingControl_ID976793710, dataLogger_ID951044074);
		instance.autopilot_impl.simulation.simulation simulation_ID1135732302 = new instance.autopilot_impl.simulation.simulation();
		instance.autopilot_impl.autopilot_impl_Instance autopilot_impl_Instance_ID341767595 = new instance.autopilot_impl.autopilot_impl_Instance(
				simulation_ID1135732302, autopilot_ID708518663);

		// Start of user code main user code mid
		MyBusListener myListener = new MyBusListener(bus);
		bus.registerListener(myListener);

		//start connection to xplane before starting autopilot
		try {
			sender.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		listener.init();
		bus.init();
		// End of user code

		//init brokers
		autopilot_impl_Instance_ID341767595.setParentConnectionBroker(null);

		dataLogger_ID951044074.startExecution();
		headingControl_ID976793710.startExecution();
		altitudeControl_ID804771714.startExecution();
		positionControl_ID415771835.startExecution();
		flightMissionExecutionControl_ID290740236.startExecution();
		pitchControl_ID1566957559.startExecution();
		mixThrottlesControl_ID626839565.startExecution();
		rollControl_ID459000148.startExecution();

		// Start of user code main user code end
		//maybe needs to be adapted to each generation because of id
		simulation_ID1135732302.start(bus, myListener);
		// End of user code
	}
}
