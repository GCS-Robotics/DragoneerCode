package org.firstinspires.ftc.teamcode.opmodes.automodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "Test Firing")
public class FiringMode extends LinearOpMode {
    DcMotorEx launcherRight;
    DcMotorEx launcherLeft;
    double velocity = 500;

    @Override
    public void runOpMode() throws InterruptedException {
        launcherRight = hardwareMap.get(DcMotorEx.class, "launcherRight");
        launcherRight.setDirection(DcMotorSimple.Direction.REVERSE);
        launcherLeft = hardwareMap.get(DcMotorEx.class, "launcherLeft");

        waitForStart();

        while(opModeIsActive()){
            if (gamepad1.dpadUpWasReleased()) {
                velocity+=10;
            }
            if(gamepad1.dpadDownWasReleased()){
                velocity -= 10;
                if (velocity <= 0){
                    velocity = 0;
                }
            }
            launcherRight.setVelocity(velocity);
            launcherLeft.setVelocity(velocity);
            telemetry.addData("Velocity", velocity);
            telemetry.update();
        }

    }
}
