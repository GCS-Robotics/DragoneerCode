package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Start @F4")
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
        easy = new EasyIMU(imu);
        waitForStart();
        // Arm
        while(spinnerPivot.getCurrentPosition()>-420&&opModeIsActive()){
            spinnerPivot.setPower(-.6);
            telemetry.addData("Current Maneuver", "Hammer Down");
            telemetry.addData("Pos", spinnerPivot.getCurrentPosition());
            telemetry.update();
        }
        spinnerPivot.setPower(0);
        // Movement
        while(leftFront.getCurrentPosition()>-250&&opModeIsActive()){
            drive.moveForward(.5);
            telemetry.addData("Current Maneuver", "Going Forward");
            telemetry.addData("Encoder Pos", leftFront.getCurrentPosition());
            telemetry.update();
        }
        drive.stop();
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Strafe
        while(leftFront.getCurrentPosition()<2700&&opModeIsActive()){
            drive.moveRight(.5);
            telemetry.addData("Current Maneuver", "Strafing");
            telemetry.addData("Encoder Pos", leftFront.getCurrentPosition());
            telemetry.update();
        }
        drive.stop();
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spinnerPivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Bringing Hammer Up
        while(spinnerPivot.getCurrentPosition()<400&&opModeIsActive()){
            tilt.setPosition(1);
            spinnerPivot.setPower(.1);
            telemetry.addData("Current Maneuver", "Hammer Up");
            telemetry.addData("Pos", spinnerPivot.getCurrentPosition());
            telemetry.update();
        }
        spinnerPivot.setPower(0);
        // Outtaking the Sample
        double beforeMove = System.currentTimeMillis();
        double finishTime = 1500;
        while(System.currentTimeMillis()-beforeMove<finishTime&&opModeIsActive()){
            spinner.setPower(.7);
            telemetry.addData("Current Maneuver", "Transferring Sample");
            telemetry.update();
        }
        spinner.setPower(0);
        // Setting down the Spinner
        while(spinnerPivot.getCurrentPosition()>-50&&opModeIsActive()){
            spinnerPivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); // Temporarily changing the zero behavior
            spinnerPivot.setPower(-.6);
            telemetry.addData("Current Maneuver", "Put Down Spinner");
            telemetry.addData("Pos", spinnerPivot.getCurrentPosition());
            telemetry.update();
        }
        spinnerPivot.setPower(0);
        // Bringing up the Slide
        while(move(linearSlide.getCurrentPosition(), 4000)&&opModeIsActive()){
            spinnerPivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // Changing it back to normal
            linearSlide.setTargetPosition(4000);
            linearSlide.setPower(1);
            telemetry.addData("Current Maneuver", "Linear Slide Up");
            telemetry.update();
        }
        // Turning a bit
        imu.resetYaw();
        while(turn(easy.getYaw(), -50)&&opModeIsActive()){
            drive.turnRightTank(.2);
            telemetry.addData("Current Maneuver", "Turn -50");
            telemetry.addData("Yaw", easy.getYaw());
            telemetry.update();
        }
        drive.stop();
        // Strafing a bit
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while(leftFront.getCurrentPosition()<400&&opModeIsActive()){
            drive.moveRight(.3);
            telemetry.addData("Current Maneuver", "Strafing a Smidge");
            telemetry.addData("Encoder Pos", leftFront.getCurrentPosition());
            telemetry.update();
        }
        drive.stop();
        // Bringing down the Slide
        while(move(linearSlide.getCurrentPosition(), 0)&&opModeIsActive()){
            linearSlide.setTargetPosition(0);
            linearSlide.setPower(1);
            tilt.setPosition(.5);
            telemetry.addData("Current Maneuver", "Linear Slide Down");
            telemetry.update();
        }
        // Strafing Back
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while(leftFront.getCurrentPosition()>-400&&opModeIsActive()){
            drive.moveLeft(.3);
            telemetry.addData("Current Maneuver", "Strafing a Smidge");
            telemetry.addData("Encoder Pos", leftFront.getCurrentPosition());
            telemetry.update();
        }
        drive.stop();
        // Turning Back
        imu.resetYaw();
        while(turn(easy.getYaw(), 50)&&opModeIsActive()){
            drive.turnLeftTank(.5);
            telemetry.addData("Current Maneuver", "Turn -50");
            telemetry.addData("Yaw", easy.getYaw());
            telemetry.update();
        }
        drive.stop();
        // Strafe
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while(leftFront.getCurrentPosition()>-4500&&opModeIsActive()){
            drive.moveLeft(.5);
            telemetry.addData("Current Maneuver", "Strafing");
            telemetry.addData("Encoder Pos", leftFront.getCurrentPosition());
            telemetry.update();
        }
        drive.stop();
        // Movement
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while(leftFront.getCurrentPosition()<250&&opModeIsActive()){
            drive.moveBackward(.5);
            telemetry.addData("Current Maneuver", "Going Backward");
            telemetry.addData("Encoder Pos", leftFront.getCurrentPosition());
            telemetry.update();
        }
        // Finished!
        while(opModeIsActive()){
            telemetry.addData("Current Maneuver", "Done!");
            telemetry.update();
        }
        /*
        // Out-taking the sample
        beforeMove = System.currentTimeMillis();
        finishTime = 1000;
        while(System.currentTimeMillis()-beforeMove<finishTime&&opModeIsActive()){
            tilt.setPosition(.7);
        }
        tilt.setPosition(1);
         */
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
}
