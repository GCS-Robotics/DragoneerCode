package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "Test Firing", group = "Test")
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
            int velIncrease = 10;
            if(gamepad1.a){
                velIncrease = 50;
            }
            if(gamepad1.b){
                velIncrease = 5;
            }
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
            telemetry.addData("Target Velocity", velocity);
            telemetry.addLine();
            telemetry.addData("Left Velocity", launcherLeft.getVelocity());
            telemetry.addData("Right Velocity", launcherRight.getVelocity());
            telemetry.update();
        }

    }
}
