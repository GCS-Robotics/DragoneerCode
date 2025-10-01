package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "test Class")
public class TestClass extends LinearOpMode {
    final float deadzone= 0.4f;  // The deadzone for the joysticks.
    private float driveSpeed = 0.1f;  // The starting drivespeed.
    int speedMode = 2;  // Mode for moving the motor at different speeds.
    DcMotor testMotor1;
    DcMotor testMotor2;

    @Override
    public void runOpMode() throws InterruptedException {
        testMotor1=hardwareMap.dcMotor.get("motor1");
        testMotor2=hardwareMap.dcMotor.get("motor2");
        testMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        testMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();

        while(opModeIsActive()) {
            if(speedMode == 2) {
                if (gamepad1.right_stick_y < -deadzone) {
                    driveSpeed += 0.01f;
                } else if(gamepad1.right_stick_y > deadzone) {
                    driveSpeed -= 0.01f;
                }
            }
            if(gamepad1.left_stick_y<-deadzone){
                testMotor1.setPower(driveSpeed);
            }
            else if(gamepad1.left_stick_y>deadzone) {
                testMotor1.setPower(-driveSpeed);
            }
            else {
                testMotor1.setPower(0);
            }
        }
    }
}
