package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.Module;

public class AdjustableOuttake extends Module<Boolean> {

    DcMotor flywheel;
    Servo hoodAdjuster;
    Servo gapAdjuster;
    double power = 1000;
    double hoodPose = 0;
    double maxHood = 25;
    double minHood = -25;
    double minGap = -10;
    double maxGap = 10;
    Gamepad gamepad2;
    double gapPose;

    public AdjustableOuttake(HardwareMap hardwareMap, String fName, String hName, String gName) {
        flywheel = hardwareMap.get(DcMotor.class, fName);
        hoodAdjuster = hardwareMap.get(Servo.class, hName);
        gapAdjuster = hardwareMap.get(Servo.class, gName);
    }

    public void runFlywheel() {
        if (gamepad2.rightTriggerWasReleased()) {
            power += 50;
        } else if (gamepad2.rightBumperWasReleased()) {
            power -= 50;
        }
        flywheel.setPower(power);
    }

    public void runHoodAdjuster() {
        if (gamepad2.aWasReleased()) {
            hoodPose += 5;
        } else if (gamepad2.bWasReleased()) {
            hoodPose -= 5;
        }
        hoodPose = Math.min(Math.max(hoodPose, minHood), maxHood);
        hoodAdjuster.setPosition(hoodPose);
    }

    public void runGapAdjuster() {
        if (gamepad2.dpadDownWasReleased()) {
            gapPose -= 10;
        } else if (gamepad2.dpadUpWasReleased()) {
            gapPose += 10;
        }
        gapPose = Math.min(Math.max(gapPose, minGap), maxGap);
        gapAdjuster.setPosition(gapPose);

    }

    @Override
    public void run(Boolean parameter) {
        runFlywheel();
        runHoodAdjuster();
        runGapAdjuster();
    }

    @Override
    public void stop() {

    }

    @Override
    public void postTelemetry(Telemetry telemetry) {

    }
}
