package bus;

import bus.api.Bus;
import bus.api.BusListener;
import bus.api.BusMessage;
import bus.api.PositionInformation;
import bus.api.ThrottleCommand;
import classifier.autopilot.external.command.CommandThrottleCommand;
import classifier.autopilot.external.information.InformationPositionInformation;

public class MyBusListener implements BusListener {

	private Bus bus;
	private volatile InformationPositionInformation currentPosInfo;

	public MyBusListener(Bus bus) {
		this.bus = bus;
	}

	@Override
	public void processMessage(BusMessage message) {
		switch (message.getType()) {
		case Position:
			//conversion
			PositionInformation posInfo = (PositionInformation) message;
			InformationPositionInformation modelPosInfo = new InformationPositionInformation();
			modelPosInfo.getHeading().setValue(posInfo.getHeading());
			modelPosInfo.getLocation().getAltitudeMsl().setValue((float) posInfo.getLocation().getAltitudeMsl());
			modelPosInfo.getLocation().getLatitude().setValue((float) posInfo.getLocation().getLatitude());
			modelPosInfo.getLocation().getLongitude().setValue((float) posInfo.getLocation().getLongitude());
			//			modelPosInfo.getLoiter().setValue()// TODO;
			modelPosInfo.getPitch().setValue(posInfo.getPitch());
			modelPosInfo.getRoll().setValue(posInfo.getRoll());

			// store information for later use
			currentPosInfo = modelPosInfo;
			break;
		default:

			break;
		}
	}

	public InformationPositionInformation getPositionInformation() {
		return currentPosInfo;
	}

	public void broadcastThrottle(CommandThrottleCommand throttle) {
		// conversion
		ThrottleCommand cmd1 = new ThrottleCommand(0, throttle.getThrottleEngine1().getValue());
		ThrottleCommand cmd2 = new ThrottleCommand(1, throttle.getThrottleEngine2().getValue());
		ThrottleCommand cmd3 = new ThrottleCommand(2, throttle.getThrottleEngine3().getValue());
		ThrottleCommand cmd4 = new ThrottleCommand(3, throttle.getThrottleEngine4().getValue());

		//broadcast
		bus.broadcast(cmd1);
		bus.broadcast(cmd2);
		bus.broadcast(cmd3);
		bus.broadcast(cmd4);
	}

}
