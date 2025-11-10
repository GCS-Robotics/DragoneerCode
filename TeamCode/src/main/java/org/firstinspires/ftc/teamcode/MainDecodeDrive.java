package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MainDecodeDrive {
    double SPEED;
    double intakeSpeed = 1.0;
    double launchSpeed = 1.0;
    double drumSpeed = 0.8;
    double DEADZONE;
    final int ROTATION_TICK = 537;
    int[] balls = new int[3]; // Ball at balls[1] is the ball that should be primed to fire
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;
    DcMotor intake;
    DcMotor launcherRight;
    DcMotor launcherLeft;
    DcMotor drumRotor;
    Servo kicker;
    MecanumDrive drive;
    Telemetry telemetry;

    ColorSensor BallColor;
    boolean launching = false;

    /**
     * Constructs a master decode drive.
     *
     * @param hardwareMap Finds all of the hardware components from the Hardware Map
     * @param tel         For any functions that want to post to telemetry (must call telemetry.update() separately)
     */
    public MainDecodeDrive(HardwareMap hardwareMap, Telemetry tel) {
        this(hardwareMap, tel, 1.0, 1.0, 1.0, 0.8, 0.2);
    }

    /**
     * Constructs a master decode drive.
     *
     * @param hardwareMap Finds all of the hardware components from the Hardware Map
     * @param tel         For any functions that want to post to telemetry (must call telemetry.update() separately)
     * @param s           Drive Speed
     * @param intakeS     Intake Speed
     * @param launchS     Launch Speed
     * @param drumS       Drum Speed
     * @param dz          Deadzone for driving inputs
     */
    public MainDecodeDrive(HardwareMap hardwareMap, Telemetry tel, double s, double intakeS, double launchS, double drumS, double dz) {
        telemetry = tel;
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        intake = hardwareMap.dcMotor.get("intake");
        launcherRight = hardwareMap.dcMotor.get("launcherRight");
        launcherLeft = hardwareMap.dcMotor.get("launcherLeft");
        drumRotor = hardwareMap.dcMotor.get("drumRotor");
        kicker = hardwareMap.servo.get("kicker");
        BallColor = hardwareMap.colorSensor.get("BallColor");
        kicker.setPosition(1);
        drive = new MecanumDrive(leftFront, rightFront, leftRear, rightRear, s, true, false, false, false);
        DcMotor[] motors = {leftFront, rightFront, leftRear, rightRear, launcherLeft, launcherRight, drumRotor};
        for (DcMotor motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        drumRotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drumRotor.setTargetPosition(0);
        drumRotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        drumRotor.setPower(drumSpeed);
        SPEED = s;
        intakeSpeed = intakeS;
        launchSpeed = launchS;
        drumSpeed = drumS;
        DEADZONE = dz;
    }

    private int reverse(boolean do_it) {
        if (do_it) return -1;
        return 1;
    }

    /**
     * Set the launch speed
     *
     * @param newSpeed Between 0 and 1
     */
    public void setLaunchSpeed(double newSpeed) {
        launchSpeed = newSpeed;
    }

    /**
     * Get the launch speed
     *
     * @return Launch Speed
     */
    public double getLaunchSpeed() {
        return launchSpeed;
    }

    /**
     * Set the drum speed
     *
     * @param newSpeed Between 0 and 1
     */
    public void setDrumSpeed(double newSpeed) {
        drumSpeed = newSpeed;
    }

    /**
     * Get the drum speed.
     *
     * @return Drum Speed
     */
    public double getDrumSpeed() {
        return drumSpeed;
    }

    /**
     * Set the intake speed
     *
     * @param newSpeed Between 0 and 1
     */
    public void setIntakeSpeed(double newSpeed) {
        intakeSpeed = newSpeed;
    }

    /**
     * Get the intake speed
     *
     * @return Intake Speed
     */
    public double getIntakeSpeed() {
        return intakeSpeed;
    }

    /**
     * Get the deadzone value.
     *
     * @return Deadzone
     */
    public double getDeadzone() {
        return DEADZONE;
    }


    /**
     * Runs all of the drive commands using the left and right sticks of a gamepad.
     *
     * @param gamepad1 Which gamepad's left and right sticks?
     */
    public void runDrive(Gamepad gamepad1) {
        // QOL #1: Set the Speed
        double speed = 1 - (gamepad1.right_trigger / 1.4);
        if (speed <= 0.1) {
            speed = .1;
        }
        // QOL #2: Reverse Controls
        if (gamepad1.left_trigger > .3) {
            speed = speed * (-1);
        }
        drive.setDriveSpeed(speed);
        if (abs(gamepad1.right_stick_x) > .4) { // If the right stick is being moved sufficiently
            if (speed < 0) {
                speed = abs(speed);
                drive.setDriveSpeed(speed);
            }
            // Tank Turn
            if (gamepad1.right_stick_x > .4) {
                drive.turnRightTank(1 * gamepad1.right_stick_x);
            }
            if (gamepad1.right_stick_x < -.4) {
                drive.turnLeftTank(1 * -gamepad1.right_stick_x);
            }
        } else if (abs(gamepad1.left_stick_x) > .4 || abs(gamepad1.left_stick_y) > .4) { // If the left stick is being moved sufficiently
            // Forward/Back
            if (gamepad1.left_stick_y < -.4 && abs(gamepad1.left_stick_x) < .4) {
                drive.moveForward(1 * -gamepad1.left_stick_y);
            }
            if (gamepad1.left_stick_y > .4 && abs(gamepad1.left_stick_x) < .4) {
                drive.moveBackward(1 * gamepad1.left_stick_y);
            }
            // Left/Right
            if (gamepad1.left_stick_x < -.4 && abs(gamepad1.left_stick_y) < .4) {
                drive.moveRight(1 * -gamepad1.left_stick_x);
            }
            if (gamepad1.left_stick_x > .4 && abs(gamepad1.left_stick_y) < .4) {
                drive.moveLeft(1 * gamepad1.left_stick_x);
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
        if(run) {
            kicker.setPosition(1);
            intake.setPower(intakeSpeed * reverse(r));
            int drumPos = drumRotor.getTargetPosition();
            if (drumPos % (ROTATION_TICK / 3) <= ROTATION_TICK / 7) {
                drumRotor.setTargetPosition((int) (drumPos / (ROTATION_TICK / 3)) * (ROTATION_TICK / 3) + ROTATION_TICK / 6);
            }
            if(isGreenOrPurple()!=-1
                    && drumRotor.getCurrentPosition() >= drumRotor.getTargetPosition()-ROTATION_TICK/16) {
                drumRotor.setTargetPosition(drumPos+ROTATION_TICK/3);
                // Shift over everything in ball storage
                balls[2] = balls[1];
                balls[1] = balls[0];
                // Adds our new ball
                balls[0] = isGreenOrPurple();
            }
        } else {
            intake.setPower(0);
        }
        telemetry.addData("Intake Speed", intakeSpeed);
    }

    /**
     * Handles all of the outtake
     * @param prime Set to true once to make the spinners start
     * @param cancel Set to true once to make the spinners stop
     * @param firePurple Set to true once to fire a purple, then stop the spinners after it's launched
     * @param fireGreen Set to true once to fire a green, then stop the spinners after it's launched
     */
    public void runOuttake(boolean prime, boolean cancel, boolean firePurple, boolean fireGreen){
        if(prime) {
            if(abs(drumRotor.getTargetPosition()%ROTATION_TICK) > ROTATION_TICK/16){
                drumRotor.setTargetPosition(drumRotor.getTargetPosition()+ ROTATION_TICK-drumRotor.getTargetPosition()%ROTATION_TICK);
            }
            launcherLeft.setPower(launchSpeed);
            launcherRight.setPower(launchSpeed);
        }
        if (cancel) {
            launcherLeft.setPower(0);
            launcherRight.setPower(0);
        }
        if (firePurple) {
            int purpleLocation = -1;
            for(int i=0; i<balls.length; i++){
                if(balls[i] == 1){
                    purpleLocation = i;
                }
            }
            if(purpleLocation != -1){
                launching = true;
                if(purpleLocation == 0){
                    drumRotor.setTargetPosition(drumRotor.getTargetPosition()+ROTATION_TICK/3);
                    int temp = balls[0];
                    balls[0] = balls[2];
                    balls[2] = balls[1];
                    balls[1] = temp;
                }
                if(purpleLocation == 2){
                    drumRotor.setTargetPosition(drumRotor.getTargetPosition()+2*ROTATION_TICK/3);
                    int temp = balls[2];
                    balls[2] = balls[0];
                    balls[0] = balls[1];
                    balls[1] = temp;
                }
            }
        }
        if (fireGreen) {
            int greenLocation = -1;
            for(int i=0; i<balls.length; i++){
                if(balls[i] == 0){
                    greenLocation = i;
                }
            }
            if(greenLocation != -1){
                launching = true;
                if(greenLocation == 0){
                    drumRotor.setTargetPosition(drumRotor.getTargetPosition()+ROTATION_TICK/3);
                    int temp = balls[0];
                    balls[0] = balls[2];
                    balls[2] = balls[1];
                    balls[1] = temp;
                }
                if(greenLocation == 2){
                    drumRotor.setTargetPosition(drumRotor.getTargetPosition()+2*ROTATION_TICK/3);
                    int temp = balls[2];
                    balls[2] = balls[0];
                    balls[0] = balls[1];
                    balls[1] = temp;
                }
            }
        }
        if(launching && abs(drumRotor.getTargetPosition()-drumRotor.getCurrentPosition())<ROTATION_TICK/16){
            kicker.setPosition(0.65);
            balls[1] = -1;
        }
    }
    /**
     * Posts all ball positions to telemetry2
     */
    public void ballTelemetry(){
        for(int i=0; i<balls.length; i++){
            if(balls[i] == 0){
                telemetry.addData("Ball "+i, "Green");
            }
            else if(balls[i] == 1){
                telemetry.addData("Ball "+i+" (Launch Position)", "Purple");
            }
            else{
                telemetry.addData("Ball "+i, "N/A");
            }
        }
    }

    /**
     * Determines the color from the color sensor, returns it as an array of doubles
     * @return Contains the different color values. [0] is red, [1] is green, [2] is blue
     */
    private double[] getColor() {
        double red = BallColor.red();
        double green = BallColor.green();
        double blue = BallColor.blue();
        return new double[]{red, green, blue};
    }

    /**
     * Using the color sensor, determines whether the detected color is green, purple, or neither.
     * @return 0 if green, 1 if purple, -1 if neither
     */
    private int isGreenOrPurple(){
        double[] colors = getColor();
        if(colors[1] > colors[2] * 1.5 && colors[1] > colors[0] * 1.5){
            return 0;
        }
        if (colors[2] > colors[1] * 1.2 && colors[0] > colors[1] * 1.2){
            return 1;
        }
        return -1;
    }
}
