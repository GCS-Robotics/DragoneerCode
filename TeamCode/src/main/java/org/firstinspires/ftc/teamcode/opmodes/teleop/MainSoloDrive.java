package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.resources.MainDecodeDrive;

@TeleOp(name = "Main Drive (1P)", group = "Main Drive")
public class MainSoloDrive extends LinearOpMode {
    MainDecodeDrive masterDrive;
    @Override
    public void runOpMode() throws InterruptedException {
        masterDrive = new MainDecodeDrive(hardwareMap, telemetry, FtcDashboard.getInstance().getTelemetry());
        waitForStart();
        while(opModeIsActive()){
            masterDrive.postTelemetry();
            masterDrive.runDrive(gamepad1);
            masterDrive.runIntake(gamepad1.left_bumper, false);
            masterDrive.runOuttake(
                    gamepad1.startWasReleased(), // Prime the Launch
                    gamepad1.backWasReleased(), // Cancel the Launch
                    gamepad1.xWasPressed(), // Prepare a Purple
                    gamepad1.aWasReleased()); // Prepare a Green
            masterDrive.runKicker(gamepad1.yWasPressed());
            // Crank Up Launch Speed
            if(gamepad1.dpadUpWasReleased()){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()+10);
            }
            // Crank Down Launch Speed
            if(gamepad1.dpadDownWasReleased() && masterDrive.getLaunchSpeed() > 0){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()-10);
            }
            masterDrive.postTelemetry();
        }
    }
}
