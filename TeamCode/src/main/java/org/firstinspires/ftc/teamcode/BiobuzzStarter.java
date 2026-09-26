package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "Robert's Biobuzz Test", group = "Starter Bot")
public class BiobuzzStarter extends LinearOpMode {
    private DcMotor leftDrive, rightDrive, intake, outtake;
    private CRServo leftIntake, rightIntake;
    @Override
    public void runOpMode() throws InterruptedException {
        // Hardware Mapping Motors
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        intake = hardwareMap.get(DcMotor.class, "intake");
        outtake = hardwareMap.get(DcMotor.class, "outtake");
        leftIntake = hardwareMap.get(CRServo.class, "left_intake_servo");
        rightIntake = hardwareMap.get(CRServo.class, "right_intake_servo");
        // Direction Configs
        rightIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        // Other Initialization Things
        leftIntake.setPower(0);
        rightIntake.setPower(0);
        // Waiting
        waitForStart();
        while(opModeIsActive()){
            drive(gamepad1);
            intake.setPower(gamepad1.left_trigger);
            outtake.setPower(gamepad1.right_trigger);
            if(gamepad1.a){
                leftIntake.setPower(1);
                rightIntake.setPower(1);
            }
        }
    }
    private void drive(Gamepad gamepad){
        float forward = -gamepad.left_stick_y;
        float rotate = gamepad1.right_stick_x;
        leftDrive.setPower(forward+rotate);
        rightDrive.setPower(forward-rotate);
    }
}
