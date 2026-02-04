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
    PIDFController[] launchControls;
    private final DcMotorEx launcherLeft;
    private final DcMotorEx launcherRight;
    DcMotorEx[] shooters;
    public double targetRPM = 2000;
    private final double TICKS_PER_REV = 28;
    double[][] pidf = new double[][]{
            {0.03, 0.05, 0, 1.0 / 3000},
            {0.02, 0.05, 0, 1.0 / 3000}};
    public States.Outtake state = States.Outtake.IDLE;
    public Flywheels(HardwareMap hardwareMap){
        PIDFController launchControls1 = new PIDFController(pidf[0][0], pidf[0][1], pidf[0][2], pidf[0][3]);
        PIDFController launchControls2 = new PIDFController(pidf[1][0], pidf[1][1], pidf[1][2], pidf[1][3]);
        launchControls = new PIDFController[]{launchControls1, launchControls2};
        launcherRight = hardwareMap.get(DcMotorEx.class, "launcherRight");
        launcherRight.setDirection(DcMotorSimple.Direction.REVERSE);
        launcherRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launcherLeft = hardwareMap.get(DcMotorEx.class, "launcherLeft");
        launcherLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooters = new DcMotorEx[]{launcherLeft, launcherRight};
    }
    public void prime(){
        if(state == States.Outtake.IDLE){
            state = States.Outtake.PRIMED;
        }
    }
    public void cancel(){
        if(state != States.Outtake.IDLE){
            state = States.Outtake.BRAKING;
        }
    }
    public boolean doRPM = true;
    public void rpmFromDistance(double distance_to_goal){
        if(!doRPM){
            return;
        }
        targetRPM = 258.4*distance_to_goal+879.14;
    }
    @Override
    public void run(boolean running){
        for (int i = 0; i < shooters.length; i++) {
            DcMotorEx shooter = shooters[i];
            double outputPower = 0;
            double currentRPM = ticksPerSecondToRPM(shooter.getVelocity());
            if (active()) {
                outputPower = launchControls[i].calculate(currentRPM, targetRPM);
                if (state == States.Outtake.PRIMED && launchersAtSpeed()) {
                    state = States.Outtake.READY;
                } else {
                    state = States.Outtake.PRIMED;
                }
            } else if (state == States.Outtake.BRAKING) {
                outputPower = launchControls[i].calculate(currentRPM, 0);
                if (launchersAtSpeed(0)) {
                    state = States.Outtake.IDLE;
                    outputPower = 0;
                }
            }
            outputPower = Math.max(-1, Math.min(1, outputPower));
            shooter.setPower(outputPower);
        }
    }
    @Override
    public void postTelemetry(Telemetry telemetry){
        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Left RPM", ticksPerSecondToRPM(launcherLeft.getVelocity()));
        telemetry.addData("Right RPM", ticksPerSecondToRPM(launcherRight.getVelocity()));
    }
    private double ticksPerSecondToRPM(double tps) {
        return tps * 60.0 / TICKS_PER_REV;
    }
    public boolean launchersAtSpeed(double target) {
        double rightRpm = abs(ticksPerSecondToRPM(launcherRight.getVelocity()));
        double leftRpm  = abs(ticksPerSecondToRPM(launcherLeft.getVelocity()));

        double target_mod = 50;

        double rightDist = rightRpm - target;
        double leftDist = leftRpm - target;

        boolean rightDone = abs(rightDist) < target_mod; //TRUE if rightWheel in speed range
        boolean leftDone  = abs(leftDist) < target_mod; // TRUE if leftWheel in speed Range
        boolean flywheelsClose = (leftDist >= 0) && (rightDist >= 0); //TRUE if zz

        return rightDone && leftDone && flywheelsClose;
    }
    public boolean launchersAtSpeed() {
        return launchersAtSpeed(targetRPM);
    }
    public boolean active(){
        return state == States.Outtake.PRIMED || state == States.Outtake.READY;
    }
}
