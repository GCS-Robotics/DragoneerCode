package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "test Class")
public class TestClass extends LinearOpMode {
    final float deadzone= 0.4f;  // The deadzone for the joysticks.
    private float driveSpeed = 0.25f;  // The starting drivespeed.
    int speedMode = 1;  // Mode for moving the motor at different speeds.
    DcMotor testMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        testMotor=hardwareMap.dcMotor.get("motor1");
        testMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();

        while(opModeIsActive()) {
            if(speedMode == 2) {
                if(gamepad1.right_stick_y<-deadzone || gamepad1.right_stick_y>deadzone){
                        driveSpeed = gamepad1.right_stick_y;
                    }
                }
            else if(speedMode == 1) {
                // Add the code later.
            }

            if(gamepad1.left_stick_y<-deadzone){
                testMotor.setPower(driveSpeed);
            }
            else if(gamepad1.left_stick_y>deadzone) {
                testMotor.setPower(-driveSpeed);
            }
            else {
                testMotor.setPower(0);
            }
        }
    }
}
