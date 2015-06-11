package org.jointheleague.erik.irobot;

import ioio.lib.api.exception.ConnectionLostException;

/**
 * A concrete class that provides a default implementation of the
 * IRobotCreateInterface. It is a convenience class intended to be extended in
 * order to define customized implementations of the IRobotCreateInterface.
 */
public class IRobotCreateAdapter implements IRobotCreateInterface {

    /**
     * The decorated instance. All calls on the methods of the
     * IRobotCreateInterface are forwarded to this instance.
     */
    protected final IRobotCreateInterface delegate;

    /**
     * Makes a new instance from an IRobotCreateInterface implementation
     * instance.
     *
     * @param delegate a non-null instance of IRobotCreateInterface to which
     *                 method calls are forwarded.
     */
    public IRobotCreateAdapter(IRobotCreateInterface delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Argument must be non-null");
        }
        this.delegate = delegate;
    }


    public void drive(int velocity, int radius)
            throws ConnectionLostException {
        delegate.drive(velocity, radius);
    }

    public void driveDirect(int leftVelocity, int rightVelocity)
            throws ConnectionLostException {
        delegate.driveDirect(leftVelocity, rightVelocity);
    }

    public void full() throws ConnectionLostException {
        delegate.full();
    }

    public int getAngle() {
        return delegate.getAngle();
    }

    public int getBatteryCapacity() {
        return delegate.getBatteryCapacity();
    }

    public int getBatteryCharge() {
        return delegate.getBatteryCharge();
    }

    public int getBatteryTemperature() {
        return delegate.getBatteryTemperature();
    }

    public int getChargingState() {
        return delegate.getChargingState();
    }

    public int getCliffSignalLeftFront() {
        return delegate.getCliffSignalLeftFront();
    }

    public int getCliffSignalRightFront() {
        return delegate.getCliffSignalRightFront();
    }

    public int getCliffSignalLeft() {
        return delegate.getCliffSignalLeft();
    }

    public int getCliffSignalRight() {
        return delegate.getCliffSignalRight();
    }

    public int getCurrent() {
        return delegate.getCurrent();
    }

    public int getDistance() {
        return delegate.getDistance();
    }

    public int getInfraredByte() {
        return delegate.getInfraredByte();
    }

    public int getInfraredByteLeft() {
        return delegate.getInfraredByteLeft();
    }

    public int getInfraredByteRight() {
        return delegate.getInfraredByteRight();
    }

    public int getOiMode() {
        return delegate.getOiMode();
    }

    public int getRequestedVelocityLeft() {
        return delegate.getRequestedVelocityLeft();
    }

    public int getRequestedRadius() {
        return delegate.getRequestedRadius();
    }

    public int getRequestedVelocityRight() {
        return delegate.getRequestedVelocityRight();
    }

    public int getRequestedVelocity() {
        return delegate.getRequestedVelocity();
    }

    public int getSongNumber() {
        return delegate.getSongNumber();
    }

    public int getVoltage() {
        return delegate.getVoltage();
    }

    public int getWallSignal() {
        return delegate.getWallSignal();
    }

    public boolean isBumpLeft() {
        return delegate.isBumpLeft();
    }

    public boolean isBumpRight() {
        return delegate.isBumpRight();
    }

    public boolean isCliffFrontLeft() {
        return delegate.isCliffFrontLeft();
    }

    public boolean isCliffFrontRight() {
        return delegate.isCliffFrontRight();
    }

    public boolean isCliffLeft() {
        return delegate.isCliffLeft();
    }

    public boolean isCliffRight() {
        return delegate.isCliffRight();
    }

    public boolean isHomeBaseChargerAvailable() {
        return delegate.isHomeBaseChargerAvailable();
    }

    public boolean isInternalChargerAvailable() {
        return delegate.isInternalChargerAvailable();
    }

    public boolean isLeftWheelOvercurrent() {
        return delegate.isLeftWheelOvercurrent();
    }

    public boolean isWheelOvercurrentMainBrush() {
        return delegate.isWheelOvercurrentMainBrush();
    }

    public boolean isSpotButtonDown() {
        return delegate.isSpotButtonDown();
    }

    public boolean isRightWheelOvercurrent() {
        return delegate.isRightWheelOvercurrent();
    }

    public boolean isSongPlaying() {
        return delegate.isSongPlaying();
    }

    public boolean isVirtualWall() {
        return delegate.isVirtualWall();
    }

    public boolean isWall() {
        return delegate.isWall();
    }

    public boolean isWheelDropLeft() {
        return delegate.isWheelDropLeft();
    }

    public boolean isWheelDropRight() {
        return delegate.isWheelDropRight();
    }

    public void leds(int powerColor, int powerIntensity, boolean spotLedOn) throws ConnectionLostException {
        delegate.leds(powerColor, powerIntensity, spotLedOn);
    }


    public void playSong(int songNumber) throws ConnectionLostException {
        delegate.playSong(songNumber);
    }


    public void readSensors(int sensorId) throws ConnectionLostException {
        delegate.readSensors(sensorId);
    }


    public void reset() throws ConnectionLostException {
        delegate.reset();
    }

    public void safe() throws ConnectionLostException {
        delegate.safe();
    }


    public void song(int songNumber, int[] notesAndDurations) throws ConnectionLostException {
        delegate.song(songNumber, notesAndDurations);
    }

    public void song(int songNumber, int[] notesAndDurations, int startIndex, int length) throws ConnectionLostException {
        delegate.song(songNumber, notesAndDurations, startIndex, length);
    }

    public void stop() throws ConnectionLostException {
        delegate.stop();
    }

    public void waitButtonPressed(boolean beep) throws ConnectionLostException {
        delegate.waitButtonPressed(beep);
    }

    public void closeConnection() {
        delegate.closeConnection();
    }

    @Override
    public int getEncoderCountLeft() {
        return delegate.getEncoderCountLeft();
    }

    @Override
    public int getEncoderCountRight() {
        return delegate.getEncoderCountRight();
    }

    @Override
    public int getMotorCurrentLeft() {
        return delegate.getMotorCurrentLeft();
    }

    @Override
    public int getMotorCurrentRight() {
        return delegate.getMotorCurrentRight();
    }

    @Override
    public boolean isStasis() {
        return delegate.isStasis();
    }

    @Override
    public boolean isLightBump() {
        return delegate.isLightBump();
    }

    @Override
    public int[] getLightBumps() {
        return delegate.getLightBumps();
    }
}
