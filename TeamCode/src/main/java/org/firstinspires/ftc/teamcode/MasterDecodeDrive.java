package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MasterDecodeDrive {
    double SPEED = 1.0;
    double intakeSpeed = 1.0;
    double launchSpeed = 1.0;
    double drumSpeed = 0.8;
    double DEADZONE = 0.2;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;
    DcMotor intake;
    DcMotor launcherRight;
    DcMotor launcherLeft;
    CRServo drumServo;
    Servo kicker;
    MecanumDrive drive;
    Telemetry telemetry;


    /**
     * Constructs a master decode drive.
     * @param hardwareMap Finds all of the hardware components from the Hardware Map
     * @param tel For any functions that want to post to telemetry (must call telemetry.update() separately)
     */
    public MasterDecodeDrive(HardwareMap hardwareMap, Telemetry tel){
        telemetry = tel;
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        intake = hardwareMap.dcMotor.get("intake");
        launcherRight = hardwareMap.dcMotor.get("launcherRight");
        launcherLeft = hardwareMap.dcMotor.get("launcherLeft");
        drumServo = hardwareMap.crservo.get("drumServo");
        kicker = hardwareMap.servo.get("kicker");
        kicker.setPosition(1);
        drive = new MecanumDrive(leftFront, rightFront, leftRear, rightRear, 1, true, false, true, false);
        DcMotor[] motors = {leftFront, rightFront, leftRear, rightRear, launcherLeft, launcherRight};
        for(DcMotor motor : motors){
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }
    /**
     * Constructs a master decode drive.
     * @param hardwareMap Finds all of the hardware components from the Hardware Map
     * @param tel For any functions that want to post to telemetry (must call telemetry.update() separately)
     * @param s Drive Speed
     * @param intakeS Intake Speed
     * @param launchS Launch Speed
     * @param drumS Drum Speed
     * @param dz Deadzone for driving inputs
     */
    public MasterDecodeDrive(HardwareMap hardwareMap, Telemetry tel, double s, double intakeS, double launchS, double drumS, double dz){
        this(hardwareMap, tel);
        SPEED = s;
        intakeSpeed = intakeS;
        launchSpeed = launchS;
        drumSpeed = drumS;
        DEADZONE = dz;
    }
    private int reverse(boolean do_it){
        if(do_it) return -1;
        return 1;
    }
    /**
     * Set the launch speed
     * @param newSpeed Between 0 and 1
     */
    public void setLaunchSpeed(double newSpeed){
        launchSpeed = newSpeed;
    }

    /**
     * Get the launch speed
     * @return Launch Speed
     */
    public double getLaunchSpeed(){
        return launchSpeed;
    }

    /**
     * Set the drum speed
     * @param newSpeed Between 0 and 1
     */
    public void setDrumSpeed(double newSpeed){
        drumSpeed = newSpeed;
    }

    /**
     * Get the drum speed.
     * @return Drum Speed
     */
    public double getDrumSpeed(){
        return drumSpeed;
    }

    /**
     * Set the intake speed
     * @param newSpeed Between 0 and 1
     */
    public void setIntakeSpeed(double newSpeed){
        intakeSpeed = newSpeed;
    }

    /**
     * Get the intake speed
     * @return Intake Speed
     */
    public double getIntakeSpeed(){
        return intakeSpeed;
    }

    /**
     * Get the deadzone value.
     * @return Deadzone
     */
    public double getDeadzone(){
        return DEADZONE;
    }
    /**
     * Runs all of the drive commands using the left and right sticks of a gamepad.
     * @param gamepad1 Which gamepad's left and right sticks?
     */
    public void runDrive(Gamepad gamepad1){
        // QOL #1: Set the Speed
        double speed = 1-(gamepad1.right_trigger/1.4);
        if(speed<=0.1){
            speed=.1;
        }
        // QOL #2: Reverse Controls
        if(gamepad1.left_trigger>.3){
            speed=speed*(-1);
        }
        drive.setDriveSpeed(speed);
        if (Math.abs(gamepad1.right_stick_x) >.4) { // If the right stick is being moved sufficiently
            if(speed<0){
                speed=Math.abs(speed);
                drive.setDriveSpeed(speed);
            }
            // Tank Turn
            if(gamepad1.right_stick_x>.4) {
                drive.turnRightTank(1*gamepad1.right_stick_x);
            }
            if(gamepad1.right_stick_x<-.4) {
                drive.turnLeftTank(1*-gamepad1.right_stick_x);
            }
        } else if(Math.abs(gamepad1.left_stick_x)>.4 || Math.abs(gamepad1.left_stick_y)>.4) { // If the left stick is being moved sufficiently
            // Forward/Back
            if (gamepad1.left_stick_y < -.4 && Math.abs(gamepad1.left_stick_x) < .4) {
                drive.moveForward(1*-gamepad1.left_stick_y);
            }
            if (gamepad1.left_stick_y > .4 && Math.abs(gamepad1.left_stick_x) < .4) {
                drive.moveBackward(1*gamepad1.left_stick_y);
            }
            // Left/Right
            if (gamepad1.left_stick_x < -.4 && Math.abs(gamepad1.left_stick_y) < .4) {
                drive.moveRight(1*-gamepad1.left_stick_x);
            }
            if (gamepad1.left_stick_x > .4 && Math.abs(gamepad1.left_stick_y) < .4) {
                drive.moveLeft(1*gamepad1.left_stick_x);
            }
            // Diagonals
            if (gamepad1.left_stick_y < -.4 && gamepad1.left_stick_x > .4) {
                drive.diagonalRightFront(1);
            }
            if (gamepad1.left_stick_y < -.4 && gamepad1.left_stick_x < -.4) {
                drive.diagonalLeftFront(1);
            }
            if (gamepad1.left_stick_y > .4 && gamepad1.left_stick_x > .4) {
                drive.diagonalRightBack(1);
            }
            if (gamepad1.left_stick_y > .4 && gamepad1.left_stick_x < -.4) {
                drive.diagonalLeftBack(1);
            }
        } else { // If the sticks aren't being touched
            drive.stop();
        }
        telemetry.addData("Drive Speed", speed);
    }
    /**
     * Runs the intake if the boolean is true.
     * @param run Whether or not the intake should be active
     * @param r Reverse Direction
     */
    public void runIntake(boolean run, boolean r){
        if(run){
            intake.setPower(intakeSpeed*reverse(r));
        }else{
            intake.setPower(0);
        }
        telemetry.addData("Intake Speed", intakeSpeed);
    }

    /**
     * Runs the outtake if the boolean is true
     * @param run Whether or not the outtake should be active
     * @param r Reverse Direction
     */
    public void runOuttake(boolean run, boolean r){
        if(run) {
            launcherRight.setPower(launchSpeed);
            launcherLeft.setPower(launchSpeed);
        } else {
            launcherRight.setPower(0);
            launcherLeft.setPower(0);
        }
        telemetry.addData("Launcher Speed", launchSpeed);
    }

    /**
     * Runs the drum based on the booleans
     * @param runForward Run the drum in the forward direction
     * @param runBackward Run the drum in the backward direction
     */
    public void runDrum(boolean runForward, boolean runBackward){
        if (runForward){
            drumServo.setPower(drumSpeed);
        }
        else if(runBackward){
            drumServo.setPower(-drumSpeed);
        } else {
            drumServo.setPower(0);
        }
        telemetry.addData("Drum Speed", drumSpeed);
    }

    /**
     * Make the kicker kick (position 1)
     */
    public void deployKicker(){
        setKickerPosition(0.6);
    }
    /**
     * Sets the kicker to a specific position
     * @param position Position between 0 and 1
     */
    public void setKickerPosition(double position){
        kicker.setPosition(position);
    }

    /**
     * Make the kicker recall (position 0)
     */
    public void returnKicker(){
        setKickerPosition(1);
    }
}
