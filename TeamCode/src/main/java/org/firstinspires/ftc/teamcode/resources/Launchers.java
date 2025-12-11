package org.firstinspires.ftc.teamcode.resources;

import static java.lang.Math.abs;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Launchers {
    private PIDFController launchControls;
    private DcMotorEx launcherLeft;
    private DcMotorEx launcherRight;
    private double targetRPM;
    private double TICKS_PER_REV = 28;
    private boolean run = false;
    private double[] pidf = new double[]{0.005, 0.05, 0.00003, 0};
    public Launchers(HardwareMap hardwareMap){
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
        run = true;
    }
    public void cancel(){
        run = false;
    }
    public void runLauncher(){
        DcMotorEx[] shooters = new DcMotorEx[]{launcherLeft, launcherRight};
        for(int i = 0; i < shooters.length; i++){
            DcMotorEx shooter = shooters[i];
            double outputPower = 0;
            double currentRPM = ticksPerSecondToRPM(shooter.getVelocity());
            if (run) {
                outputPower = launchControls.calculate(currentRPM, targetRPM);
            }
            shooter.setPower(outputPower);
        }
    }
    private double ticksPerSecondToRPM(double tps) {
        return tps * 60.0 / TICKS_PER_REV;
    }
    public boolean launchersAtSpeed() {
        double rightRpm = Math.abs(ticksPerSecondToRPM(launcherRight.getVelocity()));
        double leftRpm  = Math.abs(ticksPerSecondToRPM(launcherLeft.getVelocity()));

        boolean rightDone = Math.abs(rightRpm - targetRPM) < 100;
        boolean leftDone  = Math.abs(leftRpm  - targetRPM) < 100;

        return rightDone && leftDone;
    }

    public void launchTelemetry(Telemetry telemetry){
        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Left RPM", ticksPerSecondToRPM(launcherLeft.getVelocity()));
        telemetry.addData("Right RPM", ticksPerSecondToRPM(launcherRight.getVelocity()));
    }
}
