package org.firstinspires.ftc.teamcode.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Test", group = "Test")
public class ServoTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Servo kicker = hardwareMap.get(Servo.class, "kicker");
        kicker.setPosition(0);
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.dpadLeftWasReleased()) {
                kicker.setPosition(kicker.getPosition() + 0.05);
            }
            if (gamepad1.dpadRightWasReleased()) {
                kicker.setPosition(kicker.getPosition() - 0.05);
            }
            telemetry.addLine(""+kicker.getPosition());
            telemetry.update();
        }
    }
}
