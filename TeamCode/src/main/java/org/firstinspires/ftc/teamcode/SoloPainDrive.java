package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "PAIN (Solo)")
public class SoloPainDrive extends LinearOpMode {
    double SPEED = 1.0;
    double launchSpeed = 1.0;
    double drumSpeed = 0.2;
    double DEADZONE = 0.2;
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;
    DcMotor intake;
    DcMotor launcherRight;
    DcMotor launcherLeft;
    CRServo drumServo;
    Servo kicker;
    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        intake = hardwareMap.dcMotor.get("intake");
        launcherRight = hardwareMap.dcMotor.get("launcherRight");
        launcherLeft = hardwareMap.dcMotor.get("launcherLeft");
        drumServo = hardwareMap.crservo.get("drumServo");
        kicker = hardwareMap.servo.get("kicker");
        kicker.setDirection(Servo.Direction.REVERSE);
        MecanumDrive drive = new MecanumDrive(leftFront, rightFront, leftRear, rightRear, 1, true, false, true, false);
        DcMotor[] motors = {leftFront, rightFront, leftRear, rightRear, launcherLeft, launcherRight};
        for(DcMotor motor : motors){
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        waitForStart();
        while(opModeIsActive()){
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
            else if (abs(gamepad1.right_stick_x) + abs(gamepad1.right_stick_x) > DEADZONE) {
                if (gamepad1.right_stick_x > 0) {
                    drive.turnRightTank(SPEED);
                } else if (gamepad1.right_stick_x < 0) {
                    drive.turnLeftTank(SPEED);
                }
            } else {
                drive.stop();
            }
            /// WOULD NORMALLY BE Gamepad 2
            // Intake
            if (gamepad1.left_bumper) {
                intake.setPower(-0.5);
            } else {
                intake.setPower(0);
            }
            // Outtake
            if (gamepad1.right_bumper) {
                launcherRight.setPower(launchSpeed);
                launcherLeft.setPower(launchSpeed);
            } else {
                launcherRight.setPower(0);
                launcherLeft.setPower(0);
            }
            if (gamepad1.dpadDownWasReleased()){
                launchSpeed -= 0.05;
                if(launchSpeed<0) launchSpeed=0;
            }
            if (gamepad1.dpadUpWasReleased()) {
                launchSpeed += 0.05;
                if(launchSpeed>1) launchSpeed=1;
            }
            telemetry.addData("Launch Speed", launchSpeed);
            // Drum Controls
            if (gamepad1.a){
                drumServo.setPower(drumSpeed);
            }
            else if(gamepad1.b){
                drumServo.setPower(-drumSpeed);
            } else {
                drumServo.setPower(0);
            }
            if (gamepad1.xWasReleased()){
                drumSpeed -= 0.05;
                if(drumSpeed<0) drumSpeed=0;
            }
            if (gamepad1.yWasReleased()) {
                drumSpeed += 0.05;
                if(drumSpeed>1) drumSpeed=1;
            }
            telemetry.addData("Drum Speed", drumSpeed);
            //
            // Kicker Controls
            if (gamepad1.dpad_left){
                kicker.setPosition(.5);
            }
            if(gamepad1.dpad_right){
                kicker.setPosition(.7);
            }
            telemetry.update();
        }
    }
}
