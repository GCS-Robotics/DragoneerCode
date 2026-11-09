package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;
import org.firstinspires.ftc.teamcode.robot_modules.sensors.Limelight;

public class AdjustableOuttake extends RunModule<Gamepad> {
    Servo hoodAdjuster;
    Servo gapAdjuster;
    Servo yawAdjuster;
    Flywheel flywheel;
    double yawServoPosition = 0;
    double hoodPose = 0;
    private final double MAX_HOOD = 1;
    private final double MIN_HOOD = 0;
    private final double HOOD_ADJUST = 0.1;
    private final double POLLEN_GAP = 0;
    private final double NECTAR_GAP = 1;
    double gapPose;
    private boolean isSpinning = false;

    Limelight limelight;

    public AdjustableOuttake(HardwareMap hardwareMap, String fName, String hName, String gName, String yName, String lName) {
        flywheel = new Flywheel(hardwareMap, fName);
        limelight = new Limelight(hardwareMap, lName);
        hoodAdjuster = hardwareMap.get(Servo.class, hName);
        gapAdjuster = hardwareMap.get(Servo.class, gName);
        yawAdjuster = hardwareMap.get(Servo.class, yName);
    }
    public void runHoodAdjuster(Gamepad gamepad) {
        if (gamepad.aWasReleased()) {
            hoodPose += HOOD_ADJUST;
        }
        if (gamepad.bWasReleased()) {
            hoodPose -= HOOD_ADJUST;
        }
        hoodPose = Math.min(Math.max(hoodPose, MIN_HOOD), MAX_HOOD);
        hoodAdjuster.setPosition(hoodPose);
    }

    public void runGapAdjuster(Gamepad gamepad) {
        if (gamepad.dpadDownWasReleased()) {
            gapAdjuster.setPosition(POLLEN_GAP);
        }
        if (gamepad.dpadUpWasReleased()) {
            gapAdjuster.setPosition(NECTAR_GAP);
        }
    }

    public void runYawAdjuster(Gamepad gamepad) {
        if (gamepad.dpadLeftWasPressed()) {
            yawAdjuster.setPosition(yawServoPosition + ((limelight.getYaw() * 2.75) / 100));
            yawServoPosition = yawAdjuster.getPosition();
        } else {
            yawAdjuster.setPosition(gamepad.left_stick_x * 2.75);
        }
    }

    @Override
    public void run(Gamepad gamepad) {
        if(gamepad.xWasPressed()){
            isSpinning = !isSpinning;
        }
        flywheel.run(isSpinning);
        runHoodAdjuster(gamepad);
        runGapAdjuster(gamepad);
        runYawAdjuster(gamepad);
    }

    @Override
    public void stop() {

    }
    @Override
    public void postTelemetry(Telemetry telemetry) {
        telemetry.addData("Is Spinning?", isSpinning);
        telemetry.addData("Flywheel Target Power", flywheel.getTargetRPM());
        telemetry.addData("Hood Position", hoodPose);
        telemetry.addData("Gap Postition", gapPose);
    }
}
