package bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;

import bus.api.Bus;
import bus.api.BusListener;
import bus.api.BusMessage;
import bus.api.ThrottleCommand;

/**
 * Simple message bus implementation that directly invokes bus listeners.
 * <p/>
 * Message broadcast, as well as adding and removing listeners is handled in a
 * thread-safe manner.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class StatusBus implements Bus {
	private static final Logger log = Logger.getLogger(StatusBus.class.getName());
	private final Set<BusListener> listeners = new CopyOnWriteArraySet<BusListener>();
	private final BlockingQueue<BusMessage> queue = new LinkedBlockingQueue<BusMessage>();
	private RealtimeThread realTimeDispatcher;

	@Override
	public void registerListener(BusListener listener) {
		log.info("registerListener(" + listener + ")");
		if (listener == null) {
			throw new IllegalArgumentException("listener must not be null.");
		}

		listeners.add(listener);
	}

	@Override
	public void unregisterListener(BusListener listener) {
		log.info("unregisterListener(" + listener + ")");
		if (listener == null) {
			throw new IllegalArgumentException("listener must not be null");
		}
		if (!listeners.contains(listener)) {
			throw new IllegalStateException("Listener to remove was not previously registered");
		}

		listeners.add(listener);
	}

	@Override
	public void broadcast(BusMessage message) {
		if (message == null) {
			throw new IllegalArgumentException("Broadcast message must not be null");
		}

		queue.add(message);
	}

	public void init() {
		realTimeDispatcher = new RealtimeThread(
				new PriorityParameters(PriorityScheduler.instance().getMaxPriority() - 1), null, null, null, null,
				new MessageDispatcher());
		realTimeDispatcher.start();
	}

	class MessageDispatcher implements Runnable {

		@Override
		public void run() {
			while (!Thread.interrupted()) {
				try {
					final BusMessage message = queue.take();

					for (final BusListener listener : listeners) {
						listener.processMessage(message);
					}

				} catch (InterruptedException ex) {
					break;
				}
			}
		}
	}
}
