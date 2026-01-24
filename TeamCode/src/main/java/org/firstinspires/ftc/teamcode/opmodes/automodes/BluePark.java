package org.firstinspires.ftc.teamcode.opmodes.automodes;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.resources.States;
import org.firstinspires.ftc.teamcode.resources.base_function.Drum;
import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;

@Autonomous(name = "Blue Parker")
public class BluePark extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize drive class with hardwareMap and starting pose
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        Drum.balls = new States.Artifact[]{States.Artifact.GREEN , States.Artifact.PURPLE, States.Artifact.PURPLE};
        // Wait for start
        waitForStart();
        if (isStopRequested()) return;

        // Move forward 30 inches
        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(0, 0, 0))
                        .lineToX(20)
                        .build()
        );
    }
}
