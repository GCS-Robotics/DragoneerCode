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
        // Movement
        while(auto.forwardBackward(-150, 1)&&opModeIsActive()){
            telemetryAdd("Going Forward", leftFront.getCurrentPosition(), telemetry);
        }
        // Strafe
        while(auto.strafe(500, 1)&&opModeIsActive()){
            telemetryAdd("Strafing", leftFront.getCurrentPosition(), telemetry);
        }
        // Arm
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while(auto.hammerDownUp(-200, .4)&&opModeIsActive()){
            telemetryAdd("Hammer Down", spinnerPivot.getCurrentPosition(), telemetry);
        }
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Bringing Hammer Up
        while(auto.hammerDownUp(200, .4)&&opModeIsActive()){
            tilt.setPosition(1);
            telemetryAdd("Hammer Up", spinnerPivot.getCurrentPosition(), telemetry);
        }
        // Transferring the Sample
        while(auto.timedGoal(1500)&&opModeIsActive()){
            spinner.setPower(.5);
            telemetryAdd("Sample Transfer", 0, telemetry);
        }
        spinner.setPower(0);
        // Setting down the Spinner
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while(auto.hammerDownUp(-300, .6)&&opModeIsActive()){
            telemetryAdd("Hammer Down", spinnerPivot.getCurrentPosition(), telemetry);
        }
        // Bringing up the Slide
        while(auto.moveSlide(4000)&&opModeIsActive()){
            tilt.setPosition(1);
            telemetryAdd("Slide Up", linearSlide.getCurrentPosition(), telemetry);
        }
        // Turning a bit
        imu.resetYaw();
        while(auto.turn(-47, .4)&&opModeIsActive()){
            tilt.setPosition(1);
            telemetryAdd("Turning", easy.getYaw(), telemetry);
        }
        // Reversing a bit
        while(auto.forwardBackward(100, .6)&&opModeIsActive()){
            tilt.setPosition(1);
            telemetryAdd("Going back", leftFront.getCurrentPosition(), telemetry);
        }
        // Strafe
        while(auto.strafe(20, .6)&&opModeIsActive()){
            telemetryAdd("Strafing", leftFront.getCurrentPosition(), telemetry);
        }
        // Outtaking the Sample
        while(auto.timedGoal(2000)&&opModeIsActive()){
            tilt.setPosition(.5);
            telemetryAdd("Outtaking the Sample", 0, telemetry);
        }
        // Strafe
        while(auto.strafe(40, .6)&&opModeIsActive()){
            tilt.setPosition(1);
            telemetryAdd("Strafing", leftFront.getCurrentPosition(), telemetry);
        }
        // Going forward a bit
        while(auto.forwardBackward(-160, .6)&&opModeIsActive()){
            telemetryAdd("Forward", leftFront.getCurrentPosition(), telemetry);
        }
        // Return the Tilt
        while(auto.timedGoal(200)&&opModeIsActive()){
            tilt.setPosition(.5);
            telemetryAdd("Returning Tilt", tilt.getPosition(), telemetry);
        }
        // Bringing down the Slide
        while(auto.moveSlide(0)&&opModeIsActive()){
            tilt.setPosition(.5);
            telemetryAdd("Linear Slide Down", linearSlide.getCurrentPosition(), telemetry);
        }
        // Turning Back
        imu.resetYaw();
        while(auto.turn(40, 1)&&opModeIsActive()){
            telemetryAdd("Turning back", easy.getYaw(), telemetry);
        }
        // Finished!
        while(opModeIsActive()){
            telemetryAdd("Done!", 0, telemetry);
        }
    }
    private static void telemetryAdd(String maneuver, double encoder, Telemetry te){
        te.addData("Current Maneuver", maneuver);
        te.addData("Encoder Pos", encoder);
        te.update();
    }
}