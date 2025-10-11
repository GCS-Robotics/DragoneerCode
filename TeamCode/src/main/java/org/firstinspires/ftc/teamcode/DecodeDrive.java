package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

public class DecodeDrive extends LinearOpMode {
    // Define any variables here!
    // Also make variables for hardware, but don't set it yet.
    double speed = 1.0;

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;
    DcMotor[] driveMotors = {leftFront, rightFront, leftRear, rightRear};
    MecanumDrive drive = new MecanumDrive(leftFront, rightFront, leftRear, rightRear, 1, false, false, false, false);
    @Override
    public void runOpMode() throws InterruptedException {
        // Do hardware map here!

        leftFront=hardwareMap.dcMotor.get("leftFront");
        rightFront=hardwareMap.dcMotor.get("rightFront");
        leftRear=hardwareMap.dcMotor.get("leftRear");
        rightRear=hardwareMap.dcMotor.get("rightRear");
        for(DcMotor motor : driveMotors){
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
        }
        waitForStart();
        while(opModeIsActive()){
            // Do driving action here!
            speed = (1 - (gamepad1.right_trigger/1.4));
            if (speed <= 0.1) {
                speed = 0.1;
            }
            }
            if (abs(gamepad1.left_stick_y)+abs(gamepad1.left_stick_x) > 0.4) {
                // ANYTHING with left stick
                if (gamepad1.left_stick_y > 0) {
                    drive.moveForward(speed);

                } else if (gamepad1.left_stick_y < 0) {
                drive.moveBackward(speed);
                }else if(gamepad1.left_stick_x > 0) {
                    drive.moveRight(speed);
                }else if(gamepad1.left_stick_x < 0) {
                    drive.moveLeft(speed);
                }
            }
            else if (abs(gamepad1.right_stick_y)+abs(gamepad1.right_stick_y) > 0.4){
                // ANYTHING with right stick
                if (gamepad1.right_stick_x > 0) {
                    drive.turnRightTank(speed);

                }else if(gamepad1.right_stick_x < 0) {
                    drive.turnLeftTank(speed);
                }
            }
            else {
                drive.stop();
            }
        }
    }

