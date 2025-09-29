package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TeleOp with Road Runner")
public final class RoadRunnerTeleOp extends LinearOpMode {
    MecanumDrive runner;

    @Override
    public void runOpMode() throws InterruptedException {
        runner = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        waitForStart();
        while(opModeIsActive()) {
            Vector2d direction = new Vector2d(-gamepad1.left_stick_y, -gamepad1.left_stick_x);
            double angle = -90 * gamepad1.right_stick_x;
            runner.setDrivePowers(new PoseVelocity2d(direction, angle));
        }
    }
}