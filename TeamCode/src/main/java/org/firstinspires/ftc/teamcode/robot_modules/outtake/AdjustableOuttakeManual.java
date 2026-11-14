package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;
import org.firstinspires.ftc.teamcode.robot_modules.sensors.Limelight;

public class AdjustableOuttakeManual extends RunModule<Gamepad> {
    Servo hoodAdjuster;
    Servo gapAdjuster;
    DcMotorEx yawAdjuster;
    Flywheel flywheel;
    double yawPosition = 0;
    double hoodPose = 0;
    private final double MAX_HOOD = 1;
    private final double MIN_HOOD = 0;
    private final double HOOD_ADJUST = 0.1;
    private final double POLLEN_GAP = 0;
    private final double NECTAR_GAP = 1;
    double gapPose;
    private boolean isSpinning = false;

    Limelight limelight;

    public AdjustableOuttakeManual(HardwareMap hardwareMap, String fName, String hName, String gName, String yName, String lName) {
        flywheel = new Flywheel(hardwareMap, fName);
        limelight = new Limelight(hardwareMap, lName);
        hoodAdjuster = hardwareMap.get(Servo.class, hName);
        gapAdjuster = hardwareMap.get(Servo.class, gName);
        yawAdjuster = hardwareMap.get(DcMotorEx.class, yName);
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
            yawAdjuster.setTargetPosition((int) (yawPosition + ((limelight.getYaw() * 2.75) / 100)) * 28);
            yawPosition = yawAdjuster.getTargetPosition();
        } else {
            yawAdjuster.setTargetPosition((int) (gamepad.left_stick_x * 2.75) * 28);
        }
        yawAdjuster.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        yawAdjuster.setVelocity(6000);
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
