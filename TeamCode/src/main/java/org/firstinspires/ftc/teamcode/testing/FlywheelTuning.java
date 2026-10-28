package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.outtake.Flywheel;

@Config
@TeleOp(name = "Flywheel Tune", group = "Tuning")
public class FlywheelTuning extends LinearOpMode {
    Flywheel flywheel;
    public static double[] pidf = new double[]{0, 0, 0, 0};
    public static int targetRPM = 1000;
    private boolean run = false;
    private Telemetry dashTel;
    private double[] value_change = new double[]{0.001, 0.001, 0.001, 0.001, 100};
    private int value_changing = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        dashTel = FtcDashboard.getInstance().getTelemetry();
        flywheel = new Flywheel(hardwareMap, "outtake");
        waitForStart();
        while(opModeIsActive()){
            flywheel.run(run);
            if(gamepad1.aWasPressed() || gamepad2.aWasPressed()){
                run = !run;
            }
            if(gamepad1.rightBumperWasPressed()){
                value_changing += 1;
                if(value_changing > 4){
                    value_changing = 0;
                }
            }
            if(gamepad1.leftBumperWasPressed()){
                value_changing -= 1;
                if(value_changing < 0){
                    value_changing = 4;
                }
            }
            if(gamepad1.dpadUpWasPressed()){
                valueChange(1);
            }
            if(gamepad1.dpadDownWasPressed()){
                valueChange(-1);
            }
            for(Telemetry i : new Telemetry[]{telemetry, dashTel}){
                i.addData("Running?", run);
                i.addData("P", pidf[0]);
                i.addData("I", pidf[1]);
                i.addData("D", pidf[2]);
                i.addData("F", pidf[3]);
                i.addData("Selected Index", value_changing);
                flywheel.postTelemetry(i);
                i.update();
            }
        }
    }
    public void valueChange(int mult){
        if(value_changing >= 0 && value_changing < 4){
            pidf[value_changing] += value_change[value_changing]*mult;
            flywheel.setPIDF(pidf);
        }
        if(value_changing == 4){
            targetRPM += (int) (value_change[value_changing] * mult);
            flywheel.setTargetRPM(targetRPM);
        }
    }
}
