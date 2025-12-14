package org.firstinspires.ftc.teamcode.resources.base_function;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.resources.States;

public class DecodeBot {
    public Intake intake;
    public Flywheels flywheels;
    public Drum drum;
    public Kicker kicker;
    public Color color;
    public Drive drive;
    public Telemetry telemetry;
    public Telemetry dashTelemetry;
    public States.General state;
    public DecodeBot(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry){
        intake = new Intake(hardwareMap);
        flywheels = new Flywheels(hardwareMap);
        drive = new Drive(hardwareMap);
        color = new Color(hardwareMap);
        kicker = new Kicker(hardwareMap);
        this.telemetry = telemetry;
        this.dashTelemetry = dashTelemetry;
        drum = new Drum(hardwareMap, 1.0);
        state = States.General.IDLE;
    }
    public DecodeBot(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry, States.Artifact[] balls){
        this(hardwareMap, telemetry, dashTelemetry);
        drum = new Drum(hardwareMap, 1.0, balls);
    }
    public void run(Gamepad driveGamepad){
        if(driveGamepad != null){
            drive.run(driveGamepad);
        }
        drum.run(true);
        flywheels.run(true);
        // Intake Mode
        if(state == States.General.INTAKE){
            drum.intakeMode();
            kicker.retract();
            intake.run(true);
            States.Artifact ball = color.isGreenOrPurple();
            if(ball != States.Artifact.NONE && drum.reachedTarget() && drum.countBalls() < 3){
                drum.intakeBall(ball);
            }
        }
        else intake.run(false);
        // Prime Mode
        if(state == States.General.PRIMED){
            drum.outtakeMode();
            flywheels.prime();
        } else if(state == States.General.LAUNCHING){
            drum.outtakeMode();
            flywheels.prime();
            if(drum.reachedTarget() && flywheels.state == States.Outtake.READY){
                kicker.kick();
                drum.launchBall();
                state = States.General.PRIMED;
            }
        } else{
            flywheels.cancel();
        }
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
    public void runIntake(boolean run){
        if(run && state == States.General.IDLE){
            state = States.General.INTAKE;
        }
        if(!run && state == States.General.INTAKE){
            state = States.General.IDLE;
        }
    }
    public void setOuttake(boolean prime, boolean stop, boolean launchPurple, boolean launchGreen){
        if(prime && state == States.General.IDLE){
            state = States.General.PRIMED;
        }
        if(stop && state == States.General.PRIMED){
            state = States.General.IDLE;
        }
        if(launchPurple && state == States.General.PRIMED){
            kicker.retract();
            state = States.General.LAUNCHING;
            drum.setDrumLaunch(States.Artifact.PURPLE);
        }
        if(launchGreen && state == States.General.PRIMED){
            kicker.retract();
            state = States.General.LAUNCHING;
            drum.setDrumLaunch(States.Artifact.GREEN);
        }
    }
}
