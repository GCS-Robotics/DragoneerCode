package org.firstinspires.ftc.teamcode.resources.base_function;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DecodeBot {
    public Intake intake;
    public Flywheels flywheels;
    public Drum drum;
    private Servo kicker;
    public Color color;
    public Drive drive;
    public Telemetry telemetry;
    public Telemetry dashTelemetry;
    public boolean launching = false;
    private final double KICKED = 0.3;
    private final double NOT_KICKED = 0.65;
    public DecodeBot(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry){
        intake = new Intake(hardwareMap);
        flywheels = new Flywheels(hardwareMap);
        drum = new Drum(hardwareMap, 1.0);
        drive = new Drive(hardwareMap);
        color = new Color(hardwareMap);
        kicker = hardwareMap.get(Servo.class, "kicker");
        this.telemetry = telemetry;
        this.dashTelemetry = dashTelemetry;
    }
    public DecodeBot(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry, int[] balls){
        intake = new Intake(hardwareMap);
        flywheels = new Flywheels(hardwareMap);
        drum = new Drum(hardwareMap, 1.0, balls);
        drive = new Drive(hardwareMap);
        color = new Color(hardwareMap);
        kicker = hardwareMap.get(Servo.class, "kicker");
        this.telemetry = telemetry;
        this.dashTelemetry = dashTelemetry;
    }
    public void postTelemetry(){
        for(Telemetry telemetry : new Telemetry[]{telemetry, dashTelemetry}){
            drum.postTelemetry(telemetry);
            telemetry.addLine();
            flywheels.postTelemetry(telemetry);
        }
    }
    public void runIntake(boolean running){
        if(!running){
            intake.run(false);
            return;
        }
        retractKicker();
        intake.run(true);
        if(drum.ballsFull()) return;
        int ball = color.isGreenOrPurple();
        if(ball != -1 && drum.reachedTarget()){
            drum.intakeBall(ball);
        }
    }
    public void launchBall(int color){
        launching = true;
        drum.setDrumLaunch(color);
    }
    public void retractKicker(){
        kicker.setPosition(NOT_KICKED);
    }
    public void deployKicker(){
        kicker.setPosition(KICKED);
    }
    public void kick(){
        if(drum.reachedTarget() && launching){
            deployKicker();
            drum.launchBall();
            launching = false;
        }
    }
}
