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
    public boolean busy = false;
    private final double KICKED = 0.65;
    private final double NOT_KICKED = 0.35;
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
            telemetry.addLine();
            color.postTelemetry(telemetry);
            telemetry.update();
        }
    }
    public void runIntake(boolean running){
        if(!running){
            intake.run(false);
            return;
        }
        retractKicker();
        intake.run(true);
        int ball = color.isGreenOrPurple();
        if(ball != -1 && drum.reachedTarget() && drum.countBalls() < 3){
            drum.intakeBall(ball);
        }
        if(ball == -1 && drum.reachedTarget()){
            drum.intakeMode();
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
    public void reindex(){
        retractKicker();
        busy = true;
        reindexCount = 0;
        drum.resetBalls();
        drum.rotateThird();
    }
    private int reindexCount = 0;
    public void reindexing(){
        if(!drum.reachedTarget()){
            int ball = color.isGreenOrPurple();
            if(ball != -1 && drum.reachedTarget()){
                drum.intakeBall(ball);
                reindexCount++;
            }
        } else{
            if(reindexCount < 3){
                reindexCount++;
            }
            if(reindexCount == 3){
                busy = false;
            }
        }
    }
}
