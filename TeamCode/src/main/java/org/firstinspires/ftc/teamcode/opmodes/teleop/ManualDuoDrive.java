package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.resources.DrumRotor;
import org.firstinspires.ftc.teamcode.resources.MainDecodeDrive;
import org.firstinspires.ftc.teamcode.resources.RegularMecanumDrive;

@TeleOp(name = "Manual Drive (2P)", group = "Main Drive")
public class ManualDuoDrive extends LinearOpMode {
    DcMotor intake;
    DcMotorEx launcherRight;
    DcMotorEx launcherLeft;
    DcMotor drumRotor;
    ColorSensor BallColor;
    public Servo kicker;
    RegularMecanumDrive drive;
    double launchSpeed;
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new RegularMecanumDrive(hardwareMap, 1.0);
        intake = hardwareMap.dcMotor.get("intake");
        launcherRight = hardwareMap.get(DcMotorEx.class, "launcherRight");
        launcherRight.setDirection(DcMotorSimple.Direction.REVERSE);
        launcherLeft = hardwareMap.get(DcMotorEx.class, "launcherLeft");
        drumRotor = hardwareMap.get(DcMotor.class, "drumRotor");
        kicker = hardwareMap.servo.get("kicker");
        BallColor = hardwareMap.colorSensor.get("colorSensor");
        kicker.setDirection(Servo.Direction.REVERSE);
        waitForStart();
        while(opModeIsActive()) {
            double currentSpeed = 1 - (gamepad1.right_trigger / 1.4);
            if (currentSpeed <= 0.1) {
                currentSpeed = .1;
            }
            drive.runDrive(gamepad1, currentSpeed, gamepad1.left_trigger > 0.2, 0.2);
            if (gamepad2.left_trigger > 0.2) {
                intake.setPower(1);
            } else {
                intake.setPower(0);
            }
            if (gamepad2.startWasPressed()) {
                launcherLeft.setPower(0.5);
                launcherRight.setPower(0.5);
            }
            if (gamepad2.backWasPressed()) {
                launcherLeft.setPower(0);
                launcherRight.setPower(0);
            }
            if (gamepad2.a) {
                drumRotor.setPower(0.5);
            } else if (gamepad2.b) {
                drumRotor.setPower(-.5);
            } else {
                drumRotor.setPower(0);
            }
            if (gamepad2.y) {
                kicker.setPosition(.35);
            } else {
                kicker.setPosition(.65);
            }
            // Crank Up Launch Speed
            if (gamepad2.dpadUpWasReleased()) {
                launchSpeed += 0.05;
            }
            // Crank Down Launch Speed
            if (gamepad2.dpadDownWasReleased() && launchSpeed > 0) {
                launchSpeed -= 0.05;
            }
            telemetry.addData("Launch Speed", launchSpeed);
            telemetry.update();
        }
    }
}
