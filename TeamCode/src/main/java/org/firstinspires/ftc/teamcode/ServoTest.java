package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "SERVO TEST")
public class ServoTest extends LinearOpMode {
    Servo kicker;
    @Override
    public void runOpMode() throws InterruptedException {
        kicker = hardwareMap.servo.get("kicker");
        waitForStart();
        kicker.setPosition(0);
        while(opModeIsActive()){
            // Kicker Controls
            if (gamepad2.dpadDownWasReleased()){
                kicker.setPosition(kicker.getPosition()-0.05);
            }
            if(gamepad2.dpadUpWasReleased()){
                kicker.setPosition(kicker.getPosition()+0.05);
            }
            if(gamepad2.dpadLeftWasReleased()){
                kicker.setDirection(Servo.Direction.FORWARD);
            }
            if(gamepad2.dpadRightWasReleased()){
                kicker.setDirection(Servo.Direction.REVERSE);
            }
            telemetry.addData("Servo Position", kicker.getPosition());
            telemetry.addData("Servo Direction", kicker.getDirection());
            telemetry.update();
        }
    }
}
