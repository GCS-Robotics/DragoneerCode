package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Main Drive (1P)", group = "Main Drive")
public class MainSoloDrive extends LinearOpMode {
    MainDecodeDrive masterDrive;
    @Override
    public void runOpMode() throws InterruptedException {
        masterDrive = new MainDecodeDrive(hardwareMap, telemetry);
        waitForStart();
        while(opModeIsActive()){
            masterDrive.runDrive(gamepad1);
            masterDrive.runIntake(gamepad1.left_trigger > masterDrive.getDeadzone(), false);
            masterDrive.runOuttake(
                    gamepad1.aWasReleased(), // Prime the Launch
                    gamepad1.bWasReleased(), // Cancel the Launch
                    gamepad1.leftBumperWasReleased(), // Fire a Purple
                    gamepad1.rightBumperWasReleased()); // Fire a Green
            // Crank Up Launch Speed
            if(gamepad1.dpadUpWasReleased() && masterDrive.getLaunchSpeed() < 1){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()+0.1);
            }
            // Crank Down Launch Speed
            if(gamepad1.dpadDownWasReleased() && masterDrive.getLaunchSpeed() > 0){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()-0.1);
            }
        }
    }
}
