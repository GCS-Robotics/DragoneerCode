package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;


@Autonomous(name="Path Following")
public class PathFollowing extends LinearOpMode {

    MecanumDrive runner;

    @Override
    public void runOpMode() throws InterruptedException {
        runner = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        waitForStart();
        while(opModeIsActive()){
            Vector2d direction = new Vector2d(10,5);
            runner.setDrivePowers(new PoseVelocity2d(direction, 0));
            wait(50);
        }
    }
}

