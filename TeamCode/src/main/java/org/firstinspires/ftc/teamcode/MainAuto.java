package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(name="Three Sample Auto")
public class MainAuto extends LinearOpMode {
    // Normal Drive Stuffs
    DcMotor leftFront;
    DcMotor leftRear;
    DcMotor rightFront;
    DcMotor rightRear;
    DcMotor linearSlide;
    DcMotor spinnerPivot;
    CRServo spinner;
    Servo tilt;
    // IMU Stuffs
    IMU imu;
    IMU.Parameters params;
    MecanumDrive drive;
    EasyIMU easy;
    @Override
    public void runOpMode() throws InterruptedException {
        // Other Stuff
        leftFront=hardwareMap.dcMotor.get("motor1");
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear=hardwareMap.dcMotor.get("motor2");
        rightFront=hardwareMap.dcMotor.get("motor3");
        rightRear=hardwareMap.dcMotor.get("motor4");
        linearSlide=hardwareMap.dcMotor.get("motor5");
        spinnerPivot=hardwareMap.dcMotor.get("motor6");
        spinner=hardwareMap.crservo.get("servo2");
        tilt=hardwareMap.servo.get("servoE5");
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Setting Brake Mode for Motors
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Reset Encoder
        linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        linearSlide.setTargetPosition(linearSlide.getCurrentPosition());
        linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive = new MecanumDrive(leftFront, leftRear, rightFront, rightRear, .7, false, false, true, true);

        // IMU Stuff
        imu = hardwareMap.get(IMU.class, "imu");
        params = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                )
        );
        imu.initialize(params);
        imu.resetYaw();
        easy = new EasyIMU(imu);
        BaseAuto auto = new BaseAuto(drive, linearSlide, spinnerPivot, spinner, tilt, easy, leftFront);
        waitForStart();
        while(opModeIsActive()) {
            tilt.setPosition(.63);
            // Movement
            while (auto.forwardBackward(-150, 1) && opModeIsActive()) {
                telemetryAdd("Going Forward", leftFront.getCurrentPosition(), telemetry);
            }
            // Strafe
            while (auto.strafe(550, 1) && opModeIsActive()) {
                telemetryAdd("Strafing", leftFront.getCurrentPosition(), telemetry);
            }
            // Arm
            spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            while (auto.hammerDownUp(-300, .4) && opModeIsActive()) {
                telemetryAdd("Hammer Down", spinnerPivot.getCurrentPosition(), telemetry);
            }
            // Sample Into the Bin
            sampleIntoBin(telemetry, auto, 1500);
            imu.resetYaw();
            // Bringing up the Slide AND Turning
            while (auto.moveSlide(4000)||auto.turn(-47, .4) && opModeIsActive()) {
                tilt.setPosition(1);
                auto.turn(-47, .4);
                telemetryAdd("Slide Up AND Turning", easy.getYaw(), telemetry);
            }
            // Reversing a bit
            while (auto.forwardBackward(90, .6) && opModeIsActive()) {
                tilt.setPosition(1);
                telemetryAdd("Going back", leftFront.getCurrentPosition(), telemetry);
            }
            // Outtaking the Sample
            while (auto.timedGoal(1000)&&opModeIsActive()) {
                tilt.setPosition(.60);
                telemetryAdd("Outtaking the Sample", 0, telemetry);
            }
            while (auto.timedGoal(100) && opModeIsActive()) {
                tilt.setPosition(1);
            }
            // Going forward a bit
            while (auto.forwardBackward(-160, .6) && opModeIsActive()) {
                telemetryAdd("Forward", leftFront.getCurrentPosition(), telemetry);
            }
            // Return the Tilt
            while (auto.timedGoal(200) && opModeIsActive()) {
                tilt.setPosition(.5);
                telemetryAdd("Returning Tilt", tilt.getPosition(), telemetry);
            }
            // Bringing down the Slide
            while (auto.moveSlide(0) && opModeIsActive()) {
                tilt.setPosition(.63);
                telemetryAdd("Linear Slide Down", linearSlide.getCurrentPosition(), telemetry);
            }
            // Turning Back
            imu.resetYaw();
            while (auto.turn(35, 1) && opModeIsActive()) {
                telemetryAdd("Turning back", easy.getYaw(), telemetry);
            }
            /// SAMPLE TWO
            // Going Forward
            while(auto.forwardBackward(-20, .4)&&opModeIsActive()){
                spinner.setPower(-.5);
                telemetryAdd("Going forward", leftFront.getCurrentPosition(), telemetry);
            }
            // Eat AND Turn
            imu.resetYaw();
            while (auto.turn(25, .15)&&opModeIsActive()){
                telemetryAdd("Eat and Turn", easy.getYaw(), telemetry);
            }
            // Turning back
            imu.resetYaw();
            while (auto.turn(-25, .4)&&opModeIsActive()){
                telemetryAdd("Turning Back", easy.getYaw(), telemetry);
            }
            spinner.setPower(0);
            // Sample Into the Bin
            sampleIntoBin(telemetry, auto, 600);
            // Finished!
            while (opModeIsActive()) {
                telemetryAdd("Done!", 0, telemetry);
            }
        }
    }
    private static void telemetryAdd(String maneuver, double encoder, Telemetry te){
        te.addData("Current Maneuver", maneuver);
        te.addData("Encoder Pos", encoder);
        te.update();
    }
    private void sampleIntoBin(Telemetry telemetry, BaseAuto auto, int goalTime){
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while (auto.hammerDownUp(300, .2) && opModeIsActive()) {
            tilt.setPosition(1);
            telemetryAdd("Hammer Up", spinnerPivot.getCurrentPosition(), telemetry);
        }
        // Transferring the Sample
        while (auto.timedGoal(goalTime) && opModeIsActive()) {
            spinner.setPower(.5);
            telemetryAdd("Sample Transfer", 0, telemetry);
        }
        spinner.setPower(0);
        // Arm Down
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while (auto.hammerDownUp(-300, .6) && opModeIsActive()) {
            telemetryAdd("Hammer Down", spinnerPivot.getCurrentPosition(), telemetry);
        }
    }
}