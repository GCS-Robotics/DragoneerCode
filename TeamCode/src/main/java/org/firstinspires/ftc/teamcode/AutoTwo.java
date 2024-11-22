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

@Autonomous(name="2 Away || Observation Zone")
public class AutoTwo extends LinearOpMode {
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
        // Arm
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while(auto.hammerDownUp(-380, .6)&&opModeIsActive()){
            telemetryAdd("Hammer Down", spinnerPivot.getCurrentPosition(), telemetry);
        }
        // Movement
        while(auto.forwardBackward(-250, .5)&&opModeIsActive()){
            telemetryAdd("Going Forward", leftFront.getCurrentPosition(), telemetry);
        }
        // Strafe
        while(auto.strafe(1900, .5)&&opModeIsActive()){
            telemetryAdd("Strafing", leftFront.getCurrentPosition(), telemetry);
        }
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Bringing Hammer Up
        while(auto.hammerDownUp(350, .2)&&opModeIsActive()){
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
        while(auto.hammerDownUp(-300, .5)&&opModeIsActive()){
            telemetryAdd("Hammer Down", spinnerPivot.getCurrentPosition(), telemetry);
        }
        // Bringing up the Slide
        while(auto.moveSlide(4000)&&opModeIsActive()){
            tilt.setPosition(1);
            telemetryAdd("Slide Up", linearSlide.getCurrentPosition(), telemetry);
        }
        // Turning a bit
        imu.resetYaw();
        while(auto.turn(-47, .2)&&opModeIsActive()){
            tilt.setPosition(1);
            telemetryAdd("Turning", easy.getYaw(), telemetry);
        }
        // Reversing a bit
        while(auto.forwardBackward(170, .3)&&opModeIsActive()){
            tilt.setPosition(1);
            telemetryAdd("Going back", leftFront.getCurrentPosition(), telemetry);
        }
        // Strafe
        while(auto.strafe(80, .5)&&opModeIsActive()){
            telemetryAdd("Strafing", leftFront.getCurrentPosition(), telemetry);
        }
        // Outtaking the Sample
        while(auto.timedGoal(2000)&&opModeIsActive()){
            tilt.setPosition(.6);
            telemetryAdd("Outtaking the Sample", 0, telemetry);
        }
        // Strafe
        while(auto.strafe(-80, .5)&&opModeIsActive()){
            telemetryAdd("Strafing", leftFront.getCurrentPosition(), telemetry);
        }
        // Going forward a bit
        while(auto.forwardBackward(-160, .3)&&opModeIsActive()){
            tilt.setPosition(1);
            telemetryAdd("Forward", leftFront.getCurrentPosition(), telemetry);
        }
        // Return the Tilt
        while(auto.timedGoal(500)&&opModeIsActive()){
            tilt.setPosition(.5);
            telemetryAdd("Returning Tilt", tilt.getPosition(), telemetry);
        }
        // Bringing down the Slide
        while(auto.moveSlide(0)&&opModeIsActive()){
            tilt.setPosition(.5);
            telemetryAdd("Linear Slide Down", linearSlide.getCurrentPosition(), telemetry);
        }
        // Strafing Back
        while(auto.strafe(-400, .3)&&opModeIsActive()){
            telemetryAdd("Strafing", leftFront.getCurrentPosition(), telemetry);
        }
        // Turning Back
        imu.resetYaw();
        while(auto.turn(50, .5)&&opModeIsActive()){
            telemetryAdd("Turning back", easy.getYaw(), telemetry);
        }
        // Strafe
        while(auto.strafe(-4300, .5)&&opModeIsActive()){
            telemetryAdd("Strafing", leftFront.getCurrentPosition(), telemetry);
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