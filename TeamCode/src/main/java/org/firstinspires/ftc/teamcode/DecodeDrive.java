package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class DecodeDrive extends LinearOpMode {
    // Variables for Stuff
    double SPEED = 1.0;
    double DEADZONE = 0.4;
    // Drive Motors
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;
    // Launcher Motors
    DcMotor launcherRight;
    DcMotor launcherLeft;
    // Intake Motor
    DcMotor intake;
    // Extra Tools
    DcMotor[] allMotors = {leftFront, rightFront, leftRear, rightRear, launcherRight, launcherLeft};
    MecanumDrive drive = new MecanumDrive(leftFront, rightFront, leftRear, rightRear, 1, false, false, false, false);

    @Override
    public void runOpMode() throws InterruptedException {
        // Hardware Map
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        launcherRight = hardwareMap.dcMotor.get("launcherRight");
        launcherLeft = hardwareMap.dcMotor.get("launcherLeft");
        // Setting Motor Stuff
        for (DcMotor motor : allMotors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
        }
        waitForStart();
        // The Actual OpMode
        while (opModeIsActive()) {
            /// Gamepad 1
            // Variable Speed Control
            SPEED = (1 - (gamepad1.right_trigger / 1.4));
            if (SPEED <= 0.1) {
                SPEED = 0.1;
            }
            // Left-Stick Movement
            if (abs(gamepad1.left_stick_y) + abs(gamepad1.left_stick_x) > DEADZONE) {
                if (gamepad1.left_stick_y > 0) {
                    drive.moveForward(SPEED);

                } else if (gamepad1.left_stick_y < 0) {
                    drive.moveBackward(SPEED);
                } else if (gamepad1.left_stick_x > 0) {
                    drive.moveRight(SPEED);
                } else if (gamepad1.left_stick_x < 0) {
                    drive.moveLeft(SPEED);
                }
            }
            // Right-Stick Movement
            else if (abs(gamepad1.right_stick_y) + abs(gamepad1.right_stick_y) > DEADZONE) {
                if (gamepad1.right_stick_x > 0) {
                    drive.turnRightTank(SPEED);
                } else if (gamepad1.right_stick_x < 0) {
                    drive.turnLeftTank(SPEED);
                }
            // No Movement
            } else {
                drive.stop();
            }
            /// Gamepad 2
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
