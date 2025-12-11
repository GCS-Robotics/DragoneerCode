package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.resources.MainDecodeDrive;

@TeleOp(name = "Main Drive (2P)", group = "Main Drive")
public class MainDuoDrive extends LinearOpMode {
    MainDecodeDrive masterDrive;
    @Override
    public void runOpMode() throws InterruptedException {
        masterDrive = new MainDecodeDrive(hardwareMap, telemetry, FtcDashboard.getInstance().getTelemetry());
        waitForStart();
        while(opModeIsActive()){
            masterDrive.postTelemetry();
            masterDrive.runDrive(gamepad1);
            masterDrive.runIntake(gamepad2.left_trigger > masterDrive.getDeadzone(), false);
            masterDrive.runOuttake(
                    gamepad2.startWasReleased(), // Prime the Launch
                    gamepad2.backWasReleased(), // Cancel the Launch
                    gamepad2.xWasPressed(), // Prepare a Purple
                    gamepad2.aWasReleased()); // Prepare a Green
            if(gamepad2.y){
                masterDrive.runKicker(gamepad2.y);
            }
            // Crank Up Launch Speed
            if(gamepad2.dpadUpWasReleased()){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()+50);
            }
            // Crank Down Launch Speed
            if(gamepad2.dpadDownWasReleased() && masterDrive.getLaunchSpeed() > 0){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()-50);
            }
            masterDrive.runDrum();
            masterDrive.postTelemetry();
        }
    }
}
