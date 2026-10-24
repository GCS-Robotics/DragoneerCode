package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.Module;

public class AdjustableOuttake extends Module<Gamepad> {

    DcMotor flywheel;
    Servo hoodAdjuster;
    Servo gapAdjuster;
    double power = 1000;
    double hoodPose = 0;
    double maxHood = 25;
    double minHood = -25;
    double minGap = -10;
    double maxGap = 10;
    double gapPose;

    public AdjustableOuttake(HardwareMap hardwareMap, String fName, String hName, String gName) {
        flywheel = hardwareMap.get(DcMotor.class, fName);
        hoodAdjuster = hardwareMap.get(Servo.class, hName);
        gapAdjuster = hardwareMap.get(Servo.class, gName);
    }

    public void runFlywheel(Gamepad gamepad) {
        if (gamepad.rightTriggerWasReleased()) {
            power += 50;
        } else if (gamepad.rightBumperWasReleased()) {
            power -= 50;
        }
        flywheel.setPower(power);
    }

    public void runHoodAdjuster(Gamepad gamepad) {
        if (gamepad.aWasReleased()) {
            hoodPose += 5;
        }
        if (gamepad.bWasReleased()) {
            hoodPose -= 5;
        }
        hoodPose = Math.min(Math.max(hoodPose, minHood), maxHood);
        hoodAdjuster.setPosition(hoodPose);
    }

    public void runGapAdjuster(Gamepad gamepad) {
        if (gamepad.dpadDownWasReleased()) {
            gapPose -= 10;
        }
        if (gamepad.dpadUpWasReleased()) {
            gapPose += 10;
        }
        gapPose = Math.min(Math.max(gapPose, minGap), maxGap);
        gapAdjuster.setPosition(gapPose);
    }

    @Override
    public void run(Gamepad gamepad) {
        runFlywheel(gamepad);
        runHoodAdjuster(gamepad);
        runGapAdjuster(gamepad);
    }

    @Override
    public void stop() {
        flywheel.setPower(0);
    }

    @Override
    public void postTelemetry(Telemetry telemetry) {
        telemetry.addData("Flywheel Power:", power);
        telemetry.addData("Hood Position:", hoodPose);
        telemetry.addData("Gap Postition:", gapPose);
    }
}
