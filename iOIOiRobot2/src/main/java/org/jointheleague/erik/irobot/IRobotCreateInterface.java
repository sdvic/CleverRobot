package org.jointheleague.erik.irobot;

import ioio.lib.api.exception.ConnectionLostException;

/**
 * A high level interface to the iRobot series of Roomba/Create robots. It
 * encapsulates most of the commands and sensors specified in <a href=
 * "http://www.irobot.com/~/media/MainSite/PDFs/About/STEM/Create/iRobot_Roomba_600_Open_Interface_Spec_0512.pdf"
 * > iRobot Create 2 Open Interface (OI)</a>. It is recommended reading that
 * document in order to get a better understanding of how to work with the
 * Create.
 */
public interface IRobotCreateInterface {


    /**
     * Identifies the sensor Group 0. This group includes the sensors identified
     * by the following ids:
     * <p/>
     * SENSORS_BUMPS_AND_WHEEL_DROPS, <br>
     * SENSORS_WALL, <br>
     * SENSORS_CLIFF_LEFT, <br>
     * SENSORS_CLIFF_FRONT_LEFT, <br>
     * SENSORS_CLIFF_FRONT_RIGHT, <br>
     * SENSORS_CLIFF_RIGHT, <br>
     * SENSORS_VIRTUAL_WALL, <br>
     * SENSORS_LOWS_SIDE_DRIVER_AND_WHEEL_OVERCURRENTS, <br>
     * SENSORS_DIRT_DETECT, <br>
     * SENSORS_DUMMY2, <br>
     * SENSORS_INFRARED_BYTE, <br>
     * SENSORS_BUTTONS, <br>
     * SENSORS_DISTANCE, <br>
     * SENSORS_ANGLE, <br>
     * SENSORS_CHARGING_STATE, <br>
     * SENSORS_VOLTAGE, <br>
     * SENSORS_CURRENT, <br>
     * SENSORS_BATTERY_TEMPERATURE, <br>
     * SENSORS_BATTERY_CHARGE, <br>
     * SENSORS_BATTERY_CAPACITY
     */
    int SENSORS_GROUP_ID0 = 0;
    /**
     * Identifies the sensor Group 1. This group includes the sensors identified
     * by the following ids:
     * <p/>
     * SENSORS_BUMPS_AND_WHEEL_DROPS, <br>
     * SENSORS_WALL, <br>
     * SENSORS_CLIFF_LEFT, <br>
     * SENSORS_CLIFF_FRONT_LEFT, <br>
     * SENSORS_CLIFF_FRONT_RIGHT, <br>
     * SENSORS_CLIFF_RIGHT, <br>
     * SENSORS_VIRTUAL_WALL, <br>
     * SENSORS_LOWS_SIDE_DRIVER_AND_WHEEL_OVERCURRENTS, <br>
     * SENSORS_DIRT_DETECT, <br>
     * SENSORS_DUMMY22
     */
    int SENSORS_GROUP_ID1 = 1;
    /**
     * Identifies the sensor Group 2. This group includes the sensors identified
     * by the following ids:
     * <p/>
     * SENSORS_INFRARED_BYTE, <br>
     * SENSORS_BUTTONS, <br>
     * SENSORS_DISTANCE, <br>
     * SENSORS_ANGLE
     */
    int SENSORS_GROUP_ID2 = 2;
    /**
     * Identifies the sensor Group 3. This group includes the sensors identified
     * by the following ids:
     * <p/>
     * SENSORS_CHARGING_STATE, SENSORS_VOLTAGE, <br>
     * SENSORS_CURRENT, <br>
     * SENSORS_BATTERY_TEMPERATURE, <br>
     * SENSORS_BATTERY_CHARGE, <br>
     * SENSORS_BATTERY_CAPACITY
     */
    int SENSORS_GROUP_ID3 = 3;
    /**
     * Identifies the sensor Group 4. This group includes the sensors identified
     * by the following ids:
     * <p/>
     * SENSORS_WALL_SIGNAL, <br>
     * SENSORS_CLIFF_SIGNAL_LEFT, <br>
     * SENSORS_CLIFF_SIGNAL_LEFT_FRONT, <br>
     * SENSORS_CLIFF_SIGNAL_RIGHT_FRONT, <br>
     * SENSORS_CLIFF_SIGNAL_RIGHT, <br>
     * SENSORS_CARGO_BAY_DIGITAL_INPUTS, <br>
     * SENSORS_CARGO_BAY_ANALOG_SIGNAL, <br>
     * SENSORS_CHARGING_SOURCES_AVAILABLE
     */
    int SENSORS_GROUP_ID4 = 4;
    /**
     * Identifies the sensor Group 5. This group includes the sensors identified
     * by the following ids:
     * <p/>
     * SENSORS_OI_MODE, <br>
     * SENSORS_SONG_NUMBER, <br>
     * SENSORS_SONG_PLAYING, <br>
     * SENSORS_NUMBER_OF_STREAM_PACKETS, <br>
     * SENSORS_REQUESTED_VELOCITY, <br>
     * SENSORS_REQUESTED_RADIUS, <br>
     * SENSORS_REQUESTED_VELOCITY_RIGHT, <br>
     * SENSORS_REQUESTED_VELOCITY_LEFT
     */
    int SENSORS_GROUP_ID5 = 5;
    /**
     * Identifies the sensor Group 6. This group includes the sensors identified
     * by the following ids:
     * <p/>
     * SENSORS_BUMPS_AND_WHEEL_DROPS, <br>
     * SENSORS_WALL, <br>
     * SENSORS_CLIFF_LEFT, <br>
     * SENSORS_CLIFF_FRONT_LEFT, <br>
     * SENSORS_CLIFF_FRONT_RIGHT, <br>
     * SENSORS_CLIFF_RIGHT, <br>
     * SENSORS_VIRTUAL_WALL, <br>
     * SENSORS_LOWS_SIDE_DRIVER_AND_WHEEL_OVERCURRENTS, <br>
     * SENSORS_DIRT_DETECT, <br>
     * SENSORS_DUMMY2, <br>
     * SENSORS_INFRARED_BYTE, <br>
     * SENSORS_BUTTONS, <br>
     * SENSORS_DISTANCE, <br>
     * SENSORS_ANGLE, <br>
     * SENSORS_CHARGING_STATE, <br>
     * SENSORS_VOLTAGE, <br>
     * SENSORS_CURRENT, <br>
     * SENSORS_BATTERY_TEMPERATURE, <br>
     * SENSORS_BATTERY_CHARGE, <br>
     * SENSORS_BATTERY_CAPACITY, <br>
     * SENSORS_WALL_SIGNAL, <br>
     * SENSORS_CLIFF_SIGNAL_LEFT, <br>
     * SENSORS_CLIFF_SIGNAL_LEFT_FRONT, <br>
     * SENSORS_CLIFF_SIGNAL_RIGHT_FRONT, <br>
     * SENSORS_CLIFF_SIGNAL_RIGHT, <br>
     * SENSORS_CARGO_BAY_DIGITAL_INPUTS, <br>
     * SENSORS_CARGO_BAY_ANALOG_SIGNAL, <br>
     * SENSORS_CHARGING_SOURCES_AVAILABLE, <br>
     * SENSORS_OI_MODE, <br>
     * SENSORS_SONG_NUMBER, <br>
     * SENSORS_SONG_PLAYING, <br>
     * SENSORS_NUMBER_OF_STREAM_PACKETS, <br>
     * SENSORS_REQUESTED_VELOCITY, <br>
     * SENSORS_REQUESTED_RADIUS, <br>
     * SENSORS_REQUESTED_VELOCITY_RIGHT, <br>
     * SENSORS_REQUESTED_VELOCITY_LEFT
     */
    int SENSORS_GROUP_ID6 = 6;

    /**
     * Identifies the sensors group 100. This group includes all sensors.
     */
    int SENSORS_GROUP_ID100 = 100;

    /**
     * Identifies the sensors group 101. This group includes sensors:
     * <p/>
     * SENSORS_ENCODER_COUNT_LEFT <br>
     * SENSORS_ENCODER_COUNT_RIGHT <br>
     * SENSORS_LIGHT_BUMPER <br>
     * SENSORS_LIGHT_BUMP_SIGNAL_LEFT<br>
     * SENSORS_LIGHT_BUMP_SIGNAL_LEFT_FRONT<br>
     * SENSORS_LIGHT_BUMP_SIGNAL_LEFT_CENTER<br>
     * SENSORS_LIGHT_BUMP_SIGNAL_RIGHT_CENTER<br>
     * SENSORS_LIGHT_BUMP_SIGNAL_RIGHT_FRONT<br>
     * SENSORS_LIGHT_BUMP_SIGNAL_RIGHT<br>
     * SENSORS_INFRARED_BYTE_LEFT<br>
     * SENSORS_INFRARED_BYTE_RIGHT<br>
     * SENSORS_MOTOR_CURRENT_LEFT<br>
     * SENSORS_MOTOR_CURRENT_RIGHT<br>
     * SENSORS_MAIN_BRUSH_MOTOR_CURRENT<br>
     * SENSORS_SIDE_BRUSH_MOTOR_CURRENT<br>
     * SENSORS_STASIS
     */
    int SENSORS_GROUP_ID101 = 101;

    /**
     * Identifies the sensors group 106. This group includes:
     * <p/>
     * SENSORS_LIGHT_BUMP_SIGNAL_LEFT<br>
     * SENSORS_LIGHT_BUMP_SIGNAL_LEFT_FRONT<br>
     * SENSORS_LIGHT_BUMP_SIGNAL_LEFT_CENTER<br>
     * SENSORS_LIGHT_BUMP_SIGNAL_RIGHT_CENTER<br>
     * SENSORS_LIGHT_BUMP_SIGNAL_RIGHT_FRONT<br>
     * SENSORS_LIGHT_BUMP_SIGNAL_RIGHT<br>
     */
    int SENSORS_GROUP_ID106 = 106;

    /**
     * Identifies the sensors group 107. This group includes sensors:
     * <p/>
     * SENSORS_MOTOR_CURRENT_LEFT<br>
     * SENSORS_MOTOR_CURRENT_RIGHT<br>
     * SENSORS_MAIN_BRUSH_MOTOR_CURRENT<br>
     * SENSORS_SIDE_BRUSH_MOTOR_CURRENT<br>
     * SENSORS_STASIS
     */
    int SENSORS_GROUP_ID107 = 107;

    /**
     * Identifies the left and right bump sensor and left, right and caster
     * wheel drop sensors.
     *
     * @see #isBumpLeft()
     * @see #isBumpRight()
     * @see #isWheelDropLeft()
     * @see #isWheelDropRight()
     */
    int SENSORS_BUMPS_AND_WHEEL_DROPS = 7;
    /**
     * Identifies the Wall sensor.
     *
     * @see #isWall()
     */
    int SENSORS_WALL = 8;
    /**
     * Identifies the Left Cliff sensor.
     *
     * @see #isCliffLeft()
     */
    int SENSORS_CLIFF_LEFT = 9;
    /**
     * Identifies the Front Left Cliff sensor.
     *
     * @see #isCliffFrontLeft()
     */
    int SENSORS_CLIFF_FRONT_LEFT = 10;
    /**
     * Identifies the Front Right Cliff sensor.
     *
     * @see #isCliffFrontRight()
     */
    int SENSORS_CLIFF_FRONT_RIGHT = 11;
    /**
     * Identifies the Right Cliff sensor.
     *
     * @see #isCliffRight()
     */
    int SENSORS_CLIFF_RIGHT = 12;
    /**
     * Identifies the Virtual Wall sensor.
     *
     * @see #isVirtualWall()
     */
    int SENSORS_VIRTUAL_WALL = 13;
    /**
     * Identifies the low side driver and wheel overcurrent sensors.
     *
     * @see #isWheelOvercurrentMainBrush()
     * @see #isLeftWheelOvercurrent()
     * @see #isRightWheelOvercurrent()
     */
    int SENSORS_WHEEL_OVERCURRENTS = 14;
    /**
     * Identifies a 1 byte unused sensor.
     */
    int SENSORS_DIRT_DETECT = 15;
    /**
     * Identifies a 1 byte dummy sensor.
     */
    int SENSORS_DUMMY2 = 16;
    /**
     * Identifies the Infrared sensor.
     *
     * @see #getInfraredByte()
     */
    int SENSORS_INFRARED_BYTE = 17;
    /**
     * Identifies the button sensors.
     *
     * @see #isSpotButtonDown()
     */
    int SENSORS_BUTTONS = 18;
    /**
     * Identifies the Distance sensor.
     *
     * @see #getDistance()
     */
    int SENSORS_DISTANCE = 19;
    /**
     * Identifies the Angle sensor.
     *
     * @see #getAngle()
     */
    int SENSORS_ANGLE = 20;
    /**
     * Identifies the charging state sensor.
     *
     * @see #getChargingState()
     */
    int SENSORS_CHARGING_STATE = 21;
    /**
     * Identifies the voltage sensor.
     *
     * @see #getVoltage()
     */
    int SENSORS_VOLTAGE = 22;
    /**
     * Identifies the Current sensor.
     *
     * @see #getCurrent()
     */
    int SENSORS_CURRENT = 23;
    /**
     * Identifies the Battery Temperature sensor.
     *
     * @see #getBatteryTemperature()
     */
    int SENSORS_BATTERY_TEMPERATURE = 24;
    /**
     * Identifies the Battery Charge sensor.
     *
     * @see #getBatteryCharge()
     */
    int SENSORS_BATTERY_CHARGE = 25;
    /**
     * Identifies the Battery Capacity sensor.
     *
     * @see #getBatteryCapacity()
     */
    int SENSORS_BATTERY_CAPACITY = 26;
    /**
     * Identifies the Wall Signal sensor.
     *
     * @see #getWallSignal()
     */
    int SENSORS_WALL_SIGNAL = 27;
    /**
     * Identifies the Cliff Left Signal sensor.
     *
     * @see #getCliffSignalLeft()
     */
    int SENSORS_CLIFF_SIGNAL_LEFT = 28;
    /**
     * Identifies the Cliff Front Left Signal sensor.
     *
     * @see #getCliffSignalLeftFront()
     */
    int SENSORS_CLIFF_SIGNAL_LEFT_FRONT = 29;
    /**
     * Identifies the Cliff Front Right Signal sensor.
     *
     * @see #getCliffSignalRightFront()
     */
    int SENSORS_CLIFF_SIGNAL_RIGHT_FRONT = 30;
    /**
     * Identifies the Cliff Right Signal sensor.
     *
     * @see #getCliffSignalRight()
     */
    int SENSORS_CLIFF_SIGNAL_RIGHT = 31;
    /**
     * Deprecated. 1 byte
     */
    int SENSORS_CARGO_BAY_DIGITAL_INPUTS = 32;
    /**
     * Deprecated. 2 bytes
     */
    int SENSORS_CARGO_BAY_ANALOG_SIGNAL = 33;
    /**
     * Identifies the Available Charging Sources sensors.
     *
     * @see #isHomeBaseChargerAvailable()
     * @see #isInternalChargerAvailable()
     */
    int SENSORS_CHARGING_SOURCES_AVAILABLE = 34;
    /**
     * Identifies the OI Mode sensor
     *
     * @see #getOiMode()
     */
    int SENSORS_OI_MODE = 35;
    /**
     * Identifies the Song Number sensor.
     *
     * @see #getSongNumber()
     */
    int SENSORS_SONG_NUMBER = 36;
    /**
     * Identifies the Song Playing sensor.
     *
     * @see #isSongPlaying()
     */
    int SENSORS_SONG_PLAYING = 37;
    /**
     * Identifies the Stream Packets sensor.
     */
    int SENSORS_NUMBER_OF_STREAM_PACKETS = 38;
    /**
     * Identifies the Requested Velocity sensor.
     *
     * @see #getRequestedVelocity()
     */
    int SENSORS_REQUESTED_VELOCITY = 39;
    /**
     * Identifies the Requested Radius sensor.
     *
     * @see #getRequestedRadius()
     */
    int SENSORS_REQUESTED_RADIUS = 40;
    /**
     * Identifies the Requested Right Velocity.
     *
     * @see #getRequestedVelocityRight()
     */
    int SENSORS_REQUESTED_VELOCITY_RIGHT = 41;
    /**
     * Identifies the Requested Left Velocity sensor.
     *
     * @see #getRequestedVelocityLeft()
     */
    int SENSORS_REQUESTED_VELOCITY_LEFT = 42;

    /**
     * Identifies the left encoder
     */
    int SENSORS_ENCODER_COUNT_LEFT = 43;

    /**
     * Identifies the right encoder
     */
    int SENSORS_ENCODER_COUNT_RIGHT = 44;

    /**
     * Identifies the light bumper sensor.
     */
    int SENSORS_LIGHT_BUMPER = 45;

    /**
     * Identifies the light bump left signal strength sensor.
     */
    int SENSORS_LIGHT_BUMP_SIGNAL_LEFT = 46;

    /**
     * Identifies the light bump front left signal strength sensor.
     */
    int SENSORS_LIGHT_BUMP_SIGNAL_LEFT_FRONT = 47;

    /**
     * Identifies the light bump center left signal strength sensor.
     */
    int SENSORS_LIGHT_BUMP_SIGNAL_LEFT_CENTER = 48;

    /**
     * Identifies the light bump center right signal strength sensor.
     */
    int SENSORS_LIGHT_BUMP_SIGNAL_RIGHT_CENTER = 49;

    /**
     * Identifies the light bump front right signal strength sensor.
     */
    int SENSORS_LIGHT_BUMP_SIGNAL_RIGHT_FRONT = 50;

    /**
     * Identifies the light bump right signal strength sensor.
     */
    int SENSORS_LIGHT_BUMP_SIGNAL_RIGHT = 51;

    /**
     * Identifies the left infrared sensor.
     *
     * @see #getInfraredByteLeft()
     */
    int SENSORS_INFRARED_BYTE_LEFT = 52;
    /**
     * Identifies the right infrared sensor.
     *
     * @see #getInfraredByteRight()
     */
    int SENSORS_INFRARED_BYTE_RIGHT = 53;

    /**
     * Identifies the left motor current sensor.
     */
    int SENSORS_MOTOR_CURRENT_LEFT = 54;

    /**
     * Identifies the right motor current sensor.
     */
    int SENSORS_MOTOR_CURRENT_RIGHT = 55;

    /**
     * Not used. 2 bytes
     */
    int SENSORS_MAIN_BRUSH_MOTOR_CURRENT = 56;

    /**
     * Not used. 2 bytes
     */
    int SENSORS_SIDE_BRUSH_MOTOR_CURRENT = 57;

    /**
     * Identifies the stasis sensor.
     */
    int SENSORS_STASIS = 58;


    /**
     * Controls the Create's drive wheels. Larger values for the radius makes
     * the Create drive straighter, while the smaller values make the Create
     * turn more. The radius is measured from the center of the turning circle
     * to the center of Create. A positive velocity and a positive radius make
     * the Create drive forward while turning toward the left. A negative radius
     * makes the Create turn toward the right. Special cases for the radius make
     * Create turn in place or drive straight, as specified below. A negative
     * velocity makes Create drive backward. Available in modes: Safe or Full
     * <p/>
     * NOTE: Internal and environmental restrictions may prevent Create from
     * accurately carrying out some drive commands. For example, it may not be
     * possible for Create to drive at full speed in an arc with a large radius
     * of curvature.
     * <p/>
     * Special cases of radius:
     * <ul>
     * <li>Straight 32768 or 32767 = hex 8000 or 7FFF<br>
     * <li>Turn in place clockwise = hex FFFF <br>
     * <li>Turn in place counter-clockwise = hex 0001
     * </ul>
     *
     * @param velocity the average velocity of the wheels in mm/s. Range is -500 to
     *                 500
     * @param radius   the turn radius in mm. Range is -2000 to 2000<br>
     * @throws ConnectionLostException
     */
    void drive(int velocity, int radius) throws ConnectionLostException;

    /**
     * This method lets you control the forward and backward motion of the
     * Create by specifying the velocity of each wheel independently. A positive
     * velocity makes that wheel drive forward, while a negative velocity makes
     * it drive backward. Available in modes: Safe or Full
     *
     * @param leftVelocity  Left wheel velocity in mm/s. Range is -500 to 500
     * @param rightVelocity Right wheel velocity in mm/s. Range is -500 to 500
     * @throws ConnectionLostException
     */
    void driveDirect(int leftVelocity, int rightVelocity)
            throws ConnectionLostException;

    /**
     * This method gives you complete control over the Create by putting the OI
     * into Full mode, and turning off the cliff, wheel-drop and internal
     * charger safety features. That is, in Full mode, the Create executes any
     * command that you send it, even if the internal charger is plugged in, or
     * the robot senses a cliff or wheel drop. Available in modes: Passive,
     * Safe, or Full <br>
     * <p/>
     * Note: Before invoking this method a connection to the Create must have
     * been established.
     *
     * @throws ConnectionLostException
     */
    void full() throws ConnectionLostException;

    /**
     * Gets the angle in degrees that the Create has turned from the time before
     * last that the angle sensor was read to the last time the angle sensor was
     * read. Counter-clockwise angles are positive and clockwise angles are
     * negative. If the value is not polled frequently enough, it is capped at
     * its minimum or maximum, which are -32768 and 32767 respectively.
     * <p/>
     * <b>NOTE:</b> The Create uses wheel encoders to measure distance and
     * angle. If the wheels slip, the actual distance or angle traveled may
     * differ from the Create's measurements.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return The angle in degrees that the Create has turned.
     * @see #readSensors(int sensorId)
     */
    int getAngle();

    /**
     * Gets the estimated charge capacity of the Create's battery in
     * milliamphours (mAh). Note that this value is inaccurate if you are using
     * the alkaline battery pack.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return The estimated charge capacity of the Create's battery in
     * milliamphours (mAh).
     * @see #readSensors(int sensorId)
     */
    int getBatteryCapacity();

    /**
     * The current charge of Create's battery in milliamp-hours (mAh). The
     * charge value decreases as the battery is depleted during running and
     * increases when the battery is charged. <br>
     * Note that this value will not be accurate if you are using the alkaline
     * battery pack.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return The battery capacity in mAh.
     * @see #readSensors(int sensorId)
     */
    int getBatteryCharge();

    /**
     * The temperature of Create's battery in degrees Celsius. <br>
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return The battery temperature in degrees Celsius.
     * @see #readSensors(int sensorId)
     */
    int getBatteryTemperature();


    /**
     * This code indicates Create's current charging state. The state is encoded
     * as follows:
     * <ul>
     * <li>0 -- Not charging
     * <li>1 -- Reconditioning charging
     * <li>2 -- Full charging
     * <li>3 -- Trickle chraging
     * <li>4 -- Waiting
     * <li>
     * 5 -- Fault condition
     * </ul>
     * <p/>
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return The Create's charging state.
     * @see #readSensors(int sensorId)
     */
    int getChargingState();

    /**
     * Gets the strength of the front left cliff sensor's signal. The strength
     * is returned as a value between 0 and 4095.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return the strength of the front left cliff sensor signal.
     * @see #readSensors(int sensorId)
     */
    int getCliffSignalLeftFront();

    /**
     * Gets the strength of the front right cliff sensor's signal. The strength
     * is returned as a value between 0 and 4095.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return the strength of the front left cliff sensor signal.
     * @see #readSensors(int sensorId)
     */
    int getCliffSignalRightFront();

    /**
     * Gets the strength of the left cliff sensor's signal. The strength is
     * returned as a value between 0 and 4095. <b>NOTE:</b> This method returns
     * a locally stored value previously read from the Create. It is the
     * client's responsibility to read the sensor values from the Create prior
     * to calling this method in order to ensure fresh values.
     *
     * @return the strength of the left cliff sensor signal.
     * @see #readSensors(int sensorId)
     */
    int getCliffSignalLeft();

    /**
     * Gets the strength of the right cliff sensor's signal. The strength is
     * returned as a value between 0 and 4095. <b>NOTE:</b> This method returns
     * a locally stored value previously read from the Create. It is the
     * client's responsibility to read the sensor values from the Create prior
     * to calling this method in order to ensure fresh values.
     *
     * @return the strength of the right cliff sensor signal.
     * @see #readSensors(int sensorId)
     */
    int getCliffSignalRight();

    /**
     * Gets the current, in milliAmps, flowing into or out of the Create's
     * battery. Negative currents indicate that the current is flowing out of
     * the battery, as during normal running. Positive currents indicate that
     * the current is flowing into the battery, as during charging.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return The current in mA
     * @see #readSensors(int sensorId)
     */
    int getCurrent();

    /**
     * Gets the distance in mm that the Create has travelled from the time
     * before last that the distance sensor was read to the last time the
     * distance sensor was read. This is the same as the sum of the distance
     * traveled by both wheels divided by two. Positive values indicate travel
     * in the forward direction; negative values indicate travel in the reverse
     * direction. If the value is not polled frequently enough, it is capped at
     * its minimum or maximum, which are -32768 amd 32767 respectively.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return the travelled distance in mm
     * @see #readSensors(int sensorId)
     */
    int getDistance();

    /**
     * Gets the left encoder count. This number will roll over to 0 after it
     * passes 65535.There are 508.8 counts per revolution. The wheel diameter is
     * 72mm and the distance between the wheels is 235mm.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return the left encoder count
     */
    int getEncoderCountLeft();

    /**
     * Gets the right encoder count. This number will roll over to 0 after it
     * passes 65535. There are 508.8 counts per revolution. The wheel diameter
     * is 72mm and the distance between the wheels is 235mm.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return the right encoder count
     */
    int getEncoderCountRight();

    /**
     * Gets the value of the IR byte received by the Create. The value is
     * encoded as follows:
     * <p/>
     * Remote control values:
     * <ul>
     * <li>129 Left
     * <li>130 Forward
     * <li>131 Right
     * <li>132 Spot
     * <li>133 Max
     * <li>134 Small
     * <li>135 Medium
     * <li>136 Large / Clean
     * <li>137 PAUSE
     * <li>138 Power
     * <li>139 arc-forward-left
     * <li>140 arc-forward-right
     * <li>141 drive-stop
     * </ul>
     * Remote scheduling values:
     * <ul>
     * <li>142 Send All
     * <li>143 Seek Dock
     * </ul>
     * Home base values
     * <ul>
     * <li>240 Reserved
     * <li>248 Red Buoy
     * <li>244 Green Buoy
     * <li>242 Force Field
     * <li>252 Red Buoy and Green Buoy
     * <li>250 Red Buoy and Force Field
     * <li>246 Green Buoy and Force Field
     * <li>254 Red Buoy, Green Buoy and Force Field
     * <li>255 No value received
     * </ul>
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return One of the values above.
     * @see #readSensors(int sensorId)
     */
    int getInfraredByte();

    /**
     * Gets the value of the IR byte received by the Create. The value is
     * encoded as follows:
     * <p/>
     * Remote control values:
     * <ul>
     * <li>129 Left
     * <li>130 Forward
     * <li>131 Right
     * <li>132 Spot
     * <li>133 Max
     * <li>134 Small
     * <li>135 Medium
     * <li>136 Large / Clean
     * <li>137 PAUSE
     * <li>138 Power
     * <li>139 arc-forward-left
     * <li>140 arc-forward-right
     * <li>141 drive-stop
     * </ul>
     * Remote scheduling values:
     * <ul>
     * <li>142 Send All
     * <li>143 Seek Dock
     * </ul>
     * Home base values
     * <ul>
     * <li>240 Reserved
     * <li>248 Red Buoy
     * <li>244 Green Buoy
     * <li>242 Force Field
     * <li>252 Red Buoy and Green Buoy
     * <li>250 Red Buoy and Force Field
     * <li>246 Green Buoy and Force Field
     * <li>254 Red Buoy, Green Buoy and Force Field
     * <li>255 No value received
     * </ul>
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return One of the values above.
     * @see #readSensors(int sensorId)
     */
    int getInfraredByteLeft();

    /**
     * Gets the value of the IR byte received by the Create. The value is
     * encoded as follows:
     * <p/>
     * Remote control values:
     * <ul>
     * <li>129 Left
     * <li>130 Forward
     * <li>131 Right
     * <li>132 Spot
     * <li>133 Max
     * <li>134 Small
     * <li>135 Medium
     * <li>136 Large / Clean
     * <li>137 PAUSE
     * <li>138 Power
     * <li>139 arc-forward-left
     * <li>140 arc-forward-right
     * <li>141 drive-stop
     * </ul>
     * Remote scheduling values:
     * <ul>
     * <li>142 Send All
     * <li>143 Seek Dock
     * </ul>
     * Home base values
     * <ul>
     * <li>240 Reserved
     * <li>248 Red Buoy
     * <li>244 Green Buoy
     * <li>242 Force Field
     * <li>252 Red Buoy and Green Buoy
     * <li>250 Red Buoy and Force Field
     * <li>246 Green Buoy and Force Field
     * <li>254 Red Buoy, Green Buoy and Force Field
     * <li>255 No value received
     * </ul>
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return One of the values above.
     * @see #readSensors(int sensorId)
     */
    int getInfraredByteRight();

    /**
     * Gets the current being drawn by the left wheel motor in milliAmpere (mA).
     *
     * @return the current in the range 0 - 65535 mA
     */
    int getMotorCurrentLeft();

    /**
     * Returns an array of length 6 where the values are from 0 to 5:
     * <p/>
     * <ul>
     * <li>Light Bump Left
     * <li>Light Bump Front Left
     * <li>Light Bump Center Left
     * <li>Light Bump Center Right
     * <li>Light Bump Front Right
     * <li>Light Bump Right
     * </ul>
     * <p/>
     * The returned values are the signal strengths, where the maximum strength
     * is 4095.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return the light bumps array
     */
    int[] getLightBumps();

    /**
     * Gets the current being drawn by the right wheel motor in milliAmpere
     * (mA).
     *
     * @return the current in the range 0 - 65535 mA
     */
    int getMotorCurrentRight();

    /**
     * Gets the Create's OI mode. The returned value is encoded as follows:
     * <ul>
     * <li>0 - Off
     * <li>1 - Passive
     * <li>2 - Safe
     * <li>3 - Full
     * <ul>
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return The Create's OI mode.
     * @see #readSensors(int sensorId)
     */
    int getOiMode();

    /**
     * Gets the turn radius in mm most recently requested using the drive()
     * method.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return The most recently requested turn radius in mm.
     * @see #drive(int velocity, int radius)
     * @see #readSensors(int sensorId)
     */
    int getRequestedRadius();

    /**
     * Gets the average wheel velocity in mm/s most recently requested using the
     * driveDirect() or drive() method.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return The most recently requested average wheel velocity in mm/s.
     * @see #driveDirect(int leftVelocity, int rightVelocity)
     * @see #drive(int velocity, int radius)
     * @see #readSensors(int sensorId)
     */
    int getRequestedVelocity();

    /**
     * Gets the left wheel velocity in mm/s most recently requested using the
     * driveDirect() or drive() method.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return The most recently requested left wheel velocity in mm/s
     * @see #driveDirect(int leftVelocity, int rightVelocity)
     * @see #drive(int velocity, int radius)
     * @see #readSensors(int sensorId)
     */
    int getRequestedVelocityLeft();

    /**
     * Gets the right wheel velocity in mm/s most recently requested using the
     * driveDirect() or drive() method.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return The most recently requested right wheel velocity in mm/s.
     * @see #driveDirect(int leftVelocity, int rightVelocity)
     * @see #drive(int velocity, int radius)
     * @see #readSensors(int sensorId)
     */
    int getRequestedVelocityRight();

    /**
     * Gets the currently selected OI song song number.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return the song number
     * @see #readSensors(int sensorId)
     */
    int getSongNumber();

    /**
     * Gets the voltage of Create's battery in millivolts (mV).
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return the battery voltage in mV
     * @see #readSensors(int sensorId)
     */
    int getVoltage();

    /**
     * Returns true when the robot is making forward progress and false when it
     * is not. This always returns false when the robot is turning, driving
     * backward, or not driving.
     *
     * @return the stasis value
     */
    boolean isStasis();

    /**
     * Gets the strength of the wall sensor's signal as a number between 0 and
     * 4095.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return the wall signal's strength
     * @see #readSensors(int sensorId)
     */
    int getWallSignal();

    /**
     * Gets the state of the left bumper.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true is the left bumper is depressed.
     * @see #readSensors(int sensorId)
     */
    boolean isBumpLeft();

    /**
     * Gets the state of the right bumper.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true is the right bumper is depressed.
     * @see #readSensors(int sensorId)
     */
    boolean isBumpRight();


    boolean isCliffFrontLeft();

    /**
     * Gets the state of the front right cliff sensor.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if a cliff has been detected
     * @see #readSensors(int sensorId)
     */
    boolean isCliffFrontRight();

    /**
     * Gets the state of the left cliff sensor.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if a cliff has been detected
     * @see #readSensors(int sensorId)
     */
    boolean isCliffLeft();

    /**
     * Gets the state of the right cliff sensor.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if a cliff has been detected
     * @see #readSensors(int sensorId)
     */
    boolean isCliffRight();

    /**
     * Gets the availability of the home base charger.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if the home base charger is available
     * @see #readSensors(int sensorId)
     */
    boolean isHomeBaseChargerAvailable();

    /**
     * Gets the availability of the internal charger.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if the internal charger is available
     * @see #readSensors(int sensorId)
     */
    boolean isInternalChargerAvailable();

    /**
     * Gets the state of the left wheel overcurrent sensor.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if too much current (> 1A) is requested.
     * @see #readSensors(int sensorId)
     */
    boolean isLeftWheelOvercurrent();

    /**
     * Gets the state of the light bump.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true a light bump has occurred.
     * @see #readSensors(int sensorId)
     */
    boolean isLightBump();

    /**
     * Gets the state of the Low Side Driver 2 overcurrent sensor.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if too much current (> 1.6A) is requested.
     * @see #readSensors(int sensorId)
     */
    boolean isWheelOvercurrentMainBrush();

    /**
     * Gets the state of the Spot button
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if the Spot button is depressed.
     * @see #readSensors(int sensorId)
     */
    boolean isSpotButtonDown();

    /**
     * Gets the state of the right wheel overcurrent sensor.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if too much current (> 1A) is requested.
     * @see #readSensors(int sensorId)
     */
    boolean isRightWheelOvercurrent();

    /**
     * Gets the state of the OI song player.
     * <p/>
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if the Create is playing a song.
     * @see #readSensors(int sensorId)
     * @see #playSong(int)
     */
    boolean isSongPlaying();

    /**
     * Gets the state of the virtual wall sensor.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if virtual wall is detected.
     * @see #readSensors(int sensorId)
     */
    boolean isVirtualWall();

    /**
     * Gets the state of the wall sensor.
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if the wall is detected.
     * @see #readSensors(int sensorId)
     */
    boolean isWall();

    /**
     * Gets the state of the left wheel sensor
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if the left wheel has dropped.
     * @see #readSensors(int sensorId)
     */
    boolean isWheelDropLeft();

    /**
     * Gets the state of the right wheel sensor
     * <p/>
     * <b>NOTE:</b> This method returns a locally stored value previously read
     * from the Create. It is the client's responsibility to read the sensor
     * values from the Create prior to calling this method in order to ensure
     * fresh values.
     *
     * @return true if the right wheel has dropped.
     * @see #readSensors(int sensorId)
     */
    boolean isWheelDropRight();


    /**
     * This method controls the LEDs on the Create. The state of the Play and
     * Advance LEDs is specified by true or false. The Power LED is specified by
     * two values: one for the color and the other for the intensity. Available
     * in modes: Safe or Full
     * <p/>
     * Advance and Play LEDs use green LEDs. false = off, true = on <br>
     * Power uses a bicolor (red/green) LED. The intensity and color of this LED
     * can be controlled with 8-bit resolution.
     *
     * @param powerColor     Power LED Color (0 - 255) 0 = green, 255 = red. Intermediate
     *                       values are intermediate colors (orange, yellow, etc).
     * @param powerIntensity Power LED Intensity (0 - 255) 0 = off, 255 = full intensity.
     *                       Intermediate values are intermediate intensities.
     * @param spotLedOn      when true turn Play LED on.
     * @throws ConnectionLostException
     */
    void leds(int powerColor, int powerIntensity, boolean spotLedOn)
            throws ConnectionLostException;


    /**
     * Selects a song to play from the songs previously added to the Create
     * using one of the song() methods. This method does nothing if a song is
     * already playing. Note that the {@link #isSongPlaying() isSongPlaying()}
     * method can be used to check whether the Create is ready to accept this
     * command. Available in modes: Safe or Full.
     *
     * @param songNumber the number of the song the Create is to play.
     * @throws ConnectionLostException
     * @see #song(int, int[])
     * @see #song(int, int[], int, int)
     */
    void playSong(int songNumber) throws ConnectionLostException;


    /**
     * Retrieves one or more sensor values from the Create and stores the values
     * locally. The values can later be accessed using any of the access
     * methods. The sensors that are to be read are specified using a sensor id.
     * There are 43 different sensor Ids; see the SENSORS_* constants. Each
     * identifies a specific sensor or group of sensors.
     *
     * @param sensorId One of the SENSORS_* constants
     * @throws ConnectionLostException
     */
    void readSensors(int sensorId) throws ConnectionLostException;


    /**
     * Exits OI mode and resets the Roomba to its default state.
     *
     * @throws ConnectionLostException
     */
    void reset() throws ConnectionLostException;

    /**
     * Puts the OI into Safe mode, enabling user control of the Create. It turns
     * off all LEDs. The OI can be in Passive, Safe, or Full mode to accept this
     * command.
     *
     * @throws ConnectionLostException
     */
    void safe() throws ConnectionLostException;


    /**
     * This method lets you specify a song that you can play at a later time. Up
     * to 16 songs can be specified. Each song is associated with a song number.
     * Each song can contain up to sixteen notes. Each note is associated with a
     * note number (in the range of (31 - 127) that uses MIDI note definitions
     * and a duration that is specified in fractions of a second. The Create
     * considers all musical notes outside the range of (31 - 127) as rest
     * notes, and will make no sound during the duration of those notes.
     * Available in modes: Passive, Safe, or Full <br>
     *
     * @param songNumber        The song number associated with the specific song. Must be in
     *                          the range 0 through 15. If you send a second Song command,
     *                          using the same song number, the old song is overwritten.
     * @param notesAndDurations An int array of even length. Every even index represents a
     *                          note, every odd index is the duration of the preceding note in
     *                          units of 1/64 second. Example: a half-second long musical note
     *                          has a duration value of 32. Each entry in the array must be a
     *                          number between 0 and 255 and the total length of the array
     *                          must be 32 or less (max 16 notes).
     * @throws ConnectionLostException
     * @see #playSong(int)
     * @see #song(int, int[])
     * @see #song(int, int[], int, int)
     * @see <a href=../../../../resources/Create%20Open%20Interface_v2.pdf>
     * Create Open Interface_v2.pdf</a>
     */
    void song(int songNumber, int[] notesAndDurations)
            throws ConnectionLostException;

    /**
     * This method lets you specify a song that you can play at a later time. Up
     * to 16 songs can be specified. Each song is associated with a song number.
     * Each song can contain up to sixteen notes. Each note is associated with a
     * note number (in the range of (31 - 127) that uses MIDI note definitions
     * and a duration that is specified in fractions of a second. The Create
     * considers all musical notes outside the range of (31 - 127) as rest
     * notes, and will make no sound during the duration of those notes.
     * Available in modes: Passive, Safe, or Full <br>
     *
     * @param songNumber        The song number associated with the specific song. Must be in
     *                          the range 0 through 15. On a second invocation of the song()
     *                          method, using the same song number, the old song is
     *                          overwritten.
     * @param notesAndDurations An int array of even length. Every even index represents a
     *                          note, every odd index is the duration of the preceding note in
     *                          units of 1/64 second. Example: a half-second long musical note
     *                          has a duration value of 32. Each entry in the array must be a
     *                          number between 0 and 255.
     * @param startIndex        an index into an int array specifying the first note
     * @param length            an even number less than or equal to 32 (max 16 notes).
     * @throws ConnectionLostException
     * @see #playSong(int)
     * @see #song(int, int[])
     * @see <a href=../../../../Create%20Open%20Interface_v2.pdf> Create Open
     * Interface_v2.pdf</a>
     */
    void song(int songNumber, int[] notesAndDurations, int startIndex, int length)
            throws ConnectionLostException;


    /**
     * Exits OI mode.
     *
     * @throws ConnectionLostException
     */
    void stop() throws ConnectionLostException;

    /**
     * Pause Java execution until Spot button is pressed. While waiting, blink the LED
     * next to the desired button.
     *
     * @param beep       If true, plays a tune.
     * @throws ConnectionLostException
     */
    void waitButtonPressed(boolean beep)
            throws ConnectionLostException;

    /**
     * Closes the serial connection to the Create
     */
    void closeConnection();

}
