package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import static java.lang.Math.toDegrees;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

public class DecodeDrive extends LinearOpMode {
    // Define any variables here!
    // Also make variables for hardware, but don't set it yet.

    double currentPositionRotation = (0);
    final double fullRotation = (360);
    double target = 0;
    double speed = 1.0;
    // Drive motors
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;
    // Launcher motors
    DcMotor launcherRight;
    DcMotor launcherLeft;
    // Intake motor
    DcMotor intake;
    //Drum motor

    DcMotor drum;
    DcMotor[] driveMotors = {leftFront, rightFront, leftRear, rightRear};
    DcMotor[] DrumMotors = {drum};
    MecanumDrive drive = new MecanumDrive(leftFront, rightFront, leftRear, rightRear, 1, false, false, false, false);

    @Override
    public void runOpMode() throws InterruptedException {
        // Do hardware map here!

        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        launcherRight = hardwareMap.dcMotor.get("launcherRight");
        launcherLeft = hardwareMap.dcMotor.get("launcherLeft");
        intake = hardwareMap.dcMotor.get("intake");
        drum = hardwareMap.dcMotor.get("drum");
        for (DcMotor motor : driveMotors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setPower(0);
        }

        drum.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drum.setTargetPosition(0);
        drum.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        drum.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        drum.setPower(0.7);

        while (opModeIsActive()) {
            // Do driving action here!
            speed = (1 - (gamepad1.right_trigger / 1.4));
            if (speed <= 0.1) {
                speed = 0.1;
            }
        }
        if (abs(gamepad1.left_stick_y) + abs(gamepad1.left_stick_x) > 0.4) {
            // ANYTHING with left stick
            if (gamepad1.left_stick_y > 0) {
                drive.moveForward(speed);

            } else if (gamepad1.left_stick_y < 0) {
                drive.moveBackward(speed);
            } else if (gamepad1.left_stick_x > 0) {
                drive.moveRight(speed);
            } else if (gamepad1.left_stick_x < 0) {
                drive.moveLeft(speed);
            }
        } else if (abs(gamepad1.right_stick_y) + abs(gamepad1.right_stick_y) > 0.4) {
            // ANYTHING with right stick
            if (gamepad1.right_stick_x > 0) {
                drive.turnRightTank(speed);

            } else if (gamepad1.right_stick_x < 0) {
                drive.turnLeftTank(speed);
            }
        } else {
            drive.stop();
        }

        // Launcher
        if (gamepad2.a) {
            launcherRight.setPower(1);
            launcherLeft.setPower(1);
        } else {
            launcherRight.setPower(0);
            launcherLeft.setPower(0);
        }

        // Intake
        if (gamepad2.b) {
            intake.setPower(0.5);
        } else {
            intake.setPower(0);
        }

        // Drum (Robert's Idea)
        if(gamepad2.xWasReleased()){
            drum.setTargetPosition((int)(fullRotation/3) + drum.getCurrentPosition());
        }
    }
}



