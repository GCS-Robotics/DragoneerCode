package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "CR Servo Test")
public class CRServoTest extends LinearOpMode {
    CRServo servo1;
    CRServo servo2;

    @Override
    public void runOpMode() throws InterruptedException {
        servo1 = hardwareMap.crservo.get("Servo 1");
        servo2 = hardwareMap.crservo.get("Servo 2");
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                servo1.setPower(1);
            } else servo1.setPower(0);

            if (gamepad1.b) {
                servo2.setPower(-1);
            } else servo2.setPower(0);
        }
    }
}
