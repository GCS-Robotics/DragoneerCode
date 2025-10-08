package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "ContineServos")



public class ContineServos extends LinearOpMode {

    CRServo servo1;

    CRServo servo2;


    @Override
    public void runOpMode() throws InterruptedException {
        servo1 = hardwareMap.crservo.get("Servo 1");
        servo2 = hardwareMap.crservo.get("Servo 2");

        if (gamepad1.dpadUpWasPressed()) {
            servo1.setPower(10);

        } else servo1.setPower(0);

        if (gamepad1.dpadDownWasPressed()) {
            servo2.setPower(10);

        } else servo2.setPower(0);

    }


