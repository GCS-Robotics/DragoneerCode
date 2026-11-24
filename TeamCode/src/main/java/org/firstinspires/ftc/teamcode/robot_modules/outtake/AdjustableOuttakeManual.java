package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;

public class AdjustableOuttakeManual extends RunModule<Gamepad> {
    Servo hoodAdjuster;
    Servo gapAdjuster;
    DcMotorEx yawAdjuster;
    Flywheel flywheel;
    final int TICKS_PER_REV = 1425;
    int yawPosition = 0;
    double hoodPose = 0;
    private double[] pidf = new double[]{0.003, 0.123, 0, 0};
    private final double HOOD_ADJUST = 0.1;
    private final double POLLEN_GAP = 0;
    private final double NECTAR_GAP = 1;
    double gapPose;
    private boolean isSpinning = false;
    private final double FLYWHEEL_ADJUST_SPEED = 100; // RPM per second
    private final double STICK_DEADZONE = 0.1;
    private int minRPM = 1000;
    private int maxRPM = 5000;
    private long lastUpdateTime = 0;

    public AdjustableOuttakeManual(HardwareMap hardwareMap, String fName, String hName, String gName, String yName, String lName) {
        flywheel = new Flywheel(hardwareMap, fName);
        hoodAdjuster = hardwareMap.get(Servo.class, hName);
        gapAdjuster = hardwareMap.get(Servo.class, gName);
        yawAdjuster = hardwareMap.get(DcMotorEx.class, yName);
        flywheel.setPIDF(pidf);
    }
    public void runHoodAdjuster(Gamepad gamepad) {
        if (gamepad.aWasReleased()) {
            hoodPose += HOOD_ADJUST;
        }
        if (gamepad.bWasReleased()) {
            hoodPose -= HOOD_ADJUST;
        }
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
            yawPosition += TICKS_PER_REV / 20;
        }
        if (gamepad.dpadRightWasPressed()){
            yawPosition -= TICKS_PER_REV / 20;
        }
        yawAdjuster.setTargetPosition(yawPosition);
        yawAdjuster.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        yawAdjuster.setVelocity(2800);
    }

    public void runFlywheelAdjuster(Gamepad gamepad) {
        double stickY = -gamepad.left_stick_y;

        if (Math.abs(stickY) < STICK_DEADZONE) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0;
        lastUpdateTime = currentTime;

        int currentRPM = flywheel.getTargetRPM();

        int newRPM = (int) (currentRPM +
                stickY * FLYWHEEL_ADJUST_SPEED * deltaTime);

        newRPM = Math.max(minRPM, Math.min(maxRPM, newRPM));

        flywheel.setTargetRPM(newRPM);
    }

    @Override
    public void run(Gamepad gamepad) {
        if(gamepad.rightBumperWasPressed()){
            isSpinning = !isSpinning;
        }
        flywheel.run(isSpinning);
        runHoodAdjuster(gamepad);
        runGapAdjuster(gamepad);
        runYawAdjuster(gamepad);
        runFlywheelAdjuster(gamepad);
    }

    @Override
    public void stop() {
        yawAdjuster.setPower(0);
    }
    @Override
    public void postTelemetry(Telemetry telemetry) {
        telemetry.addData("Is Spinning?", isSpinning);
        telemetry.addData("Flywheel Target Power", flywheel.getTargetRPM());
        telemetry.addData("Hood Position", hoodPose);
        telemetry.addData("Gap Postition", gapPose);
        telemetry.addData("Yaw Position", yawPosition);
    }
}
