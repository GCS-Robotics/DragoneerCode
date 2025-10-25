package org.firstinspires.ftc.teamcode;


import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "SteveChallenge")
public class SteveChallenge extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        //MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        waitForStart();
        int count = 0;
        while(opModeIsActive()) {
            telemetry.addData("COUNTING:","%d",count);
            count++;
            sleep(1000);
        }
//        if (isStopRequested()) return;
//        Actions.runBlocking(
//                drive.actionBuilder(new Pose2d(0, 0, 0))
//                        .lineToX(64)
//                        .build());
    }
}
