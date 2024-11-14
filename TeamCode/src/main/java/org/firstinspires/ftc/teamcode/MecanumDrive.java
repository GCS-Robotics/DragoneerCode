package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MecanumDrive {
    // All the motors
    private final DcMotor frontLeft, frontRight, backLeft, backRight;
    // The factor by which all motor speeds should be multiplied
    private double driveSpeed;
    // Used in case motor spins opposite way as intended. Set as -1 or 1
    private final int fLFactor, fRFactor, bLFactor, bRFactor;

    // Used to convert the factor booleans to numbers, which are more useful
    private int factor(boolean f) {
        if(f){return 1;}
        return -1;
    }

    /**
     * Constructs an object in charge of all driving, for chassis that use Mecanum-Wheel Driving.
     * Needs the Motors, a drive-speed multiplier, and the "Factor."
     * If the motors spin the wrong way, set the corresponding factor to False. Otherwise, set it to True.
     * @param fl Front-Left Motor
     * @param fr Front-Right Motor
     * @param bl Back-Left Motor
     * @param br Back-Right Motor
     * @param ds Drive-speed Multiplier. Multiplies all driving by this number.
     * @param flf Front-Left Motor's Factor
     * @param frf Front-Right Motor's Factor
     * @param blf Back-Left Motor's Factor
     * @param brf Back-Right Motor's Factor
     */
    public MecanumDrive(DcMotor fl, DcMotor fr, DcMotor bl, DcMotor br, double ds, boolean flf, boolean frf, boolean blf, boolean brf) {
        frontLeft=fl;
        frontRight=fr;
        backLeft=bl;
        backRight=br;
        driveSpeed=ds;
        fLFactor=factor(flf);
        fRFactor=factor(frf);
        bLFactor=factor(blf);
        bRFactor=factor(brf);
    }

    /**
     * Drives the robot forward.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void moveForward(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(p*fLFactor);
        backLeft.setPower(p*bLFactor);
        frontRight.setPower(p*fRFactor);
        backRight.setPower(p*bRFactor);
    }
    /**
     * Drives the robot backward.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void moveBackward(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(-p*fLFactor);
        backLeft.setPower(-p*bLFactor);
        frontRight.setPower(-p*fRFactor);
        backRight.setPower(-p*bRFactor);
    }
    /**
     * Drives the robot left.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void moveLeft(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(p*fLFactor);
        backLeft.setPower(-p*bLFactor);
        frontRight.setPower(-p*fRFactor);
        backRight.setPower(p*bRFactor);
    }
    /**
     * Drives the robot right.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void moveRight(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(-p*fLFactor);
        backLeft.setPower(p*bLFactor);
        frontRight.setPower(p*fRFactor);
        backRight.setPower(-p*bRFactor);
    }
    /**
     * Drives the robot diagonally, to the Front-Right.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void diagonalRightFront(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(p*fLFactor);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(p*bRFactor);
    }
    /**
     * Drives the robot diagonally, to the Front-Left.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void diagonalLeftFront(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(0);
        backLeft.setPower(p*bLFactor);
        frontRight.setPower(p*fRFactor);
        backRight.setPower(0);
    }
    /**
     * Drives the robot diagonally, to the Back-Right.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void diagonalRightBack(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(0);
        backLeft.setPower(-p*bLFactor);
        frontRight.setPower(-p*fRFactor);
        backRight.setPower(0);
    }
    /**
     * Drives the robot diagonally, to the Back-Left.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void diagonalLeftBack(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(-p*fLFactor);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(-p*bRFactor);
    }
    /**
     * Turns the robot to the right, pivoting on the middle (clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnRightTank(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(p*fLFactor);
        backLeft.setPower(-p*bLFactor);
        frontRight.setPower(p*fRFactor);
        backRight.setPower(-p*bRFactor);
    }
    /**
     * Turns the robot to the left, pivoting on the middle (counter-clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnLeftTank(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(-p*fLFactor);
        backLeft.setPower(p*bLFactor);
        frontRight.setPower(-p*fRFactor);
        backRight.setPower(p*bRFactor);
    }
    /**
     * Turns the robot to the right, pivoting on the rear-axis (clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnRightRear(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(p*fLFactor);
        backLeft.setPower(0);
        frontRight.setPower(-p*fRFactor);
        backRight.setPower(0);
    }
    /**
     * Turns the robot to the left, pivoting on the rear-axis (counter-clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnLeftRear(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(-p*fLFactor);
        backLeft.setPower(0);
        frontRight.setPower(p*fRFactor);
        backRight.setPower(0);
    }
    /**
     * Turns the robot to the right, pivoting on the front-axis (clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnRightFront(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(0);
        backLeft.setPower(p*bLFactor);
        frontRight.setPower(0);
        backRight.setPower(-p*bRFactor);
    }
    /**
     * Turns the robot to the left, pivoting on the Front-axis (counter-clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnLeftFront(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(0);
        backLeft.setPower(-p*bLFactor);
        frontRight.setPower(0);
        backRight.setPower(p*bRFactor);
    }
    /**
     * Turns the robot to the right and moves forward.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void cornerRightFront(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(p*fLFactor);
        backLeft.setPower(p*bLFactor);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
    /**
     * Turns the robot to the left and moves forward.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void cornerLeftFront(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(p*fRFactor);
        backRight.setPower(p*bRFactor);
    }
    /**
     * Turns the robot to the right and moves back.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void cornerRightBack(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(-p*fRFactor);
        backRight.setPower(-p*bRFactor);
    }
    /**
     * Turns the robot to the left and moves forward.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void cornerLeftBack(double multiplier) {
        double p=multiplier*driveSpeed;
        frontLeft.setPower(-p*fLFactor);
        backLeft.setPower(-p*bLFactor);
        frontRight.setPower(0);
        backRight.setPower(0);
    }

    /**
     * Sets the power of all motors to 0.
     */
    public void stop() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
    /**
    * Sets the Driving Speed to whatever double you input.
     * @param newSpeed Sets the new speed of the robot.
     */
    public void setDriveSpeed(double newSpeed){
        driveSpeed=newSpeed;
    }
}
