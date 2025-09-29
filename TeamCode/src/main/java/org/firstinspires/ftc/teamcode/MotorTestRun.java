package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp(name="Motor Test")
public class MotorTestRun extends LinearOpMode {

        private float driveSpeed = 0.25f;  // The starting drivespeed.
        DcMotor testMotor1;
        DcMotor testMotor2;
        DcMotor testMotor3;
        DcMotor testMotor4;

        @Override
        public void runOpMode() throws InterruptedException {
            testMotor1 = hardwareMap.dcMotor.get("motor1");
            testMotor2 = hardwareMap.dcMotor.get("motor2");
            testMotor3 = hardwareMap.dcMotor.get("motor3");
            testMotor4 = hardwareMap.dcMotor.get("motor4");
            testMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            testMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            testMotor3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            testMotor4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            waitForStart();
            while (opModeIsActive()) {
                testMotor1.setPower(1);
                testMotor2.setPower(1);
                testMotor3.setPower(1);
                testMotor4.setPower(1);
            }


        }
}