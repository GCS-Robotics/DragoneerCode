package org.firstinspires.ftc.teamcode.automodes;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;

@Autonomous(name = "ForwardTest")
public class RoadrunnerFoward extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize drive class with hardwareMap and starting pose
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        // Wait for start
        waitForStart();
        if (isStopRequested()) return;

        // Move forward 30 inches
        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(0, 0, 0))
                        .lineToX(30)  // move forward in X direction
                        .build()
        );
    }
}
