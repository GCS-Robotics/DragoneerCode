package org.firstinspires.ftc.teamcode.resources.base_function;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DecodeBot {
    public Intake intake;
    public Flywheels flywheels;
    public Drum drum;
    public Kicker kicker;
    public Color color;
    public Drive drive;
    public Telemetry telemetry;
    public Telemetry dashTelemetry;
    public boolean launching = false;
    public boolean busy = false;
    public DecodeBot(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry){
        intake = new Intake(hardwareMap);
        flywheels = new Flywheels(hardwareMap);
        drive = new Drive(hardwareMap);
        color = new Color(hardwareMap);
        kicker = new Kicker(hardwareMap);
        this.telemetry = telemetry;
        this.dashTelemetry = dashTelemetry;
        drum = new Drum(hardwareMap, 1.0);
    }
    public DecodeBot(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry, int[] balls){
        this(hardwareMap, telemetry, dashTelemetry);
        drum = new Drum(hardwareMap, 1.0, balls);
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
        drum.intakeMode();
        kicker.retract();
        intake.run(true);
        int ball = color.isGreenOrPurple();
        if(ball != -1 && drum.reachedTarget() && drum.countBalls() < 3){
            drum.intakeBall(ball);
        }
    }
    public void launchBall(int color){
        launching = true;
        drum.setDrumLaunch(color);
    }
    public void kick(){
        if(drum.reachedTarget() && launching){
            kicker.kick();
            drum.launchBall();
            launching = false;
        }
    }
    public void reindex(){
        kicker.retract();
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
