package org.firstinspires.ftc.teamcode.resources;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MainDecodeDrive {
    // Constants
    final double DRIVE_SPEED;
    final double DEADZONE;
    final double KICKER_BACK = 0.65;
    final double KICKER_KICKED = 0.3;
    // Mutable Variables
    double currentSpeed;
    // Hardware
    private final DcMotor intake;
    private final DrumRotor drumRotor;
    private final Launchers launchers;
    private final ColorSensor BallColor;
    private final Servo kicker;
    private final RoadRunnerMecanumDrive drive;
    // Telemetry
    private final Telemetry telemetry;
    private final Telemetry dashboardTelemetry;
    private boolean primed = false;
    private boolean launching = false;
    /**
     * Constructs a master decode drive.
     *
     * @param hardwareMap Finds all of the hardware components from the Hardware Map
     * @param tel         For any functions that want to post to telemetry
     * @param dashTel     For any functions that want to post to dashboard
     */
    public MainDecodeDrive(HardwareMap hardwareMap, Telemetry tel, Telemetry dashTel) {
        this(hardwareMap, tel, dashTel, 1.0, 2000, 1, 0.01);
    }

    /**
     * Constructs a master decode drive.
     *
     * @param hardwareMap Finds all of the hardware components from the Hardware Map
     * @param tel         For any functions that want to post to telemetry
     * @param dashTel     For any functions that want to post to dashboard
     * @param s           Drive Speed
     * @param launchS     Launch Speed
     * @param drumS       Drum Speed
     * @param dz          Deadzone for driving inputs
     */
    public MainDecodeDrive(HardwareMap hardwareMap, Telemetry tel, Telemetry dashTel, double s, double launchS, double drumS, double dz) {
        // Telemetry
        telemetry = tel;
        dashboardTelemetry = dashTel;
        // Intake
        intake = hardwareMap.dcMotor.get("intake");
        // Launchers
        launchers = new Launchers(hardwareMap);
        launchers.setTargetRPM(launchS);
        // Drum
        drumRotor = new DrumRotor(hardwareMap, drumS);
        kicker = hardwareMap.servo.get("kicker");
        BallColor = hardwareMap.colorSensor.get("colorSensor");
        kicker.setDirection(Servo.Direction.REVERSE);
        // Drive
        drive = new RoadRunnerMecanumDrive(hardwareMap, s);
        // Constants
        DRIVE_SPEED = s;
        DEADZONE = dz;
        drumRotor.intakeMode();
    }

    /**
     * Reverse power if true
     * @param do_it Whether or not power should be reversed
     * @return -1 if true, 1 if false
     */
    private int reverse(boolean do_it) {
        if (do_it) return -1;
        return 1;
    }

    /**
     * Get the deadzone value.
     *
     * @return Deadzone
     */
    public double getDeadzone() {
        return DEADZONE;
    }

    public void setLaunchSpeed(double speed){
        launchers.setTargetRPM(speed);
    }
    public double getLaunchSpeed(){
        return launchers.getTargetRPM();
    }
    public boolean isLaunching(){
        return launching;
    }
    public boolean isSpunUp(){
        return launchers.launchersAtSpeed();
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
        if(run && !primed) {
            runKicker(false);
            drumRotor.intakeMode();
            intake.setPower(reverse(r));
            if(drumRotor.ballsFull()){return;}
            int ball = isGreenOrPurple();
            if(ball!=-1 && drumRotor.reachedTarget()) {
                drumRotor.intakeBall(ball);
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
        launchers.runLauncher();
        if(prime && !primed) {
            primed = true;
            drumRotor.outtakeMode();
            launchers.prime();
            runKicker(false);
        }
        if (cancel) {
            runKicker(false);
            primed = false;
            drumRotor.intakeMode();
            launchers.cancel();
        }
        if (firePurple && primed) {
            launching = true;
            drumRotor.setDrumLaunch(1);
            runKicker(false);
        }
        if (fireGreen && primed) {
            launching = true;
            drumRotor.setDrumLaunch(0);
            runKicker(false);
        }
        if(drumRotor.reachedTarget() && launching){
            runKicker(true);
            drumRotor.launchBall();
            launching = false;
        }
    }
    /**
     * Posts all necessary information to telemetry
     */
    public void postTelemetry(){
        runDrum();
        Telemetry[] telemetries = {telemetry, dashboardTelemetry};
        for(Telemetry telemetry : telemetries) {
            telemetry.addData("Drive Speed", currentSpeed);
            telemetry.addLine();
            drumRotor.storageTelemetry(telemetry);
            telemetry.addLine();
            launchers.launchTelemetry(telemetry);
            telemetry.addData("Launchers At Speed", launchers.launchersAtSpeed());
            telemetry.addLine();
            drumRotor.drumTelemetry(telemetry);
            telemetry.update();
        }
    }
    public void runDrum(){
        drumRotor.run();
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
            kicker.setPosition(KICKER_KICKED);
        } else{
            kicker.setPosition(KICKER_BACK);
        }
    }
    public void setPreload(int[] balls){
        drumRotor.setPreload(balls);
    }
}
