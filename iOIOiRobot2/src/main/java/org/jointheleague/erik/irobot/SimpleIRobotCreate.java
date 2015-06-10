package org.jointheleague.erik.irobot;

import android.os.SystemClock;
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
 * with the Create via the default serial interface from the IOIO to the create.
 * This means that once you have received the instance, you are ok to control
 * the robot.
 * <p/>
 * <b>NOTE:</b> This class is final; it cannot be extended. Extend
 * {@link IRobotCreateAdapter IRobotCreateAdapter} instead.
 */
public final class SimpleIRobotCreate implements IRobotCreateInterface {

    private static final String TAG = "SimpleIRobotCreate";
    /**
     * This command puts the OI into Safe mode, enabling user control of Create.
     * It turns off all LEDs. The OI can be in Passive, Safe, or Full mode to
     * accept this command.
     *
     * @see #safe()
     */
    private static final int COMMAND_MODE_SAFE = 131;
    /**
     * This command gives you complete control over Create by putting the OI
     * into Full mode, and turning off the cliff, wheel-drop and internal
     * charger safety features. That is, in Full mode, Create executes any
     * command that you send it, even if the internal charger is plugged in, or
     * the robot senses a cliff or wheel drop.
     *
     * @see #full()
     */
    private static final int COMMAND_MODE_FULL = 132;

    private static final int COMMAND_RESET = 7;

    private static final int COMMAND_STOP = 173;
    /**
     * This command controls Create's drive wheels. It takes four data bytes,
     * interpreted as two 16-bit signed values using two's complement. The first
     * two bytes specify the average velocity of the drive wheels in millimeters
     * per second (mm/s), with the high byte being sent first. The next two
     * bytes specify the radius in millimeters at which Create will turn. The
     * longer radii make Create drive straighter, while the shorter radii make
     * Create turn more. The radius is measured from the center of the turning
     * circle to the center of Create. A Drive command with a positive velocity
     * and a positive radius makes Create drive forward while turning toward the
     * left. A negative radius makes Create turn toward the right. Special cases
     * for the radius make Create turn in place or drive straight, as specified
     * below. A negative velocity makes Create drive backward. <br>
     * NOTE: Internal and environmental restrictions may prevent Create from
     * accurately carrying out some drive commands. For example, it may not be
     * possible for Create to drive at full speed in an arc with a large radius
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
     * This command controls the LEDs on Create. The state of LEDs is specified by
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
     * @see IRobotCreateInterface#leds(int, int, boolean)
     */
    private static final int COMMAND_LEDS = 139;
    /**
     * Mask identifying the Spot LED.
     */
    private static final int LEDS_SPOT = 2;
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
     * iRobot Create using the Song command. You must add one or more songs to
     * Create using the Song command in order for the Play command to work.
     * Also, this command does not work if a song is already playing. Wait until
     * a currently playing song is done before sending this command. Note that
     * {@link IRobotCreateInterface#SENSORS_SONG_PLAYING} can be used to check
     * whether the Create is ready to accept this command.
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
     * This command lets you control the forward and backward motion of Create's
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
     * Time in ms to pause after sending a command to the Create.
     */
    private static final int AFTER_COMMAND_PAUSE_TIME = 20;
    // Indices to an array containing the sensor values.
    private static final int SENSOR_IDX_advanceButton = 0;
    private static final int SENSOR_IDX_angle = 1;
    private static final int SENSOR_IDX_batteryCapacity = 2;
    private static final int SENSOR_IDX_batteryCharge = 3;
    private static final int SENSOR_IDX_batteryTemperature = 4;
    private static final int SENSOR_IDX_bumpLeft = 5;
    private static final int SENSOR_IDX_bumpRight = 6;
    private static final int SENSOR_IDX_chargingState = 8;
    private static final int SENSOR_IDX_cliffFrontLeft = 9;
    private static final int SENSOR_IDX_cliffFrontLeftSignal = 10;
    private static final int SENSOR_IDX_cliffFrontRight = 11;
    private static final int SENSOR_IDX_cliffFrontRightSignal = 12;
    private static final int SENSOR_IDX_cliffLeft = 13;
    private static final int SENSOR_IDX_cliffLeftSignal = 14;
    private static final int SENSOR_IDX_cliffRight = 15;
    private static final int SENSOR_IDX_cliffRightSignal = 16;
    private static final int SENSOR_IDX_current = 17;
    private static final int SENSOR_IDX_distance = 18;
    private static final int SENSOR_IDX_encoderCountLeft = 19;
    private static final int SENSOR_IDX_encoderCountRight = 20;
    private static final int SENSOR_IDX_homeBaseChargerAvailable = 21;
    private static final int SENSOR_IDX_infraredByte = 22;
    private static final int SENSOR_IDX_infraredByteLeft = 23;
    private static final int SENSOR_IDX_infraredByteRight = 24;
    private static final int SENSOR_IDX_internalChargerAvailable = 25;
    private static final int SENSOR_IDX_wheelOvercurrentLeft = 26;
    private static final int SENSOR_IDX_lightBumper = 27;
    private static final int SENSOR_IDX_lightBumpSignalLeft = 28;
    private static final int SENSOR_IDX_lightBumpSignalLeftFront = 29;
    private static final int SENSOR_IDX_lightBumpSignalLeftCenter = 30;
    private static final int SENSOR_IDX_lightBumpSignalRightCenter = 31;
    private static final int SENSOR_IDX_lightBumpSignalRightFront = 32;
    private static final int SENSOR_IDX_lightBumpSignalRight = 33;
    private static final int SENSOR_IDX_lowSideDriver0Overcurrent = 34;
    private static final int SENSOR_IDX_wheelOvercurrentSideBrush = 35;
    private static final int SENSOR_IDX_wheelOvercurrentMainBrush = 36;
    private static final int SENSOR_IDX_motorCurrentLeft = 37;
    private static final int SENSOR_IDX_motorCurrentRight = 38;
    private static final int SENSOR_IDX_numberOfStreamPackets = 39;
    private static final int SENSOR_IDX_oiMode = 40;
    private static final int SENSOR_IDX_buttons = 41;
    private static final int SENSOR_IDX_requestedLeftVelocity = 42;
    private static final int SENSOR_IDX_requestedRadius = 43;
    private static final int SENSOR_IDX_requestedRightVelocity = 44;
    private static final int SENSOR_IDX_requestedVelocity = 45;
    private static final int SENSOR_IDX_wheelOvercurrentRight = 46;
    private static final int SENSOR_IDX_songNumber = 47;
    private static final int SENSOR_IDX_songPlaying = 48;
    private static final int SENSOR_IDX_stasis = 49;
    private static final int SENSOR_IDX_virtualWall = 50;
    private static final int SENSOR_IDX_voltage = 51;
    private static final int SENSOR_IDX_wall = 52;
    private static final int SENSOR_IDX_wallSignal = 53;
    private static final int SENSOR_IDX_wheelDropLeft = 54;
    private static final int SENSOR_IDX_wheelDropRight = 55;
    private static final int SENSOR_IDX_MAX = 56;
    private final int[] sensorValues = new int[SENSOR_IDX_MAX];
    private SerialConnection serialConnection;
    private int powerLedColor;
    private int powerLedIntensity;
    private boolean isSpotLedOn;
    private SparseIntArray sensorGroupLow;
    private SparseIntArray sensorGroupHigh;

    /**
     * Constructor that uses the IOIO instance to communicate with the iRobot
     * Create. Equivalent to using
     * <code>SimpleIRobotCreate(ioio, false, true, true)</code>
     *
     * @param ioio The IOIO instance used to communicate with the Create
     * @throws ConnectionLostException
     * @see #SimpleIRobotCreate(IOIO, boolean, boolean, boolean)
     */
    public SimpleIRobotCreate(IOIO ioio) throws ConnectionLostException {
        this(ioio, false, true, true);
    }

    /**
     * Constructor that uses the IOIO instance to communicate with the iRobot
     * Create.
     *
     * @param ioio        The IOIO instance used to communicate with the Create
     * @param debugSerial if true will create a default serial connection with debug
     *                    true
     * @param fullMode    if true enter full mode, otherwise enter safe mode
     * @param waitButton  if true wait until play button is pressed
     * @throws ConnectionLostException
     */
    public SimpleIRobotCreate(IOIO ioio, boolean debugSerial, boolean fullMode, boolean waitButton)
            throws ConnectionLostException {
        this(SerialConnection.getInstance(ioio, debugSerial), fullMode, waitButton);
    }

    /**
     * Constructor that uses a given serial connection as its means of sending
     * and reading data to and from the Create.
     *
     * @param sc         user-specified serial connection.
     * @param fullMode   if true enter full mode, otherwise enter safe mode
     * @param waitButton if true wait until play button is pressed
     * @throws ConnectionLostException
     */
    SimpleIRobotCreate(SerialConnection sc, boolean fullMode, boolean waitButton)
            throws ConnectionLostException {
        this.serialConnection = sc;
        buildSensorGroups();
        if (fullMode) {
            full();
        } else {
            safe();
        }
        if (waitButton) {
            waitButtonPressed(true, true);
        }
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
        return getSensorInteger(SENSOR_IDX_angle);
    }

    public synchronized int getBatteryCapacity() {
        return getSensorInteger(SENSOR_IDX_batteryCapacity);
    }

    public synchronized int getBatteryCharge() {
        return getSensorInteger(SENSOR_IDX_batteryCharge);
    }

    public synchronized int getBatteryTemperature() {
        return getSensorInteger(SENSOR_IDX_batteryTemperature);
    }

    public synchronized int getChargingState() {
        return getSensorInteger(SENSOR_IDX_chargingState);
    }

    public synchronized int getCliffSignalLeftFront() {
        return getSensorInteger(SENSOR_IDX_cliffFrontLeftSignal);
    }

    public synchronized int getCliffSignalRightFront() {
        return getSensorInteger(SENSOR_IDX_cliffFrontRightSignal);
    }

    public synchronized int getCliffSignalLeft() {
        return getSensorInteger(SENSOR_IDX_cliffLeftSignal);
    }

    public synchronized int getCliffSignalRight() {
        return getSensorInteger(SENSOR_IDX_cliffRightSignal);
    }

    public synchronized int getCurrent() {
        return getSensorInteger(SENSOR_IDX_current);
    }

    public synchronized int getDistance() {
        return getSensorInteger(SENSOR_IDX_distance);
    }

    public int getEncoderCountLeft() {
        return getSensorInteger(SENSOR_IDX_encoderCountLeft);
    }

    public int getEncoderCountRight() {
        return getSensorInteger(SENSOR_IDX_encoderCountRight);
    }

    public synchronized int getInfraredByte() {
        return getSensorInteger(SENSOR_IDX_infraredByte);
    }

    public int getInfraredByteLeft() {
        return getSensorInteger(SENSOR_IDX_infraredByteLeft);
    }

    public int getInfraredByteRight() {
        return getSensorInteger(SENSOR_IDX_infraredByteRight);
    }

    public synchronized int getOiMode() {
        return getSensorInteger(SENSOR_IDX_oiMode);
    }

    public synchronized int getRequestedVelocityLeft() {
        return getSensorInteger(SENSOR_IDX_requestedLeftVelocity);
    }

    public synchronized int getRequestedRadius() {
        return getSensorInteger(SENSOR_IDX_requestedRadius);
    }

    public synchronized int getRequestedVelocityRight() {
        return getSensorInteger(SENSOR_IDX_requestedRightVelocity);
    }

    public synchronized int getRequestedVelocity() {
        return getSensorInteger(SENSOR_IDX_requestedVelocity);
    }

    private boolean getSensorBoolean(int sensorId) {
        return sensorValues[sensorId] != 0;
    }

    private int getSensorInteger(int sensorId) {
        return sensorValues[sensorId];
    }

    public synchronized int getSongNumber() {
        return getSensorInteger(SENSOR_IDX_songNumber);
    }

    public synchronized int getVoltage() {
        return getSensorInteger(SENSOR_IDX_voltage);
    }

    @Override
    public int getMotorCurrentLeft() {
        return getSensorInteger(SENSOR_IDX_motorCurrentLeft);
    }

    @Override
    public int getMotorCurrentRight() {
        return getSensorInteger(SENSOR_IDX_motorCurrentRight);
    }

    @Override
    public boolean getStasis() {
        return getSensorBoolean(SENSOR_IDX_stasis);
    }

    public synchronized int getWallSignal() {
        return getSensorInteger(SENSOR_IDX_wallSignal);
    }

    public synchronized boolean isAdvanceButtonDown() {
        return getSensorBoolean(SENSOR_IDX_advanceButton);
    }

    public synchronized boolean isBumpLeft() {
        return getSensorBoolean(SENSOR_IDX_bumpLeft);
    }

    public synchronized boolean isBumpRight() {
        return getSensorBoolean(SENSOR_IDX_bumpRight);
    }

    @Override
    public boolean isLightBump() {
        int lightBumps = getSensorInteger(SENSOR_IDX_lightBumper);
        return (lightBumps & 0x3f) != 0;
    }

    @Override
    public int[] getLightBumps() {
        int[] result = new int[6];
        int bumps = getSensorInteger(SENSOR_IDX_lightBumper);
        result[0] = (bumps & 0x1) != 0 ? getSensorInteger(SENSOR_IDX_lightBumpSignalLeft) : 0;
        result[1] = (bumps & 0x2) != 0 ? getSensorInteger(SENSOR_IDX_lightBumpSignalLeftFront) : 0;
        result[2] = (bumps & 0x4) != 0 ? getSensorInteger(SENSOR_IDX_lightBumpSignalLeftCenter) : 0;
        result[3] = (bumps & 0x8) != 0 ? getSensorInteger(SENSOR_IDX_lightBumpSignalRightCenter) : 0;
        result[4] = (bumps & 0x10) != 0 ? getSensorInteger(SENSOR_IDX_lightBumpSignalRightFront) : 0;
        result[5] = (bumps & 0x20) != 0 ? getSensorInteger(SENSOR_IDX_lightBumpSignalRight) : 0;
        return result;
    }

    public synchronized boolean isCliffFrontLeft() {
        return getSensorBoolean(SENSOR_IDX_cliffFrontLeft);
    }

    public synchronized boolean isCliffFrontRight() {
        return getSensorBoolean(SENSOR_IDX_cliffFrontRight);
    }

    public synchronized boolean isCliffLeft() {
        return getSensorBoolean(SENSOR_IDX_cliffLeft);
    }

    public synchronized boolean isCliffRight() {
        return getSensorBoolean(SENSOR_IDX_cliffRight);
    }

    public synchronized boolean isHomeBaseChargerAvailable() {
        return getSensorBoolean(SENSOR_IDX_homeBaseChargerAvailable);
    }

    public synchronized boolean isInternalChargerAvailable() {
        return getSensorBoolean(SENSOR_IDX_internalChargerAvailable);
    }

    public synchronized boolean isLeftWheelOvercurrent() {
        return getSensorBoolean(SENSOR_IDX_wheelOvercurrentLeft);
    }

    public synchronized boolean isLowSideDriver0Overcurrent() {
        return getSensorBoolean(SENSOR_IDX_lowSideDriver0Overcurrent);
    }

    public synchronized boolean isLowSideDriver1Overcurrent() {
        return getSensorBoolean(SENSOR_IDX_wheelOvercurrentSideBrush);
    }

    public synchronized boolean isLowSideDriver2Overcurrent() {
        return getSensorBoolean(SENSOR_IDX_wheelOvercurrentMainBrush);
    }

    public synchronized boolean isRightWheelOvercurrent() {
        return getSensorBoolean(SENSOR_IDX_wheelOvercurrentRight);
    }

    public synchronized boolean isSongPlaying() {
        return getSensorBoolean(SENSOR_IDX_songPlaying);
    }

    public synchronized boolean isSpotButtonDown() {
        return (getSensorInteger(SENSOR_IDX_buttons) & LEDS_SPOT) != 0;
    }

    public synchronized boolean isVirtualWall() {
        return getSensorBoolean(SENSOR_IDX_virtualWall);
    }

    public synchronized boolean isWall() {
        return getSensorBoolean(SENSOR_IDX_wall);
    }

    public synchronized boolean isWheelDropLeft() {
        return getSensorBoolean(SENSOR_IDX_wheelDropLeft);
    }

    public synchronized boolean isWheelDropRight() {
        return getSensorBoolean(SENSOR_IDX_wheelDropRight);
    }

    public synchronized void leds(int powerColor, int powerIntensity, boolean spotLedOn) throws ConnectionLostException {
        serialConnection.writeByte(COMMAND_LEDS);
        serialConnection.writeByte(spotLedOn ? LEDS_SPOT : 0);
        serialConnection.writeByte(powerColor);
        serialConnection.writeByte(powerIntensity);
        SystemClock.sleep(AFTER_COMMAND_PAUSE_TIME);
        powerLedColor = powerColor;
        powerLedIntensity = powerIntensity;
        isSpotLedOn = spotLedOn;
    }

    public synchronized void ledsToggle(boolean togglePower, boolean togglePlay)
            throws ConnectionLostException {
        if (togglePower) {
            powerLedIntensity = powerLedIntensity ^ 0xFF;
        }
        if (togglePlay) {
            isSpotLedOn = !isSpotLedOn;
        }
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
                setSensorBoolean(SENSOR_IDX_bumpRight, (dataByte & 0x01) != 0);
                setSensorBoolean(SENSOR_IDX_bumpLeft, (dataByte & 0x02) != 0);
                setSensorBoolean(SENSOR_IDX_wheelDropRight, (dataByte & 0x04) != 0);
                setSensorBoolean(SENSOR_IDX_wheelDropLeft, (dataByte & 0x08) != 0);
                break;
            case SENSORS_WALL:
                dataByte = serialConnection.readUnsignedByte();
                setSensorBoolean(SENSOR_IDX_wall, (dataByte & 0x01) != 0);
                break;
            case SENSORS_CLIFF_LEFT:
                dataByte = serialConnection.readUnsignedByte();
                setSensorBoolean(SENSOR_IDX_cliffLeft, (dataByte & 0x01) != 0);
                break;
            case SENSORS_CLIFF_FRONT_LEFT:
                dataByte = serialConnection.readUnsignedByte();
                setSensorBoolean(SENSOR_IDX_cliffFrontLeft, (dataByte & 0x01) != 0);
                break;
            case SENSORS_CLIFF_FRONT_RIGHT:
                dataByte = serialConnection.readUnsignedByte();
                setSensorBoolean(SENSOR_IDX_cliffFrontRight, (dataByte & 0x01) != 0);
                break;
            case SENSORS_CLIFF_RIGHT:
                dataByte = serialConnection.readUnsignedByte();
                setSensorBoolean(SENSOR_IDX_cliffRight, (dataByte & 0x01) != 0);
                break;
            case SENSORS_VIRTUAL_WALL:
                dataByte = serialConnection.readUnsignedByte();
                setSensorBoolean(SENSOR_IDX_virtualWall, (dataByte & 0x01) != 0);
                break;
            case SENSORS_WHEEL_OVERCURRENTS:
                dataByte = serialConnection.readUnsignedByte();
                setSensorBoolean(SENSOR_IDX_wheelOvercurrentSideBrush, (dataByte & 0x01) != 0);
                setSensorBoolean(SENSOR_IDX_wheelOvercurrentMainBrush, (dataByte & 0x04) != 0);
                setSensorBoolean(SENSOR_IDX_wheelOvercurrentRight, (dataByte & 0x08) != 0);
                setSensorBoolean(SENSOR_IDX_wheelOvercurrentLeft, (dataByte & 0x10) != 0);
                break;
            case SENSORS_DIRT_DETECT:
                serialConnection.readUnsignedByte();
                break;
            case SENSORS_DUMMY2:
                serialConnection.readUnsignedByte();
                break;
            case SENSORS_INFRARED_BYTE:
                setSensorInteger(SENSOR_IDX_infraredByte, serialConnection.readUnsignedByte());
                break;
            case SENSORS_INFRARED_BYTE_LEFT:
                setSensorInteger(SENSOR_IDX_infraredByteLeft, serialConnection.readUnsignedByte());
                break;
            case SENSORS_INFRARED_BYTE_RIGHT:
                setSensorInteger(SENSOR_IDX_infraredByteRight, serialConnection.readUnsignedByte());
                break;
            case SENSORS_BUTTONS:
                dataByte = serialConnection.readUnsignedByte();
                setSensorInteger(SENSOR_IDX_buttons, dataByte);
                break;
            case SENSORS_DISTANCE:
                dataWord = serialConnection.readSignedWord();
                setSensorInteger(SENSOR_IDX_distance, dataWord);
                break;
            case SENSORS_ANGLE:
                dataWord = serialConnection.readSignedWord();
                setSensorInteger(SENSOR_IDX_angle, dataWord);
                break;
            case SENSORS_CHARGING_STATE:
                setSensorInteger(SENSOR_IDX_chargingState, serialConnection.readUnsignedByte());
                break;
            case SENSORS_VOLTAGE:
                setSensorInteger(SENSOR_IDX_voltage, serialConnection.readUnsignedWord());
                break;
            case SENSORS_CURRENT:
                setSensorInteger(SENSOR_IDX_current, serialConnection.readSignedWord());
                break;
            case SENSORS_BATTERY_TEMPERATURE:
                setSensorInteger(SENSOR_IDX_batteryTemperature, serialConnection.readSignedByte());
                break;
            case SENSORS_BATTERY_CHARGE:
                setSensorInteger(SENSOR_IDX_batteryCharge, serialConnection.readUnsignedWord());
                break;
            case SENSORS_BATTERY_CAPACITY:
                setSensorInteger(SENSOR_IDX_batteryCapacity, serialConnection.readUnsignedWord());
                break;
            case SENSORS_WALL_SIGNAL:
                setSensorInteger(SENSOR_IDX_wallSignal, serialConnection.readUnsignedWord());
                break;
            case SENSORS_CLIFF_SIGNAL_LEFT:
                setSensorInteger(SENSOR_IDX_cliffLeftSignal, serialConnection.readUnsignedWord());
                break;
            case SENSORS_CLIFF_SIGNAL_LEFT_FRONT:
                setSensorInteger(SENSOR_IDX_cliffFrontLeftSignal, serialConnection.readUnsignedWord());
                break;
            case SENSORS_CLIFF_SIGNAL_RIGHT_FRONT:
                setSensorInteger(SENSOR_IDX_cliffFrontRightSignal, serialConnection.readUnsignedWord());
                break;
            case SENSORS_CLIFF_SIGNAL_RIGHT:
                setSensorInteger(SENSOR_IDX_cliffRightSignal, serialConnection.readUnsignedWord());
                break;
            case SENSORS_CARGO_BAY_DIGITAL_INPUTS:
                serialConnection.readUnsignedByte();
                break;
            case SENSORS_CARGO_BAY_ANALOG_SIGNAL:
                serialConnection.readUnsignedWord();
                break;
            case SENSORS_CHARGING_SOURCES_AVAILABLE:
                dataByte = serialConnection.readUnsignedByte();
                setSensorBoolean(SENSOR_IDX_internalChargerAvailable, (dataByte & 0x01) != 0);
                setSensorBoolean(SENSOR_IDX_homeBaseChargerAvailable, (dataByte & 0x02) != 0);
                break;
            case SENSORS_OI_MODE:
                setSensorInteger(SENSOR_IDX_oiMode, serialConnection.readUnsignedByte());
                break;
            case SENSORS_SONG_NUMBER:
                setSensorInteger(SENSOR_IDX_songNumber, serialConnection.readUnsignedByte());
                break;
            case SENSORS_SONG_PLAYING:
                dataByte = serialConnection.readUnsignedByte();
                setSensorBoolean(SENSOR_IDX_songPlaying, (dataByte & 0x01) != 0);
                break;
            case SENSORS_NUMBER_OF_STREAM_PACKETS:
                setSensorInteger(SENSOR_IDX_numberOfStreamPackets, serialConnection.readUnsignedByte());
                break;
            case SENSORS_REQUESTED_VELOCITY:
                setSensorInteger(SENSOR_IDX_requestedVelocity, serialConnection.readSignedWord());
                break;
            case SENSORS_REQUESTED_RADIUS:
                setSensorInteger(SENSOR_IDX_requestedRadius, serialConnection.readSignedWord());
                break;
            case SENSORS_REQUESTED_VELOCITY_RIGHT:
                setSensorInteger(SENSOR_IDX_requestedRightVelocity, serialConnection.readSignedWord());
                break;
            case SENSORS_REQUESTED_VELOCITY_LEFT:
                setSensorInteger(SENSOR_IDX_requestedLeftVelocity, serialConnection.readSignedWord());
                break;
            case SENSORS_ENCODER_COUNT_LEFT:
                setSensorInteger(SENSOR_IDX_encoderCountLeft, serialConnection.readSignedWord());
                break;
            case SENSORS_ENCODER_COUNT_RIGHT:
                setSensorInteger(SENSOR_IDX_encoderCountRight, serialConnection.readUnsignedWord());
                break;
            case SENSORS_LIGHT_BUMPER:
                setSensorInteger(SENSOR_IDX_lightBumper, serialConnection.readUnsignedByte());
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_LEFT:
                setSensorInteger(SENSOR_IDX_lightBumpSignalLeft, serialConnection.readUnsignedWord());
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_LEFT_FRONT:
                setSensorInteger(SENSOR_IDX_lightBumpSignalLeftFront, serialConnection.readUnsignedWord());
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_LEFT_CENTER:
                setSensorInteger(SENSOR_IDX_lightBumpSignalLeftCenter,
                        serialConnection.readUnsignedWord());
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_RIGHT:
                setSensorInteger(SENSOR_IDX_lightBumpSignalRight, serialConnection.readUnsignedWord());
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_RIGHT_FRONT:
                setSensorInteger(SENSOR_IDX_lightBumpSignalRightFront,
                        serialConnection.readUnsignedWord());
                break;
            case SENSORS_LIGHT_BUMP_SIGNAL_RIGHT_CENTER:
                setSensorInteger(SENSOR_IDX_lightBumpSignalRightCenter,
                        serialConnection.readUnsignedWord());
                break;
            case SENSORS_MOTOR_CURRENT_LEFT:
                setSensorInteger(SENSOR_IDX_motorCurrentLeft, serialConnection.readUnsignedWord());
                break;
            case SENSORS_MOTOR_CURRENT_RIGHT:
                setSensorInteger(SENSOR_IDX_motorCurrentRight, serialConnection.readUnsignedWord());
                break;
            case SENSORS_MAIN_BRUSH_MOTOR_CURRENT:
                serialConnection.readSignedWord();
                break;
            case SENSORS_SIDE_BRUSH_MOTOR_CURRENT:
                serialConnection.readSignedWord();
                break;
            case SENSORS_STASIS:
                dataByte = serialConnection.readUnsignedByte();
                setSensorBoolean(SENSOR_IDX_stasis, (dataByte & 0x1) != 0);
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

    private void setSensorBoolean(int sensorId, boolean value) {
        sensorValues[sensorId] = value ? 1 : 0;
    }

    private void setSensorInteger(int sensorId, int value) {
        sensorValues[sensorId] = value;
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

    public synchronized void waitButtonPressed(boolean spotButton, boolean beep)
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
            if (gotButtonDown && spotButton && !isSpotButtonDown()) {
                break;
            }
            if (gotButtonDown && !spotButton && !isAdvanceButtonDown()) {
                break;
            }
            if (spotButton && isSpotButtonDown()) {
                gotButtonDown = true;
            }
            if (!spotButton && isAdvanceButtonDown()) {
                gotButtonDown = true;
            }
            SystemClock.sleep(noteDuration);
            totalTimeWaiting += noteDuration;
            if (totalTimeWaiting > 500) {
                if (beep) {
                    playSong(0);
                }
                ledsToggle(false, spotButton);
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
