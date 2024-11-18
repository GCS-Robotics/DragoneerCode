package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous(name="Autonomous One")
public class AutoOne extends LinearOpMode {
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
    easyIMU easy;
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
        spinnerPivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
        easy = new easyIMU(imu);
        waitForStart();
        // Arm
        while(spinnerPivot.getCurrentPosition()>-420&&opModeIsActive()){
            spinnerPivot.setPower(-.6);
            telemetry.addData("Pos", spinnerPivot.getCurrentPosition());
            telemetry.update();
        }
        spinnerPivot.setPower(0);
        // Movement
        while(leftFront.getCurrentPosition()>-250&&opModeIsActive()){
            drive.moveForward(.5);
            telemetry.addData("Encoder Pos", leftFront.getCurrentPosition());
            telemetry.update();
        }
        drive.stop();
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Strafe
        while(leftFront.getCurrentPosition()<1000&&opModeIsActive()){
            drive.moveRight(.5);
            telemetry.addData("Encoder Pos", leftFront.getCurrentPosition());
            telemetry.update();
        }
        drive.stop();
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        /*
        // Turn
        while(turn(easy.getYaw(), 90)&&opModeIsActive()){
            drive.turnLeftTank(.5);
            telemetry.addData("Yaw", easy.getYaw());
            telemetry.update();
        }
        drive.stop();
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Movement
        while(move(leftFront.getCurrentPosition(), -2000)&&opModeIsActive()){
            drive.moveForward(.5);
            telemetry.addData("Encoder Pos", leftFront.getCurrentPosition());
            telemetry.update();
        }
        drive.stop();
        while(move(linearSlide.getCurrentPosition(), 4200)&&opModeIsActive()){
            linearSlide.setTargetPosition(4200);
            linearSlide.setPower(1);
        }
         */
    }
    private static boolean turn(double degrees, double goal){
        return !(goal - 1 < degrees) || !(degrees < goal + 1);
    }
    private static boolean move(double encoderValue, double goal){
        return !(goal -1 < encoderValue) || !(encoderValue < goal+1);
    }
    private class easyIMU{
        IMU imu;
        public easyIMU(IMU i){
            imu = i;
        }
        public double getYaw(){
            YawPitchRollAngles orientation;
            orientation = imu.getRobotYawPitchRollAngles();
            return orientation.getYaw(AngleUnit.DEGREES);
        }
        public double getRoll(){
            YawPitchRollAngles orientation;
            orientation = imu.getRobotYawPitchRollAngles();
            return orientation.getRoll(AngleUnit.DEGREES);
        }
        public double getPitch(){
            YawPitchRollAngles orientation;
            orientation = imu.getRobotYawPitchRollAngles();
            return orientation.getPitch(AngleUnit.DEGREES);
        }
    }
}
