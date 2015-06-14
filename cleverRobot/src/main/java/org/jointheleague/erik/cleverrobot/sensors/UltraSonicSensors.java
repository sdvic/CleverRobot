package org.jointheleague.erik.cleverrobot.sensors;

/**************************************************************************
 * Simplified version 140512A by Erik  Super Happy Version
 * version 150225A AndroidStudio version
 **************************************************************************/
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PulseInput;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.exception.ConnectionLostException;
import android.os.SystemClock;

/**
 * An UltraSonicSensors instance is used to access three ultrasonic sensors
 * (leftInput, frontInput, and rightInput) and read the measurements from these
 * sensors. version 140427...modified by Vic...ultrasonics works using Ytai's
 * suggestions...cleaned up formatting
 * @author Erik Colban
 */
public class UltraSonicSensors
{
	private static final float CONVERSION_FACTOR = 17280.0F; // cm/s
	private static int LEFT_ULTRASONIC_INPUT_PIN = 35;
	private static int FRONT_ULTRASONIC_INPUT_PIN = 36;
	private static int RIGHT_ULTRASONIC_INPUT_PIN = 37;
	private static final int LEFT_STROBE_ULTRASONIC_OUTPUT_PIN = 15;
	private static final int FRONT_STROBE_ULTRASONIC_OUTPUT_PIN = 16;
	private static final int RIGHT_STROBE_ULTRASONIC_OUTPUT_PIN = 17;
	private DigitalOutput strobeLeft;
	private DigitalOutput strobeFront;
	private DigitalOutput strobeRight;
	private volatile int distanceLeft;
	private volatile int distanceFront;
	private volatile int distanceRight;
	private IOIO ioio;

	/**
	 * Constructor of a UltraSonicSensors instance.
	 * @param ioio the IOIO instance used to communicate with the sensor
	 * @throws ConnectionLostException
	 */
	public UltraSonicSensors(IOIO ioio) throws ConnectionLostException
	{
		this.ioio = ioio;
		this.strobeLeft = ioio.openDigitalOutput(LEFT_STROBE_ULTRASONIC_OUTPUT_PIN);
		this.strobeRight = ioio.openDigitalOutput(RIGHT_STROBE_ULTRASONIC_OUTPUT_PIN);
		this.strobeFront = ioio.openDigitalOutput(FRONT_STROBE_ULTRASONIC_OUTPUT_PIN);
	}

	/**
	 * Makes a reading of the ultrasonic sensors and stores the results locally.
	 * To access these readings, use {@link #getDistanceLeft()},
	 * {@link #getDistanceFront()}, and {@link #getDistanceRight()}.
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	public void read() throws ConnectionLostException, InterruptedException
	{
		distanceLeft = read(strobeLeft, LEFT_ULTRASONIC_INPUT_PIN);
		distanceFront = read(strobeFront, FRONT_ULTRASONIC_INPUT_PIN);
		distanceRight = read(strobeRight, RIGHT_ULTRASONIC_INPUT_PIN);
	}

	private int read(DigitalOutput strobe, int inputPin) throws ConnectionLostException, InterruptedException
	{
		ioio.beginBatch();//order of statements critical...do not change
		strobe.write(true);
		PulseInput input = ioio.openPulseInput(inputPin, PulseMode.POSITIVE);
		ioio.endBatch();
		SystemClock.sleep(20);
		strobe.write(false);
		int distance = (int) (input.getDuration() * CONVERSION_FACTOR);
		input.close();
		return distance;
	}

	public int getDistanceLeft()
	{
		return distanceLeft;
	}

	public int getDistanceFront()
	{
		return distanceFront;
	}

	public int getDistanceRight()
	{
		return distanceRight;
	}

	public void closeConnection()
	{
		strobeLeft.close();
		strobeFront.close();
		strobeRight.close();
	}
}
