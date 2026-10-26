package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot_modules.outtake.Flywheel;

@Config
public class FlywheelTuning extends LinearOpMode {
    Flywheel flywheel;
    public static double kP;
    private double lastkP;
    public static double kI;
    private double lastkI;
    public static double kD;
    private double lastkD;
    public static double kF;
    private double lastkF;
    private boolean run = false;
    @Override
    public void runOpMode() throws InterruptedException {
        setLastConstant();
        flywheel = new Flywheel(hardwareMap, "outtake");
        waitForStart();
        if(opModeIsActive()) while(opModeIsActive()){
            if(valuesChanged()){
                flywheel.setPIDF(new double[]{kP, kI, kD, kF});
            }
            flywheel.run(run);
            if(gamepad1.aWasPressed() || gamepad2.aWasPressed()){
                run = !run;
            }
            telemetry.addData("Running?", run);
            flywheel.postTelemetry(telemetry);
            telemetry.update();
        }
    }
    private void setLastConstant(){
        lastkP = kP;
        lastkI = kI;
        lastkD = kD;
        lastkF = kF;
    }
    private boolean valuesChanged(){
        return (lastkP != kP) || (lastkI != kI) || (lastkD != kD) || (lastkF != kF);
    }
}
