package org.firstinspires.ftc.teamcode.resources.base_function;

import static java.lang.Math.abs;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.resources.States;

public class Flywheels extends Mechanism{
    private PIDFController launchControls;
    private DcMotorEx launcherLeft;
    private DcMotorEx launcherRight;
    private double targetRPM = 2000;
    private double TICKS_PER_REV = 28;
    private boolean primed = false;
    private double[] pidf = new double[]{0.005, 0.05, 0.00003, 0};
    public States.Outtake state = States.Outtake.IDLE;
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
        state = States.Outtake.PRIMED;
    }
    public boolean isPrimed(){ return primed;}
    public void cancel(){
        primed = false;
        state = States.Outtake.BRAKING;
    }
    @Override
    public void run(boolean running){
        DcMotorEx[] shooters = new DcMotorEx[]{launcherLeft, launcherRight};
        for(int i = 0; i < shooters.length; i++){
            DcMotorEx shooter = shooters[i];
            double outputPower = 0;
            double currentRPM = ticksPerSecondToRPM(shooter.getVelocity());
            if (state == States.Outtake.PRIMED || state == States.Outtake.READY) {
                outputPower = launchControls.calculate(currentRPM, targetRPM);
                if(launchersAtSpeed()){
                    state = States.Outtake.READY;
                } else {
                    state = States.Outtake.PRIMED;
                }
            } else if (state == States.Outtake.BRAKING){
                outputPower = launchControls.calculate(currentRPM, 0);
                if(launchersAtSpeed(0)){
                    state = States.Outtake.IDLE;
                }
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
        telemetry.addData("Spinner State", state);
    }
    private double ticksPerSecondToRPM(double tps) {
        return tps * 60.0 / TICKS_PER_REV;
    }
    public boolean launchersAtSpeed(double target) {
        double rightRpm = abs(ticksPerSecondToRPM(launcherRight.getVelocity()));
        double leftRpm  = abs(ticksPerSecondToRPM(launcherLeft.getVelocity()));

        boolean rightDone = abs(rightRpm - target) < 100;
        boolean leftDone  = abs(leftRpm  - target) < 100;

        return rightDone || leftDone; // It's this bad only because our tuning lowkey sucks.
    }
    public boolean launchersAtSpeed() {
        return launchersAtSpeed(targetRPM);
    }
}
