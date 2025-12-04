package org.firstinspires.ftc.teamcode.resources;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class MainDecodeDrive {
    // Constants
    final double DRIVE_SPEED;
    final double DEADZONE;
    final double KICKER_BACK = 0.65;
    final double KICKER_KICKED = 0.3;
    // Mutable Variables
    double currentSpeed;
    double intakeSpeed = 1.0;
    double launchSpeed = 1.0;
    int[] balls = new int[3]; // Ball at balls[1] is the ball that should be primed to fire
    // Hardware
    DcMotor intake;
    DcMotorEx launcherRight;
    DcMotorEx launcherLeft;
    public DrumRotor drumRotor;
    ColorSensor BallColor;
    public Servo kicker;
    RegularMecanumDrive drive;
    // Telemetry
    Telemetry telemetry;
    Telemetry dashboardTelemetry;


    boolean launching = false;
    boolean primed = false;

    /**
     * Constructs a master decode drive.
     *
     * @param hardwareMap Finds all of the hardware components from the Hardware Map
     * @param tel         For any functions that want to post to telemetry (must call telemetry.update() separately)
     */
    public MainDecodeDrive(HardwareMap hardwareMap, Telemetry tel, Telemetry dashTel) {
        this(hardwareMap, tel, dashTel, 1.0, 1.0, 1000, 1, 0.01);
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
    public MainDecodeDrive(HardwareMap hardwareMap, Telemetry tel, Telemetry dashTel, double s, double intakeS, double launchS, double drumS, double dz) {
        telemetry = tel;
        dashboardTelemetry = dashTel;
        intake = hardwareMap.dcMotor.get("intake");
        launcherRight = hardwareMap.get(DcMotorEx.class, "launcherRight");
        launcherRight.setDirection(DcMotorSimple.Direction.REVERSE);
        launcherLeft = hardwareMap.get(DcMotorEx.class, "launcherLeft");
        drumRotor = new DrumRotor(hardwareMap, drumS);
        kicker = hardwareMap.servo.get("kicker");
        BallColor = hardwareMap.colorSensor.get("colorSensor");
        kicker.setDirection(Servo.Direction.REVERSE);
        balls[0] = -1;
        balls[1] = -1;
        balls[2] = -1;
        drive = new RegularMecanumDrive(hardwareMap, s);
        DcMotor[] motors = {launcherLeft, launcherRight};
        for (DcMotor motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        DRIVE_SPEED = s;
        intakeSpeed = intakeS;
        launchSpeed = launchS;
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
        if(primed){
            launcherLeft.setVelocity(launchSpeed);
            launcherRight.setVelocity(launchSpeed);
        }
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
        currentSpeed = DRIVE_SPEED - (gamepad.right_trigger / 1.4);
        if (currentSpeed <= 0.1) {
            currentSpeed = .1;
        }
        drive.runDrive(gamepad, currentSpeed, gamepad.left_trigger > DEADZONE, DEADZONE);
    }
    /**
     * Runs the intake if the boolean is true.
     * @param run Whether or not the intake should be active
     * @param r Reverse Direction
     */
    public void runIntake(boolean run, boolean r){
        if(run && !(launching || primed)) {
            int ballCount = 0;
            for(int ball : balls){
                if(ball>=0){
                    ballCount+=1;
                }
            }
            intake.setPower(intakeSpeed * reverse(r));
            if(ballCount >= 3){
                return;
            }
            drumRotor.intakeMode();
            int ball = isGreenOrPurple();
            if(ball!=-1
                    && drumRotor.reachedTarget()) {
                drumRotor.rotateThird();
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
            drumRotor.outtakeMode();
            launcherLeft.setVelocity(launchSpeed);
            launcherRight.setVelocity(launchSpeed);
        }
        if (cancel) {
            primed = false;
            launcherLeft.setPower(0);
            launcherRight.setPower(0);
        }
        if (firePurple && primed) {
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
        if (fireGreen && primed) {
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
    }
    public boolean launchersHappy(){
        return abs(launcherRight.getVelocity(AngleUnit.DEGREES))+
                abs(launcherLeft.getVelocity(AngleUnit.DEGREES))
                > 0;
    }

    /**
     * Prepares the ball in the designated position for launch
     * @param ballLocation Which ball in the balls[] array should be launched.
     */
    private void setDrumLaunch(int ballLocation){
        if(ballLocation == 1){
            return;
        }
        if(ballLocation == 0){
            drumRotor.rotateThird();
            int temp = balls[0];
            balls[0] = balls[2];
            balls[2] = balls[1];
            balls[1] = temp;
            return;
        }
        if(ballLocation == 2){
            drumRotor.rotateTwoThirds();
            int temp = balls[2];
            balls[2] = balls[0];
            balls[0] = balls[1];
            balls[1] = temp;
            return;
        }
    }
    /**
     * Posts all necessary information to telemetry
     */
    public void postTelemetry(){
        drumRotor.run();
        Telemetry[] telemetries = {telemetry, dashboardTelemetry};
        for(Telemetry telemetry : telemetries) {
            telemetry.addData("Drum Target Rotation (In Spins)", drumRotor.drum.getTargetPosition()/drumRotor.ROTATION_TICK);
            telemetry.addData("Drum Current (In Spins)", drumRotor.drum.getCurrentPosition()/drumRotor.ROTATION_TICK);
            telemetry.addData("Drive Speed", currentSpeed);
            telemetry.addLine();
            telemetry.addData("Launch Speed", launchSpeed);
            telemetry.addLine();
            for (int i = 0; i < balls.length; i++) {
                String ballDesc = "Stored Ball - ";
                if (i == 1) {
                    ballDesc = "Launch Ball - ";
                }
                if (balls[i] == 0) {
                    telemetry.addData(ballDesc, "Green");
                } else if (balls[i] == 1) {
                    telemetry.addData(ballDesc, "Purple");
                } else {
                    telemetry.addData(ballDesc, "N/A");
                }
            }
            telemetry.addLine();
            telemetry.addData("Red", getColor()[0]);
            telemetry.addData("Green", getColor()[1]);
            telemetry.addData("Blue", getColor()[2]);
            telemetry.update();
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
        if(colors[1] > colors[2] && colors[1] > colors[0] * 2 && colors[1] > 150){ // Green
            return 0;
        }
        if (colors[2] > colors[1] * 1.2 && colors[2] > colors[0]*1.5 && colors[2] > 150){ // Purple
            return 1;
        }
        return -1;
    }
    public void runKicker(boolean kick){
        if(kick){
            if(drumRotor.reachedTarget() && launching && launchersHappy()){
                balls[1] = -1;
                launching = false;
            }
            kicker.setPosition(KICKER_KICKED);
        } else{
            kicker.setPosition(KICKER_BACK);
        }
    }
}
