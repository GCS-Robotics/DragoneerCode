package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

public class  DecodeDrive extends LinearOpMode {
    // Define any variables here!
    // Also make variables for hardware, but don't set it yet.
    double tempSpeed = 1.0;
    double permSpeed = 1.0;
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
    DcMotor[] driveMotors = {leftFront, rightFront, leftRear, rightRear};
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
        for (DcMotor motor : driveMotors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
        }
        waitForStart();
        while (opModeIsActive()) {
            // Do driving action here.

            /// Sets the speed double to the value.
            // Sets the permSpeed double to the correct value.
            if (gamepad1.a) {
                permSpeed = 1.0;
            } else if (gamepad1.b) {
                permSpeed = 0.5;
            } else if (gamepad1.y) {
                permSpeed = 0.75;
            } else if (gamepad1.x) {
                permSpeed = 0.25;
            }

            // If only right trigger is pressed.
            if (gamepad1.right_trigger > 0 && gamepad1.left_trigger == 0) {
                tempSpeed = (1 - (gamepad1.right_trigger / 1.4));
                // Set speed to valid values.
                if (tempSpeed <= 0.1) {
                    tempSpeed = 0.1;
                } else if (tempSpeed >= 1.0) {
                    tempSpeed = 1;
                }
            // If only the left trigger is pressed.
            } else if (gamepad1.right_trigger == 0 && gamepad1.left_trigger > 0) {
                tempSpeed = (1 - (gamepad1.left_trigger / 1.4));
                // Set speed to valid values.
                if (tempSpeed <= 0.1) {
                    tempSpeed = 0.1;
                } else if (tempSpeed >= 1.0) {
                    tempSpeed = 1;
                }
            } else {
                tempSpeed = permSpeed;
            }

            speed = tempSpeed;

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
        }
    }
}
