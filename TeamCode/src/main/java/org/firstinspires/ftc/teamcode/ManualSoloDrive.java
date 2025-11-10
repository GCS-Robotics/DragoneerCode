package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleOp Drive (1P)")
public class ManualSoloDrive extends LinearOpMode {
    ManualDrumDrive masterDrive;
    @Override
    public void runOpMode() throws InterruptedException {
        masterDrive = new ManualDrumDrive(hardwareMap, telemetry);
        waitForStart();
        while(opModeIsActive()){
            masterDrive.runDrive(gamepad1);
            masterDrive.runIntake(gamepad1.left_bumper, false);
            masterDrive.runOuttake(gamepad1.right_bumper, false);
            masterDrive.runDrum(gamepad1.aWasReleased(), gamepad1.bWasReleased());
            if(gamepad1.dpadRightWasReleased())masterDrive.deployKicker();
            if(gamepad1.dpadLeftWasReleased()) masterDrive.returnKicker();
            if(gamepad1.dpadUpWasReleased() && masterDrive.getLaunchSpeed() < 1){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()+0.1);
            }
            if(gamepad1.dpadDownWasReleased() && masterDrive.getLaunchSpeed() > 0){
                masterDrive.setLaunchSpeed(masterDrive.getLaunchSpeed()-0.1);
            }
            if(gamepad1.xWasReleased() && masterDrive.getDrumSpeed() < 1){
                masterDrive.setDrumSpeed(masterDrive.getDrumSpeed()+0.1);
            }
            if(gamepad1.yWasReleased() && masterDrive.getDrumSpeed() > 0){
                masterDrive.setDrumSpeed(masterDrive.getDrumSpeed()-0.1);
            }
            telemetry.update();
        }
    }
}
