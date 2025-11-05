package org.firstinspires.ftc.teamcode;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "SteveChallenge")
public class SteveChallenge extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));



        waitForStart();
        if (isStopRequested()) return;


        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(0, 0, 0))
                        .turn(PI / 2)    // .lineToX(10)
                        // .lineToX(0)
                        //  .turn(PI / 2)
                        .build()
        );
    }
}

