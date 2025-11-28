package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MainDecodeDrive;

@TeleOp(name = "Main Drive (2P)", group = "Main Drive")
public class MainDuoDrive extends LinearOpMode {
    MainDecodeDrive masterDrive;
    @Override
    public void runOpMode() throws InterruptedException {
        masterDrive = new MainDecodeDrive(hardwareMap, telemetry);
        waitForStart();
        while(opModeIsActive()){
            masterDrive.runDrive(gamepad1);
            masterDrive.runIntake(gamepad2.left_trigger > masterDrive.getDeadzone(), false);
            masterDrive.runOuttake(
                    gamepad2.aWasReleased(), // Prime the Launch
                    gamepad2.bWasReleased(), // Cancel the Launch
                    gamepad2.leftBumperWasReleased(), // Fire a Purple
                    gamepad2.rightBumperWasReleased()); // Fire a Green
            // Crank Up Launch Speed
            if(gamepad2.dpadUpWasReleased() && masterDrive.getLaunchSpeed() < 1){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()+0.1);
            }
            // Crank Down Launch Speed
            if(gamepad2.dpadDownWasReleased() && masterDrive.getLaunchSpeed() > 0){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()-0.1);
            }
            telemetry.update();
        }
    }
}
