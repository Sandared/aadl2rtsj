package bus.api;

/**
 * Marks the type of a specific bus message.
 *
 * @see api.data.bus.BusMessage
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public enum BusMessageType {
							Timestamp,
							EngineStatus,
							Position,
							AutopilotSettings,
							AutopilotStatus,
							RadioSettings,

							ThrottleRequest,
							AltitudeCommand,
							RollCommand,
							HeadingCommand,
							PitchCommand,
							PositionCommand,
							ShutdownCommand,

							ThrottleCommand,
							AileronCommand,
							ElevatorCommand,
							ElevatorTrimCommand,
							HeadingBugCommand,
							RudderTrimCommand,
							ThrottleChangeCommand,
}
