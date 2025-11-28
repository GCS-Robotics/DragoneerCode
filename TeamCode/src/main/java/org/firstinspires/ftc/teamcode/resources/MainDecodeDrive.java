package org.firstinspires.ftc.teamcode.resources;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MainDecodeDrive {
    double SPEED;
    double currentSpeed;
    double intakeSpeed = 1.0;
    double launchSpeed = 1.0;
    double drumSpeed = 1.0;
    double DEADZONE;
    final int ROTATION_TICK = 288;
    int[] balls = new int[3]; // Ball at balls[1] is the ball that should be primed to fire
    DcMotor intake;
    DcMotor launcherRight;
    DcMotor launcherLeft;
    DcMotor drumRotor;
    Servo kicker;
    MecanumDrive drive;
    Telemetry telemetry;

    ColorSensor BallColor;
    boolean launching = false;
    boolean primed = false;

    /**
     * Constructs a master decode drive.
     *
     * @param hardwareMap Finds all of the hardware components from the Hardware Map
     * @param tel         For any functions that want to post to telemetry (must call telemetry.update() separately)
     */
    public MainDecodeDrive(HardwareMap hardwareMap, Telemetry tel) {
        this(hardwareMap, tel, 1.0, 1.0, 1.0, 0.2, 0.2);
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
        intake = hardwareMap.dcMotor.get("intake");
        launcherRight = hardwareMap.dcMotor.get("launcherRight");
        launcherRight.setDirection(DcMotorSimple.Direction.REVERSE);
        launcherLeft = hardwareMap.dcMotor.get("launcherLeft");
        drumRotor = hardwareMap.dcMotor.get("drumRotor");
        kicker = hardwareMap.servo.get("kicker");
        BallColor = hardwareMap.colorSensor.get("colorSensor");
        kicker.setDirection(Servo.Direction.REVERSE);
        kicker.setPosition(.65);
        balls[0] = -1;
        balls[1] = -1;
        balls[2] = -1;
        drive = new MecanumDrive(hardwareMap, s);
        DcMotor[] motors = {launcherLeft, launcherRight, drumRotor};
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
     * @param gamepad Gamepad that provides the left and right sticks.
     */
    public void runDrive(Gamepad gamepad) {
        currentSpeed = SPEED - (gamepad.right_trigger / 1.4);
        if (currentSpeed <= 0.1) {
            currentSpeed = .1;
        }
        drive.runDrive(gamepad, currentSpeed, gamepad.left_trigger > DEADZONE);
    }
    /**
     * Runs the intake if the boolean is true.
     * @param run Whether or not the intake should be active
     * @param r Reverse Direction
     */
    public void runIntake(boolean run, boolean r){
        if(run && !(launching || primed)) {
            kicker.setPosition(0.65);
            intake.setPower(intakeSpeed * reverse(r));
            int drumPos = drumRotor.getTargetPosition();
            if (drumPos % (ROTATION_TICK / 3) <= ROTATION_TICK / 7) {
                drumRotor.setTargetPosition((int) (drumPos / (ROTATION_TICK / 3)) * (ROTATION_TICK / 3) + ROTATION_TICK / 6);
            }
            int ball = isGreenOrPurple();
            if(ball!=-1
                    && drumRotor.getCurrentPosition() >= drumRotor.getTargetPosition()-ROTATION_TICK/16) {
                drumRotor.setTargetPosition(drumPos+ROTATION_TICK/3);
                // Shift over everything in ball storage
                balls[0] = balls[2];
                balls[2] = balls[1];
                // Adds our new ball
                balls[1] = ball;
            }
        } else {
            intake.setPower(0);
        }
    }

    /**
     * Handles all of the outtake
     * @param prime Set to true once to make the spinners start
     * @param cancel Set to true once to make the spinners stop
     * @param firePurple Set to true once to fire a purple, then stop the spinners after it's launched
     * @param fireGreen Set to true once to fire a green, then stop the spinners after it's launched
     */
    public void runOuttake(boolean prime, boolean cancel, boolean firePurple, boolean fireGreen){
        if(prime && !primed) {
            primed = true;
            int drumPos = drumRotor.getTargetPosition();
            drumRotor.setTargetPosition(drumPos + ROTATION_TICK / 6);
            launcherLeft.setPower(launchSpeed);
            launcherRight.setPower(launchSpeed);
        }
        if (cancel) {
            primed = false;
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
                setDrumLaunch(purpleLocation);
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
                setDrumLaunch(greenLocation);
            }
        }
        if(launching && abs(drumRotor.getTargetPosition()-drumRotor.getCurrentPosition())<ROTATION_TICK/16){
            kicker.setPosition(0.3);
            balls[1] = -1;
            launching = false;
        }
    }
    private void setDrumLaunch(int ballLocation){
        kicker.setPosition(0.65);
        if(ballLocation == 0){
            drumRotor.setTargetPosition(drumRotor.getTargetPosition()+ROTATION_TICK/3);
            int temp = balls[0];
            balls[0] = balls[2];
            balls[2] = balls[1];
            balls[1] = temp;
        }
        else if(ballLocation == 2){
            drumRotor.setTargetPosition(drumRotor.getTargetPosition()+(ROTATION_TICK*2/3));
            int temp = balls[2];
            balls[2] = balls[0];
            balls[0] = balls[1];
            balls[1] = temp;
        }
    }
    /**
     * Posts all necessary information to telemetry
     */
    public void postTelemetry(){
        telemetry.addData("Drive Speed", currentSpeed);
        telemetry.addLine();
        telemetry.addData("Launch Speed", launchSpeed);
        telemetry.addLine();
        for(int i=0; i<balls.length; i++){
            String ballDesc = "Stored Ball - ";
            if(i == 1){
                ballDesc = "Launch Ball - ";
            }
            if(balls[i] == 0){
                telemetry.addData(ballDesc, "Green");
            }
            else if(balls[i] == 1){
                telemetry.addData(ballDesc, "Purple");
            }
            else{
                telemetry.addData(ballDesc, "N/A");
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
        if(colors[1] > colors[2] * 1.5 && colors[1] > colors[0] * 2.0){
            return 0;
        }
        if (colors[2] > colors[1] * 1.5 && colors[2] > colors[0]*1.5){
            return 1;
        }
        return -1;
    }
}
