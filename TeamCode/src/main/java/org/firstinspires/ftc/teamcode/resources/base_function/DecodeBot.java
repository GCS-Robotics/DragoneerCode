package org.firstinspires.ftc.teamcode.resources.base_function;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.resources.States;
import org.firstinspires.ftc.teamcode.resources.autonomous.LimelightHandler;

public class DecodeBot {
    private IMU imu;
    public LimelightHandler limelight;
    public Intake intake;
    public Flywheels flywheels;
    public Drum drum;
    public Kicker kicker;
    public Color color;
    public Drive drive;
    public Telemetry telemetry;
    public Telemetry dashTelemetry;
    public States.General state;
    public double distance;

    // Color detection debounce state to avoid single-frame misclassifications
    private States.Artifact lastColor = States.Artifact.NONE;
    private int colorStableCount = 0;
    private static final int COLOR_STABLE_THRESHOLD = 3;

    public DecodeBot(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry){
        imu = hardwareMap.get(IMU.class, "imu");
        limelight = new LimelightHandler(hardwareMap);
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
    public void run(){
        drum.run(true);
        flywheels.run(true);
        // Intake Mode
        if(state == States.General.INTAKE){
            drum.intakeMode();
            kicker.retract();
            intake.run(true);

            // Sample detection and require it to be stable for several loop cycles
            States.Artifact detected = color.isGreenOrPurple();
            if(detected != States.Artifact.NONE && drum.state == States.DrumState.IDLE && drum.countBalls() < 3){
                if(detected == lastColor){
                    colorStableCount++;
                } else {
                    lastColor = detected;
                    colorStableCount = 1;
                }
                if(colorStableCount >= COLOR_STABLE_THRESHOLD){
                    drum.intakeBall(detected);
                    // reset debounce after accepting
                    colorStableCount = 0;
                    lastColor = States.Artifact.NONE;
                }
            } else {
                // Reset on no detection or when not intaking
                colorStableCount = 0;
                lastColor = States.Artifact.NONE;
            }
        }
        else {
            intake.run(false);
        }
        // Prime Mode
        if(state == States.General.PRIMED || state == States.General.LAUNCHING){
            distance = limelight.getDistance(imu.getRobotYawPitchRollAngles().getYaw());
            if(distance != -1){
                flywheels.rpmFromDistance(distance);
            }
            drum.outtakeMode();
            flywheels.prime();
            if(drum.state == States.DrumState.IDLE
                    && state == States.General.LAUNCHING
                    && flywheels.launchersAtSpeed()) {
                kicker.kick();
                drum.launchBall();
                state = States.General.PRIMED;
            }
        } else{
            flywheels.cancel();
        }
    }
    public void run(Gamepad driveGamepad){
        drive.run(driveGamepad);
        run();
    }
    public void postTelemetry(){
        for(Telemetry telemetry : new Telemetry[]{telemetry, dashTelemetry}){
            drum.postTelemetry(telemetry);
            telemetry.addData("Distance", distance);
            telemetry.addLine();
            telemetry.addData("Manual Speed Mode", flywheels.doRPM);
            telemetry.addData("Drum Ticks", Drum.targetPosition);
            telemetry.addLine();
            flywheels.postTelemetry(telemetry);
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
        if(run && state == States.General.PRIMED){
            flywheels.cancel();
            state = States.General.INTAKE;
        }
    }
    public void setOuttake(boolean prime, boolean stop, boolean launchPurple, boolean launchGreen){
        if((prime || launchPurple || launchGreen) && state == States.General.IDLE){
            state = States.General.PRIMED;
        }
        if(stop && state == States.General.PRIMED){
            flywheels.cancel();
            state = States.General.IDLE;
        }
        if(launchPurple && drum.hasBall(States.Artifact.PURPLE) && state == States.General.PRIMED){
            kicker.retract();
            state = States.General.LAUNCHING;
            drum.setDrumLaunch(States.Artifact.PURPLE);
        }
        if(launchGreen && drum.hasBall(States.Artifact.GREEN) && state == States.General.PRIMED){
            kicker.retract();
            state = States.General.LAUNCHING;
            drum.setDrumLaunch(States.Artifact.GREEN);
        }
    }
    public int getMotifTag() {
        if(limelight.findAprilTags().size() <= 0){
            return -1;
        }
        return limelight.findAprilTags().get(0).getFiducialId();
    }
}
