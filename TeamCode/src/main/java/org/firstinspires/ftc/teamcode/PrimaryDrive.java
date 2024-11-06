package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "Primary Drive Mode")
public class PrimaryDrive extends LinearOpMode {
    // Define Motors Here
    DcMotor leftFront; //port 0 Gobilda 5202/3/4
    DcMotor leftRear; //port 1 Gobilda 5202/3/4
    DcMotor rightFront; //port 2 Gobilda 5202/3/4
    DcMotor rightRear; //port 3 Gobilda 5202/3/4
    DcMotor linearSlide; //expansionHub port 0 Gobilda 5202/3/4
    // Allowed to go from 0 to -4500
    DcMotor spinnerPivot;
    CRServo spinner;
    Servo tilt;

    @Override
    public void runOpMode() throws InterruptedException {
        // On Init (Hardwaremap Motors Here)
        leftFront=hardwareMap.dcMotor.get("motor1");
        leftRear=hardwareMap.dcMotor.get("motor2");
        rightFront=hardwareMap.dcMotor.get("motor3");
        rightRear=hardwareMap.dcMotor.get("motor4");
        linearSlide=hardwareMap.dcMotor.get("motor5");
        spinnerPivot=hardwareMap.dcMotor.get("motor6");
        spinner=hardwareMap.crservo.get("servo2");
        tilt=hardwareMap.servo.get("servoE6");

        // Reset Encoder
        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        // Making the Drive Class
        MecanumDrive drive = new MecanumDrive(leftFront, leftRear, rightFront, rightRear, .7, false, false, true, true);
        // On Play
        while(opModeIsActive()) {
            /// LINEAR SLIDE
            if (gamepad2.left_stick_y<-.4 && linearSlide.getCurrentPosition()>-4500) {
                linearSlide.setPower(.3);
            }
            else if(gamepad2.left_stick_y>.4&&linearSlide.getCurrentPosition()<-600){
                linearSlide.setPower(-.3);
            } else {
                linearSlide.setPower(0);
            }
            telemetry.addData("Current Pos", linearSlide.getCurrentPosition());
            // SPINNER PIVOT
            if(gamepad2.right_stick_y>.4) {
                spinnerPivot.setPower(.5);
            } else if(gamepad2.right_stick_y<-.4) {
                spinnerPivot.setPower(-.5);
            } else {
                spinnerPivot.setPower(0);
            }
            // SPINNER
            if(gamepad2.right_bumper) {
                spinner.setPower(.7);
            } else if(gamepad2.left_bumper) {
                spinner.setPower(-.7);
            } else {
                spinner.setPower(0);
            }
            // TILT
            if(gamepad2.a) {
                tilt.setPosition(0);
            } if(gamepad2.b) {
                tilt.setPosition(1);
            }
            // DRIVING
            telemetry.addData("Left Stick X: ", gamepad1.left_stick_x);
            telemetry.addData("Left Stick Y: ", gamepad1.left_stick_y);
            telemetry.addData("Right Stick X: ", gamepad1.right_stick_x);
            telemetry.addData("Right Stick Y: ", gamepad1.right_stick_y);
            telemetry.update();
            if (Math.abs(gamepad1.right_stick_x) >.4) { // If the right stick is being moved sufficiently
                // Tank Turn
                if(gamepad1.right_stick_x>.4) {
                    drive.turnRightTank(1);
                }
                if(gamepad1.right_stick_x<-.4) {
                    drive.turnLeftTank(1);
                }
            } else if(Math.abs(gamepad1.left_stick_x)>.4 || Math.abs(gamepad1.left_stick_y)>.4) { // If the left stick is being moved sufficiently
                // Forward/Back
                if (gamepad1.left_stick_y < -.4 && Math.abs(gamepad1.left_stick_x) < .4) {
                    drive.moveForward(1);
                }
                if (gamepad1.left_stick_y > .4 && Math.abs(gamepad1.left_stick_x) < .4) {
                    drive.moveBackward(1);
                }
                // Left/Right
                if (gamepad1.left_stick_x < -.4 && Math.abs(gamepad1.left_stick_y) < .4) {
                    drive.moveRight(1);
                }
                if (gamepad1.left_stick_x > .4 && Math.abs(gamepad1.left_stick_y) < .4) {
                    drive.moveLeft(1);
                }
                // Diagonals
                if (gamepad1.left_stick_y < -.4 && gamepad1.left_stick_x > .4) {
                    drive.diagonalRightFront(1);
                }
                if (gamepad1.left_stick_y < -.4 && gamepad1.left_stick_x < -.4) {
                    drive.diagonalLeftFront(1);
                }
                if (gamepad1.left_stick_y > .4 && gamepad1.left_stick_x > .4) {
                    drive.diagonalRightBack(1);
                }
                if (gamepad1.left_stick_y > .4 && gamepad1.left_stick_x < -.4) {
                    drive.diagonalLeftBack(1);
                }
            } else { // If the sticks aren't being touched
                drive.stop();
            }
        }
    }
}