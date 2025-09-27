package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Potato Man 5002")
public final class RoadRunnerTeleOp extends LinearOpMode {
    MecanumDrive runner = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

    @Override
    public void runOpMode() throws InterruptedException {
        Vector2d direction = new Vector2d(gamepad1.left_stick_x, gamepad1.left_stick_y);
        runner.setDrivePowers(new PoseVelocity2d(direction, 0));
    }
}