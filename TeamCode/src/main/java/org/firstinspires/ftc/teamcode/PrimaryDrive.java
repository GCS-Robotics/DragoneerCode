package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
        // Motor Inits
        leftFront=hardwareMap.dcMotor.get("motor1");
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear=hardwareMap.dcMotor.get("motor2");
        rightFront=hardwareMap.dcMotor.get("motor3");
        rightRear=hardwareMap.dcMotor.get("motor4");
        // Setting Brake Mode for Motors
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // More Motor Inits
        linearSlide=hardwareMap.dcMotor.get("motor5");
        spinnerPivot=hardwareMap.dcMotor.get("motor6");
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        spinner=hardwareMap.crservo.get("servo2");
        tilt=hardwareMap.servo.get("servoE5");
        // Reset Encoder
        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        linearSlide.setTargetPosition(linearSlide.getCurrentPosition());
        linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();
        // Making the Drive Class
        MecanumDrive drive = new MecanumDrive(leftFront, leftRear, rightFront, rightRear, .7, false, false, true, true);
        while(opModeIsActive()) {
            /// LINEAR SLIDE
            if(gamepad2.dpad_up){
                linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearSlide.setTargetPosition(4200);
                linearSlide.setPower(1);
            }
            if(gamepad2.dpad_left||gamepad2.dpad_right){
                linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearSlide.setTargetPosition(2400);
                linearSlide.setPower(1);
            }
            if(gamepad2.dpad_down){
                linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                linearSlide.setTargetPosition(0);
                if(linearSlide.getCurrentPosition()>10){
                    linearSlide.setPower(1);
                } else{
                    linearSlide.setPower(0);
                    linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
            }
            telemetry.addData("Y-Stick", gamepad2.left_stick_y);
            telemetry.addData("Current Pos", linearSlide.getCurrentPosition());
            /// SPINNER PIVOT
            if(gamepad2.right_stick_y>.4) {
                spinnerPivot.setPower(-.6);
            } else if(gamepad2.right_stick_y<-.4) {
                spinnerPivot.setPower(.6);
            } else{
                spinnerPivot.setPower(0);
            }
            /// SPINNER
            if(gamepad2.right_bumper) {
                spinner.setPower(.7);
            } else if(gamepad2.left_bumper) {
                spinner.setPower(-.7);
            } else {
                spinner.setPower(0);
            }
            /// TILT
            if(gamepad2.a) {
                tilt.setPosition(tilt.getPosition()-.01);
            } if(gamepad2.b) {
                tilt.setPosition(tilt.getPosition()+.01);
            }
            // Telemetry Stuff
            telemetry.addData("Basket Position: ", tilt.getPosition());
            /// DRIVING
            // QOL #1: Set the Speed
            double speed = 1-(gamepad1.right_trigger/1.2);
            if(speed<=0.1){
                speed=.1;
            }
            // QOL #2: Reverse Controls
            if(gamepad1.left_trigger>.3){
                speed=speed*(-1);
            }
            drive.setDriveSpeed(speed);
            telemetry.addData("Speed", speed);
            if (Math.abs(gamepad1.right_stick_x) >.4) { // If the right stick is being moved sufficiently
                if(speed<0){
                    speed=Math.abs(speed);
                    drive.setDriveSpeed(speed);
                }
                // Tank Turn
                if(gamepad1.right_stick_x>.4) {
                    drive.turnRightTank(1*gamepad1.right_stick_x);
                }
                if(gamepad1.right_stick_x<-.4) {
                    drive.turnLeftTank(1*-gamepad1.right_stick_x);
                }
            } else if(Math.abs(gamepad1.left_stick_x)>.4 || Math.abs(gamepad1.left_stick_y)>.4) { // If the left stick is being moved sufficiently
                // Forward/Back
                if (gamepad1.left_stick_y < -.4 && Math.abs(gamepad1.left_stick_x) < .4) {
                    drive.moveForward(1*-gamepad1.left_stick_y);
                }
                if (gamepad1.left_stick_y > .4 && Math.abs(gamepad1.left_stick_x) < .4) {
                    drive.moveBackward(1*gamepad1.left_stick_y);
                }
                // Left/Right
                if (gamepad1.left_stick_x < -.4 && Math.abs(gamepad1.left_stick_y) < .4) {
                    drive.moveRight(1*-gamepad1.left_stick_x);
                }
                if (gamepad1.left_stick_x > .4 && Math.abs(gamepad1.left_stick_y) < .4) {
                    drive.moveLeft(1*gamepad1.left_stick_x);
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
            telemetry.update();
        }
    }
}
