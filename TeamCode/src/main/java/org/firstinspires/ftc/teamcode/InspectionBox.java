package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "The Inspection Box")
public class InspectionBox extends LinearOpMode {
    Servo tilt;

    @Override
    public void runOpMode() throws InterruptedException {
        tilt=hardwareMap.servo.get("servoE5");
        tilt.setPosition(.5);
        waitForStart();
        while(opModeIsActive()){
            tilt.setPosition(0);
        }

    }
}
