package org.jointheleague.erik.cleverrobot;

import org.jointheleague.erik.irobot.IRobotCreateAdapter;
import org.jointheleague.erik.irobot.IRobotCreateInterface;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

public class Pilot extends IRobotCreateAdapter {

    // The following measurements are taken from the interface specification
    private static final double WHEEL_DISTANCE = 235.0; //in mm
    private static final double WHEEL_DIAMETER = 72.0; //in mm
    private static final double ENCODER_COUNTS_PER_REVOLUTION = 508.8;

    private final Dashboard dashboard;
//    public UltraSonicSensors sonar;

    private int startLeft;
    private int startRight;
    private int countsToGoWheelLeft;
    private int countsToGoWheelRight;
    private int directionLeft;
    private int directionRight;
    private static final int STRAIGHT_SPEED = 200;
    private static final int TURN_SPEED = 100;

    private int currentCommand = 0;

    public Pilot(IRobotCreateInterface iRobot, Dashboard dashboard, IOIO ioio)
            throws ConnectionLostException {
        super(iRobot);
//        sonar = new UltraSonicSensors(ioio);
        this.dashboard = dashboard;
    }

    /* This method is executed when the robot first starts up. */
    public void initialize() throws ConnectionLostException {
        dashboard.log(dashboard.getString(R.string.hello));
        //what would you like me to do, Clever Human?
        nextCommand();

    }

    /* This method is called repeatedly. */
    public void loop() throws ConnectionLostException {
        if (checkDone()) {
            nextCommand();
        }
    }

    /**
     * This method determines where to go next. This is a very simple Tortoise-like
     * implementation, but a more advanced implementation could take into account
     * sensory input, maze mapping, and other into account.
     *
     * @throws ConnectionLostException
     */
    private void nextCommand() throws ConnectionLostException {
        if (currentCommand < 8) {
            if (currentCommand % 2 == 0) {
                goStraight(1000);
            } else {
                turnRight(90);
            }
            currentCommand++;
        } else if (currentCommand == 8) {
            dashboard.log("Shutting down... Bye!");
            stop();
            closeConnection();
        }
    }

    /**
     * Moves the robot in a straight line
     *
     * @param distance the distance to go in mm
     */
    private void goStraight(int distance) throws ConnectionLostException {
        dashboard.log("Going straight");
        countsToGoWheelLeft = (int) (distance * ENCODER_COUNTS_PER_REVOLUTION
                / (Math.PI * WHEEL_DIAMETER));
        countsToGoWheelRight = countsToGoWheelLeft;
        directionLeft = 1;
        directionRight = 1;
        readSensors(SENSORS_GROUP_ID101);
        startLeft = directionLeft * getEncoderCountLeft();
        startRight = directionRight * getEncoderCountRight();
        driveDirect(directionLeft * STRAIGHT_SPEED, directionRight * STRAIGHT_SPEED);
    }


    /**
     * Turns in place rightwards.
     *
     * @param degrees the number of degrees to turn.
     */
    private void turnRight(int degrees) throws ConnectionLostException {
        dashboard.log("Turning right");
        countsToGoWheelRight = (int) (degrees * WHEEL_DISTANCE * ENCODER_COUNTS_PER_REVOLUTION
                / (360.0 * WHEEL_DIAMETER));
        countsToGoWheelLeft = countsToGoWheelRight;
        directionLeft = 1;
        directionRight = -1;
        readSensors(SENSORS_GROUP_ID101);
        startLeft = directionLeft * getEncoderCountLeft();
        startRight = directionRight * getEncoderCountRight();
        driveDirect(directionLeft * TURN_SPEED, directionRight * TURN_SPEED);
    }

    /**
     * Turns in place leftwards.
     *
     * @param degrees the number of degrees to turn.
     */
    private void turnLeft(int degrees) throws ConnectionLostException {
        dashboard.log("Turning left");
        countsToGoWheelRight = (int) (degrees * WHEEL_DISTANCE * ENCODER_COUNTS_PER_REVOLUTION
                / (360.0 * WHEEL_DIAMETER));
        countsToGoWheelLeft = countsToGoWheelRight;
        directionLeft = -1;
        directionRight = 1;
        readSensors(SENSORS_GROUP_ID101);
        startLeft = directionLeft * getEncoderCountLeft();
        startRight = directionRight * getEncoderCountRight();
        driveDirect(directionLeft * TURN_SPEED, directionRight * TURN_SPEED);
    }


    /**
     * Checks if the last command has been completed.
     *
     * @return true if the last command has been completed
     * @throws ConnectionLostException
     */
    private boolean checkDone() throws ConnectionLostException {
        readSensors(SENSORS_GROUP_ID101);
        int countLeft = directionLeft * getEncoderCountLeft();
        int countRight = directionRight * getEncoderCountRight();
        boolean done = false;
        if ((countLeft - startLeft & 0xFFFF) > countsToGoWheelLeft
                || (countRight - startRight & 0xFFFF) > countsToGoWheelRight) {
            driveDirect(0, 0);
            done = true;
        }
        return done;
    }


}
