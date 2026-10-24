package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.Module;

public class AdjustableOuttake extends Module<Gamepad> {
    Servo hoodAdjuster;
    Servo gapAdjuster;
    Flywheel flywheel;
    double hoodPose = 0;
    private final double MAX_HOOD = 25;
    private final double MIN_HOOD = -25;
    private final double MIN_GAP = 0;
    private final double MAX_GAP = .5;
    double gapPose;
    private boolean isSpinning = false;

    public AdjustableOuttake(HardwareMap hardwareMap, String fName, String hName, String gName) {
        flywheel = new Flywheel(hardwareMap, fName);
        hoodAdjuster = hardwareMap.get(Servo.class, hName);
        gapAdjuster = hardwareMap.get(Servo.class, gName);
    }
    public void runHoodAdjuster(Gamepad gamepad) {
        if (gamepad.aWasReleased()) {
            hoodPose += 5;
        }
        if (gamepad.bWasReleased()) {
            hoodPose -= 5;
        }
        hoodPose = Math.min(Math.max(hoodPose, MIN_HOOD), MAX_HOOD);
        hoodAdjuster.setPosition(hoodPose);
    }

    public void runGapAdjuster(Gamepad gamepad) {
        if (gamepad.dpadDownWasReleased()) {
            gapPose -= 10;
        }
        if (gamepad.dpadUpWasReleased()) {
            gapPose += 10;
        }
        gapPose = Math.min(Math.max(gapPose, MIN_GAP), MAX_GAP);
        gapAdjuster.setPosition(gapPose);
    }

    @Override
    public void run(Gamepad gamepad) {
        if(gamepad.xWasPressed()){
            isSpinning = !isSpinning;
        }
        flywheel.run(isSpinning);
        runHoodAdjuster(gamepad);
        runGapAdjuster(gamepad);
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
