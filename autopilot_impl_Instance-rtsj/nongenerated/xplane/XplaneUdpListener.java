package xplane;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.realtime.AsyncEventHandler;
import javax.realtime.PeriodicTimer;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;

import bus.api.Bus;
import bus.api.GeoCoordinate;
import bus.api.PositionInformation;
import common.Configuration;
import xplane.parser.XPlanePacketConsumer;
import xplane.parser.XPlaneUdpParser;
import xplane.parser.exceptions.ParseException;
import xplane.parser.packets.AngularOrientationPacket;
import xplane.parser.packets.LatLonAltitudePacket;

/**
 * Implements a data receiver for the X-Plane UDP data output format.
 * <p/>
 * The received data is parsed and processed for output on the internal data
 * bus.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class XplaneUdpListener implements XPlanePacketConsumer {
	private static final Logger log = Logger.getLogger(XplaneUdpListener.class.getName());

	private Bus bus;
	private Configuration config;
	private XPlaneUdpParser parser;

	private boolean pos1Ready = false;
	private boolean pos2Ready = false;
	private Object monitor = new Object();

	// Local settings
	private final PositionInformation position = new PositionInformation();

	private int busUpdatePeriod = 100;
	private RealtimeThread realTimeWorker;
	private PeriodicTimer realTimeTimer;

	public void setStatusBus(Bus bus) {
		this.bus = bus;
	}

	public void setConfiguration(Configuration config) {
		this.config = config;
	}

	public void setParser(XPlaneUdpParser parser) {
		this.parser = parser;
		parser.bindConsumer(this);
	}

	@Override
	public void onPacket(LatLonAltitudePacket packet) {
		position.setLocation(new GeoCoordinate(packet.getLatitude(), packet.getLongitude(), packet.getAltitudeMsl(),
				packet.getAltitudeAgl()));
		synchronized (monitor) {
			pos1Ready = true;
		}
	}

	@Override
	public void onPacket(AngularOrientationPacket packet) {
		position.setPitch(packet.getPitch());
		position.setRoll(packet.getRoll());
		position.setHeading(packet.getTrueHeading());
		position.setMagHeading(packet.getMagHeading());
		synchronized (monitor) {
			pos2Ready = true;
		}
	}

	public void init() {
		busUpdatePeriod = config.getBusUpdatePeriod();
		log.info("Using " + busUpdatePeriod + " as bus update period");

		final int receivePort = config.getXplaneInPort();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		realTimeWorker = new RealtimeThread(new PriorityParameters(PriorityScheduler.instance().getMaxPriority() - 1),
				null, null, null, null, new UdpReceiver(receivePort, parser));

		realTimeWorker.start();

		realTimeTimer = new PeriodicTimer(null, new RelativeTime(busUpdatePeriod, 0),
				new AsyncEventHandler(new Runnable() {

					@Override
					public void run() {
						synchronized (monitor) {
							if (pos1Ready && pos2Ready) {
								bus.broadcast(position);
								pos1Ready = false;
								pos2Ready = false;
							}
						}
					}
				}));
		realTimeTimer.start();
	}
}

class UdpReceiver implements Runnable {
	private static final Charset UTF8 = Charset.forName("UTF8");
	private static final int PACKET_SIZE = 1024;
	private static final Logger log = Logger.getLogger(UdpReceiver.class.getName());

	private final XPlaneUdpParser parser;

	private DatagramChannel inChannel = null;

	public UdpReceiver(int receivePort, XPlaneUdpParser parser) {
		this.parser = parser;

		try {
			inChannel = DatagramChannel.open();
			log.info("Starting UDP listener");
			inChannel.socket().setReceiveBufferSize(2 * PACKET_SIZE);
			inChannel.socket().bind(new InetSocketAddress(receivePort));
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	@Override
	public void run() {
		try {
			final ByteBuffer buf = ByteBuffer.allocate(PACKET_SIZE);
			while (!Thread.interrupted()) {
				try {
					inChannel.receive(buf);
					parser.parseMessage(buf);
				} catch (ClosedByInterruptException e) {
					// Expected as external shutdown signal
				} catch (ParseException e) {
					handleParseException(buf, e);
				} finally {
					buf.clear();
				}
			}

		} catch (IOException ex) {
			log.warning("UDP connection error " + ex);
		} finally {

			if (inChannel != null) {
				try {
					log.info("Closing UDP socket");
					inChannel.close();
				} catch (IOException e) {
					log.warning("Could not close UDP Connection " + e);
				}

			}
		}
	}

	private void handleParseException(ByteBuffer buf, ParseException e) {
		buf.rewind();
		final String bufContents = UTF8.decode(buf).toString();
		//
		//		log.warning("Buffer info: " + buf);
		//		log.warning("Buffer was: " + bufContents);
		//		log.severe("Could not parse received UDP packet " + e);
	}
}
