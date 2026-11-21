package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;
import org.firstinspires.ftc.teamcode.robot_modules.sensors.Limelight;

public class AdjustableOuttake extends RunModule<Boolean> {
    Servo hoodAdjuster;
    Servo gapAdjuster;
    DcMotorEx yawAdjuster;
    Flywheel flywheel;
    double yawPosition = 0;
    double hoodPose = 0;
    int ticksPerRev = 1425;

    int ticksLimit = 1069;
    private final double HOOD_ADJUST = 0.1;
    private final double POLLEN_GAP = 0;
    private final double NECTAR_GAP = 1;
    double gapPose;
    private boolean isSpinning = false;

    Limelight limelight;
    private double[] pidf = new double[]{0.003, 0.123, 0, 0};
    public AdjustableOuttake(HardwareMap hardwareMap, String fName, String hName, String gName, String yName, String lName) {
        flywheel = new Flywheel(hardwareMap, fName);
        limelight = new Limelight(hardwareMap, lName);
        hoodAdjuster = hardwareMap.get(Servo.class, hName);
        gapAdjuster = hardwareMap.get(Servo.class, gName);
        yawAdjuster = hardwareMap.get(DcMotorEx.class, yName);
        hoodAdjuster.setPosition(.23);
        yawAdjuster.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel.setPIDF(pidf);
    }
    public void runHoodAdjuster(Boolean run) {
        if (run) {
            hoodPose = limelight.getAngleDegrees(0, 100)/100;
            hoodAdjuster.setPosition(hoodPose);
        }
    }

    public void runYawAdjuster(Boolean run) {
        if (run) {
            yawPosition = yawAdjuster.getCurrentPosition();
            yawAdjuster.setTargetPosition(Math.max(Math.min((int) ((yawPosition + ((limelight.getYaw() * 2.75) / 100)) * ticksPerRev), ticksLimit), -ticksLimit));
            yawAdjuster.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            yawAdjuster.setVelocity(2800);
        }
    }

    @Override
    public void run(Boolean isRunning) {
        if (isRunning) {
            flywheel.run(isRunning);
            runHoodAdjuster(true);
            runYawAdjuster(true);
        } else {
            stop();
        }

    }

    @Override
    public void stop() {
        flywheel.stop();
        runHoodAdjuster(false);
        runYawAdjuster(false);
    }
    @Override
    public void postTelemetry(Telemetry telemetry) {
        telemetry.addData("Is Spinning?", isSpinning);
        telemetry.addData("Flywheel Target Power", flywheel.getTargetRPM());
        telemetry.addData("Hood Position", hoodPose);
        telemetry.addData("Gap Postition", gapPose);
    }
}
