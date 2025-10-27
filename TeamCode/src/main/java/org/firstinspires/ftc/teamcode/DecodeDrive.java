package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "Drive Code")
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
    // Drum
    CRServo drumServo;
    Servo kicker;
    /*
    DistanceSensor distance;
    ColorSensor color;
    boolean running = false;
    int rotations = 0;
    int targetRotations = 0;
    int targetBall = -1;
    double[] PURPLE = {255, 0, 255};
    double[] GREEN = {0, 255, 0};

     */
    // Extra Tools
    DcMotor[] allMotors = {leftFront, rightFront, leftRear, rightRear};
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
        drumServo = hardwareMap.crservo.get("drumServo");
        intake = hardwareMap.dcMotor.get("intake");
        kicker = hardwareMap.servo.get("kicker");
        //distance = hardwareMap.get(DistanceSensor.class, "distance");
        //color = hardwareMap.get(ColorSensor.class, "color");
        // Setting Motor Stuff
        /*
        for (DcMotor motor : allMotors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
        }

         */
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
            if (gamepad2.right_trigger > 0.1) {
                launcherRight.setPower(1);
                launcherLeft.setPower(1);
            } else {
                launcherRight.setPower(0);
                launcherLeft.setPower(0);
            }
            // Intake
            if (gamepad2.left_trigger > 0.1) {
                intake.setPower(0.5);
            } else {
                intake.setPower(0);
            }
            // Kicker Controls
            if (gamepad2.dpad_left){
                kicker.setPosition(0);
            }
            if(gamepad2.dpad_right){
                kicker.setPosition(0.5);
            }
            // Drum Controls
            if (gamepad2.a){
                drumServo.setPower(0.2);
            }
            else if(gamepad2.b){
                drumServo.setPower(-0.2);
            } else {
                drumServo.setPower(0);
            }
            /*
            if (gamepad2.aWasPressed()){ // Do 1/3 rotation
                targetRotations = 1;
                targetBall = -1;
            }
            if (gamepad2.xWasReleased()){ // Send up a purple
                targetRotations = 4;
                targetBall = 0;
            }
            if (gamepad2.bWasReleased()) { // Send up a green
                targetRotations = 4;
                targetBall = 1;
            }
            // Drum Backend
            if(rotations < targetRotations){
                drumServo.setPower(0.2);
            } else {
                drumServo.setPower(0);
                targetRotations = 0;
                rotations = 0;
                // TODO: Add something to tell the driver what ball is primed, or tell them if no ball is primed.
                // TODO: That message should stay until they click another ball request button or launch the current ball.
            }
            // TODO: Call if we sense the distance to be very low AND this isn't a repeat detection (check for high distance beforehand?)
            if(false){
                rotations += 1;
                distance.getDistance(DistanceUnit.INCH);
            }
            // TODO: Call if we sense something purple AND we're looking for something purple
            // TODO: Call this also if we sense something green AND we're looking for something green
            if(false){
                targetRotations = -1;
            }
             */
        }
    }
}
