package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import static java.lang.Math.abs;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;

public class Flywheel extends RunModule<Boolean> {
    DcMotorEx flywheel;
    private final int TICKS_PER_REV = 28;
    private double[] pidf = new double[] {0.03, 0.05, 0, 0};
    PIDFController launchControls;
    private int targetRPM = 1000;
    private final int TARGET_LEEWAY = 10;
    public Flywheel(HardwareMap hardwareMap, String name){
        flywheel = hardwareMap.get(DcMotorEx.class, name);
        launchControls = new PIDFController(pidf[0], pidf[1], pidf[2], pidf[3]);
        flywheel = hardwareMap.get(DcMotorEx.class, "launcherRight");
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    @Override
    public void run(Boolean parameter) {
        DcMotorEx shooter = flywheel;
        double outputPower = 0;
        double currentRPM = ticksPerSecondToRPM(shooter.getVelocity());
        if (parameter) {
            outputPower = launchControls.calculate(currentRPM, targetRPM);
        } else {
            outputPower = launchControls.calculate(currentRPM, 0);
            if (launchersAtSpeed(0)) {
                outputPower = 0;
            }
        }
        outputPower = Math.max(-1, Math.min(1, outputPower));
        shooter.setPower(outputPower);
    }

    @Override
    public void stop() {
        flywheel.setPower(0);
    }

    @Override
    public void postTelemetry(Telemetry telemetry) {
        telemetry.addData("Flywheel Target", targetRPM);
        telemetry.addData("Flywheel Current", ticksPerSecondToRPM(flywheel.getVelocity()));
    }
    private double ticksPerSecondToRPM(double tps) {
        return tps * 60.0 / TICKS_PER_REV;
    }

    public boolean launchersAtSpeed(double target) {
        double flywheelRpm  = abs(ticksPerSecondToRPM(flywheel.getVelocity()));

        double flywheelDist = flywheelRpm - target;

        boolean flywheelDone  = abs(flywheelDist) < TARGET_LEEWAY;
        boolean flywheelClose = (flywheelDist >= 0);

        return flywheelDone && flywheelClose;
    }
    public double getTargetRPM(){
        return targetRPM;
    }
    public void setTargetRPM(int newTarget){targetRPM = newTarget;}
    public void setPIDF(double[] values){
        pidf = values;
        launchControls = new PIDFController(pidf[0], pidf[1], pidf[2], pidf[3]);
    }
}
