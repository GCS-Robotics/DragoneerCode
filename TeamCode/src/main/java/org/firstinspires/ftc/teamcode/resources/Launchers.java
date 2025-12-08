package org.firstinspires.ftc.teamcode.resources;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Launchers {
    private PIDFController launchControls;
    private DcMotorEx launcherLeft;
    private DcMotorEx launcherRight;
    private double targetRPM = 1000;
    private double TICKS_PER_REV = 28;
    private boolean run = false;
    public Launchers(HardwareMap hardwareMap){
        launchControls = new PIDFController(0, 0, 0, 6000);
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

                shooter.setPower(outputPower);
            } else {
                shooter.setPower(0);
            }
        }
    }
    private double ticksPerSecondToRPM(double tps) {
        return tps * 60.0 / TICKS_PER_REV;
    }
}
