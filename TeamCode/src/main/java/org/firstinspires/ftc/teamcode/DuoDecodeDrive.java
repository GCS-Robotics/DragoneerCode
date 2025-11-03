package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleOp Drive (2P)")
public class DuoDecodeDrive extends LinearOpMode {
    MasterDecodeDrive masterDrive;
    @Override
    public void runOpMode() throws InterruptedException {
        masterDrive = new MasterDecodeDrive(hardwareMap, telemetry);
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
