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
    private final PIDFController launchControls1;
    private final PIDFController launchControls2;
    PIDFController[] launchControls;
    private final DcMotorEx launcherLeft;
    private final DcMotorEx launcherRight;
    public double targetRPM = 2000;
    private final double TICKS_PER_REV = 28;
    private final double[][] pidf = new double[][]{
            {0.005, 0.05, 0.00003, 0},
            {0.005, 0.05, 0.00003, 0}};
    public States.Outtake state = States.Outtake.IDLE;
    public Flywheels(HardwareMap hardwareMap){
        launchControls1 = new PIDFController(pidf[0][0], pidf[0][1], pidf[0][2], pidf[0][3]);
        launchControls2 = new PIDFController(pidf[1][0], pidf[1][1], pidf[1][2], pidf[1][3]);
        launchControls = new PIDFController[]{launchControls1, launchControls2};
        launcherRight = hardwareMap.get(DcMotorEx.class, "launcherRight");
        launcherRight.setDirection(DcMotorSimple.Direction.REVERSE);
        launcherRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launcherLeft = hardwareMap.get(DcMotorEx.class, "launcherLeft");
        launcherLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
    public void rpmFromDistance(double distance_to_goal){

    }
    @Override
    public void run(boolean running){
        DcMotorEx[] shooters = new DcMotorEx[]{launcherLeft, launcherRight};
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
    public boolean active(){
        return state == States.Outtake.PRIMED || state == States.Outtake.READY;
    }
}
