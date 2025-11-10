package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Manual Drive (2P)", group = "Manual Drum")
public class ManualDuoDrive extends LinearOpMode {
    ManualDrumDrive masterDrive;
    @Override
    public void runOpMode() throws InterruptedException {
        masterDrive = new ManualDrumDrive(hardwareMap, telemetry);
        waitForStart();
        while(opModeIsActive()){
            masterDrive.runDrive(gamepad1);
            masterDrive.runIntake(gamepad2.left_trigger > masterDrive.getDeadzone(), false);
            masterDrive.runOuttake(gamepad2.right_trigger  > masterDrive.getDeadzone(), false);
            masterDrive.runDrum(gamepad2.aWasReleased(), gamepad2.bWasReleased());
            if(gamepad2.dpadRightWasReleased())masterDrive.deployKicker();
            if(gamepad2.dpadLeftWasReleased()) masterDrive.returnKicker();
            if(gamepad2.dpadUpWasReleased() && masterDrive.getLaunchSpeed() < 1){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()+0.1);
            }
            if(gamepad2.dpadDownWasReleased() && masterDrive.getLaunchSpeed() > 0){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()-0.1);
            }
            if(gamepad2.xWasReleased() && masterDrive.getDrumSpeed() < 1){
                masterDrive.setDrumSpeed(masterDrive.getDrumSpeed()+0.1);
            }
            if(gamepad2.yWasReleased() && masterDrive.getDrumSpeed() > 0){
                masterDrive.setDrumSpeed(masterDrive.getDrumSpeed()-0.1);
            }
            telemetry.update();
        }
    }
}
