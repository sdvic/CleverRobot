package org.jointheleague.erik.irobot;

import android.os.SystemClock;
import android.util.Log;
import android.util.SparseIntArray;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * A final class that provides a high level interface to the iRobot series of
 * Roomba/Create robots.
 * <p/>
 * The names of most of the readSensors are derived mostly from the <a href=
 * "http://www.irobot.com/~/media/MainSite/PDFs/About/STEM/Create/iRobot_Roomba_600_Open_Interface_Spec_0512.pdf"
 * > iRobot Create 2 Open Interface (OI)</a>. Recommend reading this document in order to get a
 * better understanding of how to work with the robot.
 * <p/>
 * Note: The default constructor will not return until it has managed to connect
 * with the iRobot via the default serial interface from the IOIO to the iRobot.
 * This means that once you have received the instance, you are ok to control
 * the robot.
 * <p/>
 * <b>NOTE:</b> This class is final; it cannot be extended. Extend
 * {@link IRobotAdapter IRobotAdapter} instead.
 */
public final class SimpleIRobot implements IRobotInterface {

    private static final String TAG = "SimpleIRobot";
    /**
     * This command puts the OI into Safe mode, enabling user control of iRobot.
     * It turns off all LEDs. The OI can be in Passive, Safe, or Full mode to
     * accept this command.
     *
     * @see #safe()
     */
    private static final int COMMAND_MODE_SAFE = 131;
    /**
     * This command gives you complete control over iRobot by putting the OI
     * into Full mode, and turning off the cliff, wheel-drop and internal
     * charger safety features. That is, in Full mode, iRobot executes any
     * command that you send it, even if the internal charger is plugged in, or
     * the robot senses a cliff or wheel drop.
     *
     * @see #full()
     */
    private static final int COMMAND_MODE_FULL = 132;

    private static final int COMMAND_RESET = 7;

    private static final int COMMAND_STOP = 173;
    /**
     * This command controls iRobot's drive wheels. It takes four data bytes,
     * interpreted as two 16-bit signed values using two's complement. The first
     * two bytes specify the average velocity of the drive wheels in millimeters
     * per second (mm/s), with the high byte being sent first. The next two
     * bytes specify the radius in millimeters at which iRobot will turn. The
     * longer radii make iRobot drive straighter, while the shorter radii make
     * iRobot turn more. The radius is measured from the center of the turning
     * circle to the center of iRobot. A Drive command with a positive velocity
     * and a positive radius makes iRobot drive forward while turning toward the
     * left. A negative radius makes iRobot turn toward the right. Special cases
     * for the radius make iRobot turn in place or drive straight, as specified
     * below. A negative velocity makes iRobot drive backward. <br>
     * NOTE: Internal and environmental restrictions may prevent iRobot from
     * accurately carrying out some drive commands. For example, it may not be
     * possible for iRobot to drive at full speed in an arc with a large radius
     * of curvature. <br>
     * + Available in modes: Safe or Full <br>
     * + Changes mode to: No Change <br>
     * + Velocity (-500 - 500 mm/s) <br>
     * + Radius (-2000 - 2000 mm) <br>
     * Special cases: <br>
     * Straight = 32768 or 32767 = hex 8000 or 7FFF <br>
     * Turn in place clockwise = hex FFFF <br>
     * Turn in place counter-clockwise = hex 0001
     *
     * @see #drive(int, int)
     */
    private static final int COMMAND_DRIVE = 137;
    /**
     * This command controls the LEDs on iRobot. The state of LEDs is specified by
     * two bits in the first data byte. The power LED is specified by two data bytes:
     * one for the color and the other for the intensity.
     * <p/>
     * This command shall be followed by 3 data bytes:
     * <p/>
     * <ul>
     * <li>LED bits, identifies the LEDS (bit 0 - Debris, bit 1 - Spot, bit 2 - Home,
     * bit 3 - Check Robot) </li>
     * <li>Power Color, (0 - 255) 0 = green, 255 = red. Intermediate values are
     * intermediate colors (orange, yellow, etc). </li>
     * <li>Power Intensity, (0 - 255) 0 = off, 255 = full intensity. Intermediate
     * values are intermediate intensities.</li>
     * </ul>
     *
     * @see IRobotInterface#leds(int, int, boolean)
     */
    private static final int COMMAND_LEDS = 139;
    /**
     * Mask identifying the Spot LED and the Spot button.
     */
    private static final int SPOT_BUTTON_LED_ID = 2;
    /**
     * This command lets you specify up to sixteen songs to the OI that you can
     * play at a later time. Each song is associated with a song number. The
     * Play command uses the song number to identify your song selection. Each
     * song can contain up to sixteen notes. Each note is associated with a note
     * number that uses MIDI note definitions and a duration that is specified
     * in fractions of a second. The number of data bytes varies, depending on
     * the length of the song specified. A one note song is specified by four
     * data bytes. For each additional note within a song, add two data bytes.
     *
     * @see #song(int, int[])
     * @see #song(int, int[], int, int)
     */
    private static final int COMMAND_SONG = 140;
    /**
     * This command lets you select a song to play from the songs added to
     * iRobot using the Song command. You must add one or more songs to
     * iRobot using the Song command in order for the Play command to work.
     * Also, this command does not work if a song is already playing. Wait until
     * a currently playing song is done before sending this command. Note that
     * {@link IRobotInterface#SENSORS_SONG_PLAYING} can be used to check
     * whether the iRobot is ready to accept this command.
     *
     * @see #playSong(int)
     */
    private static final int COMMAND_PLAY_SONG = 141;
    /**
     * This command requests the OI to send a packet of sensor data bytes. There
     * are 43 different sensor data packets. Each provides a value of a specific
     * sensor or group of sensors.
     *
     * @see #readSensors(int)
     */
    private static final int COMMAND_SENSORS = 142;
    /**
     * This command lets you control the forward and backward motion of iRobot's
     * drive wheels independently. It takes four data bytes, which are
     * interpreted as two 16-bit signed values using two's complement. The first
     * two bytes specify the velocity of the right wheel in millimeters per
     * second (mm/s), with the high byte sent first. The next two bytes specify
     * the velocity of the left wheel, in the same format. A positive velocity
     * makes that wheel drive forward, while a negative velocity makes it drive
     * backward. <br>
     * + Available in modes: Safe or Full <br>
     * + Changes mode to: No Change <br>
     * + Right wheel velocity (-500 - 500 mm/s) <br>
     * + Left wheel velocity (-500 - 500 mm/s)
     *
     * @see #driveDirect(int, int)
     */
    private static final int COMMAND_DRIVE_DIRECT = 145;
    /**
     * Time in ms to pause after sending a command to the iRobot.
     */
    private static final int AFTER_COMMAND_PAUSE_TIME = 20;
    // Sensor values previously read:
    private int angle;
    private int batteryCapacity;
    private int batteryCharge;
    private int batteryTemperature;
    private boolean bumpLeft;
    private boolean bumpRight;
    private int chargingState;
    private boolean cliffFrontLeft;
    private int cliffSignalLeftFront;
    private boolean cliffFrontRight;
    private int cliffSignalRightFront;
    private boolean cliffLeft;
    private int cliffSignalLeft;
    private boolean cliffRight;
    private int cliffSignalRight;
    private int current;
    private int distance;
    private int encoderCountLeft;
    private int encoderCountRight;
    private boolean homeBaseChargerAvailable;
    private int infraredByte;
    private int infraredByteLeft;
    private int infraredByteRight;
    private boolean internalChargerAvailable;
    private boolean wheelOvercurrentLeft;
    private int lightBump;
    private int lightBumpSignalLeft;
    private int lightBumpSignalLeftFront;
    private int lightBumpSignalLeftCenter;
    private int lightBumpSignalRightCenter;
    private int lightBumpSignalRightFront;
    private int lightBumpSignalRight;
    private boolean wheelOvercurrentSideBrush;
    private boolean wheelOvercurrentMainBrush;
    private int motorCurrentLeft;
    private int motorCurrentRight;
    private int oiMode;
    private int buttons;
    private int requestedVelocityLeft;
    private int requestedRadius;
    private int requestedVelocityRight;
    private int requestedVelocity;
    private boolean wheelOvercurrentRight;
    private int songNumber;
    private boolean songPlaying;
    private boolean stasis;
    private boolean virtualWall;
    private int voltage;
    private boolean wall;
    private int wallSignal;
    private boolean wheelDropLeft;
    private boolean wheelDropRight;
    private SerialConnection serialConnection;
    private int powerLedColor;
    private int powerLedIntensity;
    private boolean isSpotLedOn;
    private SparseIntArray sensorGroupLow;
    private SparseIntArray sensorGroupHigh;

    /**
     * Constructor that uses the IOIO instance to communicate with the iRobot.
     * Equivalent to using <code>SimpleIRobot(ioio, false, true, true)</code>
     *
     * @param ioio The IOIO instance used to communicate with the iRobot
     * @throws ConnectionLostException
     * @see #SimpleIRobot(IOIO, boolean, boolean, boolean)
     */
    public SimpleIRobot(IOIO ioio) throws ConnectionLostException {
        this(ioio, false, true, true);
    }

    /**
     * Constructor that uses the IOIO instance to communicate with the iRobot.
     *
     * @param ioio        The IOIO instance used to communicate with the iRobot
     * @param debugSerial if true will create a default serial connection with debug
     *                    true
     * @param fullMode    if true enter full mode, otherwise enter safe mode
     * @param waitButton  if true wait until play button is pressed
     * @throws ConnectionLostException
     */
    public SimpleIRobot(IOIO ioio, boolean debugSerial, boolean fullMode, boolean waitButton)
            throws ConnectionLostException {
        this(SerialConnection.getInstance(ioio, debugSerial), fullMode, waitButton);
    }

    /**
     * Constructor that uses a given serial connection as its means of sending
     * and reading data to and from the iRobot.
     *
     * @param sc         user-specified serial connection.
     * @param fullMode   if true enter full mode, otherwise enter safe mode
     * @param waitButton if true wait until play button is pressed
     * @throws ConnectionLostException
     */
    SimpleIRobot(SerialConnection sc, boolean fullMode, boolean waitButton)
            throws ConnectionLostException {
        this.serialConnection = sc;
        buildSensorGroups();
        if (fullMode) {
            full();
        } else {
            safe();
        }
        if (waitButton) {
            waitButtonPressed(true);
        }
        startSpotListener();
    }

    private void startSpotListener() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean running = true;
                try {
                    while (running) {
                        SystemClock.sleep(1000L);
                        readSensors(SENSORS_GROUP_ID2);
                        Log.d(TAG, "Spot button sensor read.");
                        if (isSpotButtonDown()) {
                            Log.d(TAG, "Spot button down.");
                            stop();
                            closeConnection();
                            running = false;
                        }
                    }
                } catch (ConnectionLostException e) {
                    Log.w(TAG, "ConnectionLostException occurred. " + e.getMessage());
                }
            }
        }).start();
        Log.d(TAG, "Spot listener started.");
    }

    private void buildSensorGroups() {
        sensorGroupLow = new SparseIntArray(11);
        sensorGroupHigh = new SparseIntArray(11);
        addGroup(0, 7, 26);
        addGroup(1, 7, 16);
        addGroup(2, 17, 20);
        addGroup(3, 21, 26);
        addGroup(4, 27, 34);
        addGroup(5, 35, 42);
        addGroup(6, 7, 42);
        addGroup(100, 7, 58);
        addGroup(101, 43, 58);
        addGroup(106, 46, 51);
        addGroup(107, 54, 58);
    }

    private void addGroup(int key, int low, int high) {
        sensorGroupLow.put(key, low);
        sensorGroupHigh.put(key, high);

    }


    public synchronized void drive(int velocity, int radius)
            throws ConnectionLostException {
        serialConnection.writeByte(COMMAND_DRIVE);
        serialConnection.writeSignedWord(velocity);
        serialConnection.writeSignedWord(radius);
        SystemClock.sleep(AFTER_COMMAND_PAUSE_TIME);
    }

    public synchronized void driveDirect(int leftVelocity, int rightVelocity)
            throws ConnectionLostException {
        serialConnection.writeByte(COMMAND_DRIVE_DIRECT);
        serialConnection.writeSignedWord(rightVelocity);
        serialConnection.writeSignedWord(leftVelocity);
        SystemClock.sleep(AFTER_COMMAND_PAUSE_TIME);
    }

    public synchronized void full() throws ConnectionLostException {
        serialConnection.writeByte(COMMAND_MODE_FULL);
        SystemClock.sleep(AFTER_COMMAND_PAUSE_TIME);
    }

    public synchronized int getAngle() {
        return angle;
    }

    public synchronized int getBatteryCapacity() {
        return batteryCapacity;
    }

    public synchronized int getBatteryCharge() {
        return batteryCharge;
    }

    public synchronized int getBatteryTemperature() {
        return batteryTemperature;
    }

    public synchronized int getChargingState() {
        return chargingState;
    }

    public synchronized int getCliffSignalLeftFront() {
        return cliffSignalLeftFront;
    }

    public synchronized int getCliffSignalRightFront() {
        return cliffSignalRightFront;
    }

    public synchronized int getCliffSignalLeft() {
        return cliffSignalLeft;
    }

    public synchronized int getCliffSignalRight() {
        return cliffSignalRight;
    }

    public synchronized int getCurrent() {
        return current;
    }

    public synchronized int getDistance() {
        return distance;
    }

    public int getEncoderCountLeft() {
        return encoderCountLeft;
    }

    public int getEncoderCountRight() {
        return encoderCountRight;
    }

    public synchronized int getInfraredByte() {
        return infraredByte;
    }

    public int getInfraredByteLeft() {
        return infraredByteLeft;
    }

    public int getInfraredByteRight() {
        return infraredByteRight;
    }

    public synchronized int getOiMode() {
        return oiMode;
    }

    public synchronized int getRequestedVelocityLeft() {
        return requestedVelocityLeft;
    }

    public synchronized int getRequestedRadius() {
        return requestedRadius;
    }

    public synchronized int getRequestedVelocityRight() {
        return requestedVelocityRight;
    }

    public synchronized int getRequestedVelocity() {
        return requestedVelocity;
    }

//    private boolean getSensorBoolean(int sensorId) {
//        return sensorValues[sensorId] != 0;
//    }
//
//    private int getSensorInteger(int sensorId) {
//        return sensorValues[sensorId];
//    }

    public synchronized int getSongNumber() {
        return songNumber;
    }

    public synchronized int getVoltage() {
        return voltage;
    }

    @Override
    public int getMotorCurrentLeft() {
        return motorCurrentLeft;
    }

    @Override
    public int getMotorCurrentRight() {
        return motorCurrentRight;
    }

    @Override
    public boolean isStasis() {
        return stasis;
    }

    public synchronized int getWallSignal() {
        return wallSignal;
    }

    public synchronized boolean isBumpLeft() {
        return bumpLeft;
    }

    public synchronized boolean isBumpRight() {
        return bumpRight;
    }

    @Override
    public boolean isLightBump() {
        return (lightBump & 0x3F) != 0;
    }

    @Override
    public int[] getLightBumps() {
        int[] result = new int[6];
        int bumps = lightBump;
        result[0] = (bumps & 0x1) != 0 ? lightBumpSignalLeft : 0;
        result[1] = (bumps & 0x2) != 0 ? lightBumpSignalLeftFront : 0;
        result[2] = (bumps & 0x4) != 0 ? lightBumpSignalLeftCenter : 0;
        result[3] = (bumps & 0x8) != 0 ? lightBumpSignalRightCenter : 0;
        result[4] = (bumps & 0x10) != 0 ? lightBumpSignalRightFront : 0;
        result[5] = (bumps & 0x20) != 0 ? lightBumpSignalRight : 0;
        return result;
    }

    public synchronized boolean isCliffFrontLeft() {
        return cliffFrontLeft;
    }

    public synchronized boolean isCliffFrontRight() {
        return cliffFrontRight;
    }

    public synchronized boolean isCliffLeft() {
        return cliffLeft;
    }

    public synchronized boolean isCliffRight() {
        return cliffRight;
    }

    public synchronized boolean isHomeBaseChargerAvailable() {
        return homeBaseChargerAvailable;
    }

    public synchronized boolean isInternalChargerAvailable() {
        return internalChargerAvailable;
    }

    public synchronized boolean isLeftWheelOvercurrent() {
        return wheelOvercurrentLeft;
    }

    public synchronized boolean isWheelOvercurrentSideBrush() {
        return wheelOvercurrentSideBrush;
    }

    public synchronized boolean isWheelOvercurrentMainBrush() {
        return wheelOvercurrentMainBrush;
    }

    public synchronized boolean isRightWheelOvercurrent() {
        return wheelOvercurrentRight;
    }

    public synchronized boolean isSongPlaying() {
        return songPlaying;
    }

    public synchronized boolean isSpotButtonDown() {
        return (buttons & SPOT_BUTTON_LED_ID) != 0;
    }

    public synchronized boolean isVirtualWall() {
        return virtualWall;
    }

    public synchronized boolean isWall() {
        return wall;
    }

    public synchronized boolean isWheelDropLeft() {
        return wheelDropLeft;
    }

    public synchronized boolean isWheelDropRight() {
        return wheelDropRight;
    }

    public synchronized void leds(int powerColor, int powerIntensity, boolean spotLedOn) throws ConnectionLostException {
        serialConnection.writeByte(COMMAND_LEDS);
        serialConnection.writeByte(spotLedOn ? SPOT_BUTTON_LED_ID : 0);
        serialConnection.writeByte(powerColor);
        serialConnection.writeByte(powerIntensity);
        SystemClock.sleep(AFTER_COMMAND_PAUSE_TIME);
        powerLedColor = powerColor;
        powerLedIntensity = powerIntensity;
        isSpotLedOn = spotLedOn;
    }

    public synchronized void ledsToggle(boolean togglePower)
            throws ConnectionLostException {
        if (togglePower) {
            powerLedIntensity = powerLedIntensity ^ 0xFF;
        }
        isSpotLedOn = !isSpotLedOn;
        leds(powerLedColor, powerLedIntensity, isSpotLedOn);
    }


    public synchronized void playSong(int songNumber)
            throws ConnectionLostException {
        serialConnection.writeByte(COMMAND_PLAY_SONG);
        serialConnection.writeByte(songNumber);
        SystemClock.sleep(AFTER_COMMAND_PAUSE_TIME);
    }


    public synchronized void readSensors(int sensorId)
            throws ConnectionLostException {
        serialConnection.writeByte(COMMAND_SENSORS);
        serialConnection.writeByte(sensorId);
        SystemClock.sleep(AFTER_COMMAND_PAUSE_TIME);
        saveSensorData(sensorId);
    }

    private void saveSensorData(int sensorId) throws ConnectionLostException {
        int low = sensorGroupLow.get(sensorId, sensorId);
        int high = sensorGroupHigh.get(sensorId, sensorId);
        for (int i = low; i <= high; i++) {
            saveSensorDataPrim(i);
        }
    }

    private void saveSensorDataPrim(int sensorId) throws ConnectionLostException {
        int dataByte, dataWord;

        switch (sensorId) {
            case SENSORS_BUMPS_AND_WHEEL_DROPS:
                dataByte = serialConnection.readUnsignedByte();
                bumpRight = (dataByte & 0x01) != 0;
                bumpLeft = (dataByte & 0x02) != 0;
                wheelDropRight = (dataByte & 0x04) != 0;
                wheelDropLeft = (dataByte & 0x08) != 0;
                break;
            case SENSORS_WALL:
                dataByte = serialConnection.readUnsignedByte();
                wall = (dataByte & 0x01) != 0;
                break;
            case SENSORS_CLIFF_LEFT:
                dataByte = serialConnection.readUnsignedByte();
                cliffLeft = (dataByte & 0x01) != 0;
                break;
            case SENSORS_CLIFF_FRONT_LEFT:
                dataByte = serialConnection.readUnsignedByte();
                cliffFrontLeft = (dataByte & 0x01) != 0;
                break;
            case SENSORS_CLIFF_FRONT_RIGHT:
                dataByte = serialConnection.readUnsignedByte();
                cliffFrontRight = (dataByte & 0x01) != 0;
                break;
            case SENSORS_CLIFF_RIGHT:
                dataByte = serialConnection.readUnsignedByte();
                cliffRight = (dataByte & 0x01) != 0;
                break;
            case SENSORS_VIRTUAL_WALL:
                dataByte = serialConnection.readUnsignedByte();
                virtualWall = (dataByte & 0x01) != 0;
                break;
            case SENSORS_WHEEL_OVERCURRENTS:
                dataByte = serialConnection.readUnsignedByte();
                wheelOvercurrentSideBrush = (dataByte & 0x01) != 0;
                wheelOvercurrentMainBrush = (dataByte & 0x04) != 0;
                wheelOvercurrentRight = (dataByte & 0x08) != 0;
                wheelOvercurrentLeft = (dataByte & 0x10) != 0;
                break;
            case SENSORS_DIRT_DETECT:
                serialConnection.readUnsignedByte();
                break;
            case SENSORS_DUMMY2:
                serialConnection.readUnsignedByte();
                break;
            case SENSORS_INFRARED_BYTE:
                infraredByte = serialConnection.readUnsignedByte();
                break;
            case SENSORS_INFRARED_BYTE_LEFT:
                infraredByteLeft = serialConnection.readUnsignedByte();
                break;
            case SENSORS_INFRARED_BYTE_RIGHT:
                infraredByteRight = serialConnection.readUnsignedByte();
                break;
            case SENSORS_BUTTONS:
                dataByte = serialConnection.readUnsignedByte();
                buttons = dataByte;
                break;
            case SENSORS_DISTANCE:
                dataWord = serialConnection.readSignedWord();
                distance = dataWord;
                break;
            case SENSORS_ANGLE:
                dataWord = serialConnection.readSignedWord();
                angle = dataWord;
                break;
            case SENSORS_CHARGING_STATE:
                chargingState = serialConnection.readUnsignedByte();
                break;
            case SENSORS_VOLTAGE:
                voltage = serialConnection.readUnsignedWord();
                break;
            case SENSORS_CURRENT:
                current = serialConnection.readSignedWord();
                break;
            case SENSORS_BATTERY_TEMPERATURE:
                batteryTemperature = serialConnection.readSignedByte();
                break;
            case SENSORS_BATTERY_CHARGE:
                batteryCharge = serialConnection.readUnsignedWord();
                break;
            case SENSORS_BATTERY_CAPACITY:
                batteryCapacity = serialConnection.readUnsignedWord();
                break;
            case SENSORS_WALL_SIGNAL:
                wallSignal = serialConnection.readUnsignedWord();
                break;
            case SENSORS_CLIFF_SIGNAL_LEFT:
                cliffSignalLeft = serialConnection.readUnsignedWord();
                break;
            case SENSORS_CLIFF_SIGNAL_LEFT_FRONT:
                cliffSignalLeftFront = serialConnection.readUnsignedWord();
                break;
            case SENSORS_CLIFF_SIGNAL_RIGHT_FRONT:
                cliffSignalRightFront = serialConnection.readUnsignedWord();
                break;
            case SENSORS_CLIFF_SIGNAL_RIGHT:
                cliffSignalRight = serialConnection.readUnsignedWord();
                break;
            case SENSORS_CARGO_BAY_DIGITAL_INPUTS:
                serialConnection.readUnsignedByte();
                break;
            case SENSORS_CARGO_BAY_ANALOG_SIGNAL:
                serialConnection.readUnsignedWord();
                break;
            case SENSORS_CHARGING_SOURCES_AVAILABLE:
                dataByte = serialConnection.readUnsignedByte();
                internalChargerAvailable = (dataByte & 0x01) != 0;
                homeBaseChargerAvailable = (dataByte & 0x02) != 0;
                break;
            case SENSORS_OI_MODE:
                oiMode = serialConnection.readUnsignedByte();
                break;
            case SENSORS_SONG_NUMBER:
                songNumber = serialConnection.readUnsignedByte();
                break;
            case SENSORS_SONG_PLAYING:
                dataByte = serialConnection.readUnsignedByte();
                songPlaying = (dataByte & 0x01) != 0;
                break;
            case SENSORS_NUMBER_OF_STREAM_PACKETS:
                serialConnection.readUnsignedByte();
                break;
            case SENSORS_REQUESTED_VELOCITY:
                requestedVelocity = serialConnection.readSignedWord();
                break;
            case SENSORS_REQUESTED_RADIUS:
                requestedRadius = serialConnection.readSignedWord();
                break;
            case SENSORS_REQUESTED_VELOCITY_RIGHT:
                requestedVelocityRight = serialConnection.readSignedWord();
                break;
            case SENSORS_REQUESTED_VELOCITY_LEFT:
                requestedVelocityLeft = serialConnection.readSignedWord();
                break;
            case SENSORS_ENCODER_COUNT_LEFT:
                encoderCountLeft = serialConnection.readUnsignedWord();
                break;
            case SENSORS_ENCODER_COUNT_RIGHT:
                encoderCountRight = serialConnection.readUnsignedWord();
                break;
            case SENSORS_LIGHT_BUMPER:
                lightBump = serialConnection.readUnsignedByte();
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_LEFT:
                lightBumpSignalLeft = serialConnection.readUnsignedWord();
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_LEFT_FRONT:
                lightBumpSignalLeftFront = serialConnection.readUnsignedWord();
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_LEFT_CENTER:
                lightBumpSignalLeftCenter = serialConnection.readUnsignedWord();
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_RIGHT:
                lightBumpSignalRight = serialConnection.readUnsignedWord();
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_RIGHT_FRONT:
                lightBumpSignalRightFront = serialConnection.readUnsignedWord();
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_RIGHT_CENTER:
                lightBumpSignalRightCenter = serialConnection.readUnsignedWord();
                break;
            case SENSORS_MOTOR_CURRENT_LEFT:
                motorCurrentLeft = serialConnection.readUnsignedWord();
                break;
            case SENSORS_MOTOR_CURRENT_RIGHT:
                motorCurrentRight = serialConnection.readUnsignedWord();
                break;
            case SENSORS_MAIN_BRUSH_MOTOR_CURRENT:
                serialConnection.readSignedWord();
                break;
            case SENSORS_SIDE_BRUSH_MOTOR_CURRENT:
                serialConnection.readSignedWord();
                break;
            case SENSORS_STASIS:
                dataByte = serialConnection.readUnsignedByte();
                stasis = (dataByte & 0x1) != 0;
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(sensorId));
        }
    }

    public synchronized void reset() throws ConnectionLostException {
        serialConnection.writeByte(COMMAND_RESET);
        SystemClock.sleep(AFTER_COMMAND_PAUSE_TIME);
    }

    public synchronized void safe() throws ConnectionLostException {
        serialConnection.writeByte(COMMAND_MODE_SAFE);
        SystemClock.sleep(AFTER_COMMAND_PAUSE_TIME);
    }

    public synchronized void song(int songNumber, int[] notesAndDurations)
            throws ConnectionLostException {
        song(songNumber, notesAndDurations, 0, notesAndDurations.length);
    }

    public synchronized void song(int songNumber, int[] notesAndDurations, int startIndex,
                                  int length) throws ConnectionLostException {
        if (songNumber < 0 || songNumber > 15) {
            throw new IllegalArgumentException("songNumber " + songNumber);
        }
        if ((length & 0x01) == 0x01) {
            throw new IllegalArgumentException("length " + songNumber + "must be even");
        }
        if (length < 1 || length > (256 - (songNumber * 16 * 2))) {
            throw new IllegalArgumentException("length " + length);
        }
        serialConnection.writeByte(COMMAND_SONG);
        serialConnection.writeByte(songNumber);
        serialConnection.writeByte(length >> 1);
        serialConnection.writeBytes(notesAndDurations, startIndex, length);
        SystemClock.sleep(AFTER_COMMAND_PAUSE_TIME);
    }

    public void stop() throws ConnectionLostException {
        serialConnection.writeByte(COMMAND_STOP);
        SystemClock.sleep(AFTER_COMMAND_PAUSE_TIME);
    }

    public synchronized void waitButtonPressed(boolean beep)
            throws ConnectionLostException {
        int startingPowerLedIntensity = powerLedIntensity;
        int startingPowerLedColor = powerLedColor;
        boolean startingSpotLedState = isSpotLedOn;
        int totalTimeWaiting = 0;
        boolean gotButtonDown = false;
        final int noteDuration = 16;
        if (beep) {
            song(0, new int[]{58, noteDuration, 62, noteDuration});
        }
        while (true) {
            readSensors(SENSORS_BUTTONS);
            if (gotButtonDown && !isSpotButtonDown()) {
                break;
            }
            if (isSpotButtonDown()) {
                gotButtonDown = true;
            }
            SystemClock.sleep(noteDuration);
            totalTimeWaiting += noteDuration;
            if (totalTimeWaiting > 500) {
                if (beep) {
                    playSong(0);
                }
                ledsToggle(true);
                totalTimeWaiting = 0;
            }
        }
        leds(startingPowerLedColor, startingPowerLedIntensity, startingSpotLedState);
    }

    public void closeConnection() {
        if (serialConnection != null) {
            serialConnection.close();
        }
    }
}
