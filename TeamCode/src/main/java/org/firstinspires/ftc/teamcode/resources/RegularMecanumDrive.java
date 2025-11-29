package org.firstinspires.ftc.teamcode.resources;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RegularMecanumDrive {
    // All the motors
    private final DcMotor frontLeft, frontRight, backLeft, backRight;
    // The factor by which all motor speeds should be multiplied
    private double driveSpeed;

    // Used to convert the factor booleans to numbers, which are more useful
    private int factor(boolean f) {
        if(f){return 1;}
        return -1;
    }

    /**
     * Constructs an object in charge of all driving, for chassis that use Mecanum-Wheel Driving.
     * Needs the Motors, a drive-speed multiplier, and the "Factor."
     * If the motors spin the wrong way, set the corresponding factor to False. Otherwise, set it to True.
     * @param hardwareMap Finds the motors from the hardware map.
     * @param ds Drive-speed Multiplier. Multiplies all driving by this number.
     */
    public RegularMecanumDrive(HardwareMap hardwareMap, double ds) {
        frontLeft = hardwareMap.dcMotor.get("leftFront");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight = hardwareMap.dcMotor.get("rightFront");
        backLeft = hardwareMap.dcMotor.get("leftRear");
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight = hardwareMap.dcMotor.get("rightRear");
        driveSpeed = ds;
        DcMotor[] motors = {frontLeft, frontRight, backLeft, backRight};
        for (DcMotor motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }
    public void runDrive(Gamepad gamepad, double speed, boolean reverseSwitch){
        // QOL #2: Reverse Controls
        if (reverseSwitch) {
            speed = speed * (-driveSpeed);
        }
        setDriveSpeed(speed);
        if (abs(gamepad.right_stick_x) > .4) { // If the right stick is being moved sufficiently
            if (speed < 0) {
                speed = abs(speed);
                setDriveSpeed(speed);
            }
            // Tank Turn
            if (gamepad.right_stick_x > .4) {
                turnRightTank(driveSpeed * gamepad.right_stick_x);
            }
            if (gamepad.right_stick_x < -.4) {
                turnLeftTank(driveSpeed * -gamepad.right_stick_x);
            }
        } else if (abs(gamepad.left_stick_x) > .4 || abs(gamepad.left_stick_y) > .4) { // If the left stick is being moved sufficiently
            // Forward/Back
            if (gamepad.left_stick_y < -.4 && abs(gamepad.left_stick_x) < .4) {
                moveForward(driveSpeed * -gamepad.left_stick_y);
            }
            if (gamepad.left_stick_y > .4 && abs(gamepad.left_stick_x) < .4) {
                moveBackward(driveSpeed * gamepad.left_stick_y);
            }
            // Left/Right
            if (gamepad.left_stick_x < -.4 && abs(gamepad.left_stick_y) < .4) {
                moveRight(driveSpeed * -gamepad.left_stick_x);
            }
            if (gamepad.left_stick_x > .4 && abs(gamepad.left_stick_y) < .4) {
                moveLeft(driveSpeed * gamepad.left_stick_x);
            }
            // Diagonals
            if (gamepad.left_stick_y < -.4 && gamepad.left_stick_x > .4) {
                diagonalRightFront(driveSpeed);
            }
            if (gamepad.left_stick_y < -.4 && gamepad.left_stick_x < -.4) {
                diagonalLeftFront(driveSpeed);
            }
            if (gamepad.left_stick_y > .4 && gamepad.left_stick_x > .4) {
                diagonalRightBack(driveSpeed);
            }
            if (gamepad.left_stick_y > .4 && gamepad.left_stick_x < -.4) {
                diagonalLeftBack(driveSpeed);
            }
        } else { // If the sticks aren't being touched
            stop();
        }
    }
    /**
     * Drives the robot forward.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void moveForward(double multiplier) {
        frontLeft.setPower(driveSpeed);
        backLeft.setPower(multiplier);
        frontRight.setPower(multiplier);
        backRight.setPower(multiplier);
    }
    /**
     * Drives the robot backward.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void moveBackward(double multiplier) {
        frontLeft.setPower(-multiplier);
        backLeft.setPower(-multiplier);
        frontRight.setPower(-multiplier);
        backRight.setPower(-multiplier);
    }
    /**
     * Drives the robot left.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void moveLeft(double multiplier) {
        frontLeft.setPower(multiplier);
        backLeft.setPower(-multiplier);
        frontRight.setPower(-multiplier);
        backRight.setPower(multiplier);
    }
    /**
     * Drives the robot right.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void moveRight(double multiplier) {
        frontLeft.setPower(-multiplier);
        backLeft.setPower(multiplier);
        frontRight.setPower(multiplier);
        backRight.setPower(-multiplier);
    }
    /**
     * Drives the robot diagonally, to the Front-Right.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void diagonalRightFront(double multiplier) {
        frontLeft.setPower(multiplier);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(multiplier);
    }
    /**
     * Drives the robot diagonally, to the Front-Left.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void diagonalLeftFront(double multiplier) {
        frontLeft.setPower(0);
        backLeft.setPower(multiplier);
        frontRight.setPower(multiplier);
        backRight.setPower(0);
    }
    /**
     * Drives the robot diagonally, to the Back-Right.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void diagonalRightBack(double multiplier) {
        frontLeft.setPower(0);
        backLeft.setPower(-multiplier);
        frontRight.setPower(-multiplier);
        backRight.setPower(0);
    }
    /**
     * Drives the robot diagonally, to the Back-Left.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void diagonalLeftBack(double multiplier) {
        frontLeft.setPower(-multiplier);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(-multiplier);
    }
    /**
     * Turns the robot to the right, pivoting on the middle (clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnRightTank(double multiplier) {
        frontLeft.setPower(multiplier);
        backLeft.setPower(multiplier);
        frontRight.setPower(-multiplier);
        backRight.setPower(-multiplier);
    }
    /**
     * Turns the robot to the left, pivoting on the middle (counter-clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnLeftTank(double multiplier) {
        frontLeft.setPower(-multiplier);
        backLeft.setPower(-multiplier);
        frontRight.setPower(multiplier);
        backRight.setPower(multiplier);
    }
    /**
     * Turns the robot to the right, pivoting on the rear-axis (clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnRightRear(double multiplier) {
        frontLeft.setPower(multiplier);
        backLeft.setPower(0);
        frontRight.setPower(-multiplier);
        backRight.setPower(0);
    }
    /**
     * Turns the robot to the left, pivoting on the rear-axis (counter-clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnLeftRear(double multiplier) {
        frontLeft.setPower(-multiplier);
        backLeft.setPower(0);
        frontRight.setPower(multiplier);
        backRight.setPower(0);
    }
    /**
     * Turns the robot to the right, pivoting on the front-axis (clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnRightFront(double multiplier) {
        frontLeft.setPower(0);
        backLeft.setPower(multiplier);
        frontRight.setPower(0);
        backRight.setPower(-multiplier);
    }
    /**
     * Turns the robot to the left, pivoting on the Front-axis (counter-clockwise)
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void turnLeftFront(double multiplier) {
        frontLeft.setPower(0);
        backLeft.setPower(-multiplier);
        frontRight.setPower(0);
        backRight.setPower(multiplier);
    }
    /**
     * Turns the robot to the right and moves forward.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void cornerRightFront(double multiplier) {
        frontLeft.setPower(multiplier);
        backLeft.setPower(multiplier);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
    /**
     * Turns the robot to the left and moves forward.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void cornerLeftFront(double multiplier) {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(multiplier);
        backRight.setPower(multiplier);
    }
    /**
     * Turns the robot to the right and moves back.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void cornerRightBack(double multiplier) {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(-multiplier);
        backRight.setPower(-multiplier);
    }
    /**
     * Turns the robot to the left and moves forward.
     * @param multiplier Multiplier for how faster the drive should happen.
     */
    public void cornerLeftBack(double multiplier) {
        frontLeft.setPower(-multiplier);
        backLeft.setPower(-multiplier);
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
