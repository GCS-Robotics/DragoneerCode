package org.firstinspires.ftc.teamcode.resources.base_function;

import static java.lang.Math.abs;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Flywheels extends Mechanism{
    private PIDFController launchControls;
    private DcMotorEx launcherLeft;
    private DcMotorEx launcherRight;
    private double targetRPM = 2000;
    private double TICKS_PER_REV = 28;
    private boolean primed = false;
    private double[] pidf = new double[]{0.005, 0.05, 0.00003, 0};
    public Flywheels(HardwareMap hardwareMap){
        launchControls = new PIDFController(pidf[0], pidf[1], pidf[2], pidf[3]);
        launcherRight = hardwareMap.get(DcMotorEx.class, "launcherRight");
        launcherRight.setDirection(DcMotorSimple.Direction.REVERSE);
        launcherRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launcherLeft = hardwareMap.get(DcMotorEx.class, "launcherLeft");
        launcherLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void setTargetRPM(double target){
        targetRPM = target;
    }
    public double getTargetRPM(){
        return targetRPM;
    }
    public void prime(){
        primed = true;
    }
    public boolean isPrimed(){ return primed;}
    public void cancel(){
        primed = false;
    }
    @Override
    public void run(boolean running){
        DcMotorEx[] shooters = new DcMotorEx[]{launcherLeft, launcherRight};
        for(int i = 0; i < shooters.length; i++){
            DcMotorEx shooter = shooters[i];
            double outputPower = 0;
            double currentRPM = ticksPerSecondToRPM(shooter.getVelocity());
            if (primed) {
                outputPower = launchControls.calculate(currentRPM, targetRPM);
            }
            shooter.setPower(outputPower);
        }
    }
    @Override
    public void postTelemetry(Telemetry telemetry){
        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Left RPM", ticksPerSecondToRPM(launcherLeft.getVelocity()));
        telemetry.addData("Right RPM", ticksPerSecondToRPM(launcherRight.getVelocity()));
        telemetry.addLine();
        telemetry.addData("Spinners at Speed", launchersAtSpeed());
    }
    private double ticksPerSecondToRPM(double tps) {
        return tps * 60.0 / TICKS_PER_REV;
    }
    public boolean launchersAtSpeed() {
        double rightRpm = abs(ticksPerSecondToRPM(launcherRight.getVelocity()));
        double leftRpm  = abs(ticksPerSecondToRPM(launcherLeft.getVelocity()));

        boolean rightDone = abs(rightRpm - targetRPM) < 100;
        boolean leftDone  = abs(leftRpm  - targetRPM) < 100;

        return rightDone || leftDone; // It's this bad only because our tuning lowkey sucks.
    }
}
