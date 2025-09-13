package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "test Class")
public class TestClass extends LinearOpMode {

    final float deadzone= 0.4f;

    private float driveSpeed = 0.25f;

    DcMotor testMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        testMotor=hardwareMap.dcMotor.get("motor1");
        testMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();

        while(opModeIsActive()) {
            if(gamepad1.right_stick_y<-deadzone){
                if(driveSpeed<0.99f){
                    driveSpeed += 0.001f;
                }
            }
            else if(gamepad1.right_stick_y>deadzone){
                if(driveSpeed>0.1f){
                    driveSpeed -= 0.001f;
                }
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
