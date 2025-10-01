package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Flywheel Test")

public class FlywheelTest extends LinearOpMode {
    DcMotor motorLeft, motorRight;
    float driveSpeed = 0.5f;
    @Override
    public void runOpMode() throws InterruptedException {
        motorLeft = hardwareMap.dcMotor.get("motor1");
        motorRight = hardwareMap.dcMotor.get("motor2");
        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.a) {
                motorLeft.setPower(driveSpeed);
                motorRight.setPower(-driveSpeed);
            }
            else if (gamepad1.b) {
                motorLeft.setPower(-driveSpeed);
                motorRight.setPower(driveSpeed);
            }
            else {
                motorLeft.setPower(0);
                motorRight.setPower(0);
            }
        }
    }
}
