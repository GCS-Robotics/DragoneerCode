package org.firstinspires.ftc.teamcode;




import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Autonomous(name="RoadRunnerTest")
public class RoadRunnerAutoBeta extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));


        waitForStart();


        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(0,0,0))
                        .turnTo(-90)
                        .lineToX(12)
                        .lineToY(-24)
                        .build());
        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(12, -24, -90))
                        .lineToY(-34.5)
                        .lineToY(-39.5)
                        .lineToY(-44.5)
                        .build());
        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(12, -44.5, -90))
                        .lineToY(-24)
                        .lineToX(24)
                        .turnTo(45)
                        .build());

        // Launch Balls



        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(24, -24, 45))
                        .turnTo(-90)
                        .lineToX(-12)
                        .lineToY(-24)
                        .build());
        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(-12, -24, -90))
                        .lineToY(-34.5)
                        .lineToY(-39.5)
                        .lineToY(-44.5)
                        .build());
        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(-12, -44.5, -90))
                        .lineToY(-24)
                        .lineToX(24)
                        .turnTo(45)
                        .build());
        // Launch Balls




        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(24, -24, 45))
                        .turnTo(-90)
                        .lineToX(-36)
                        .lineToY(-24)
                        .build());
        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(-36, -24, -90))
                        .lineToY(-34.5)
                        .lineToY(-39.5)
                        .lineToY(-44.5)
                        .build());
        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(-36, -44.5, -90))
                        .lineToY(-24)
                        .lineToX(24)
                        .turnTo(45)
                        .build());
        // Launch Balls

        //Go to home
        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(24, -24, 45))
                        .lineToY(36)
                        .lineToX(-36)
                        .turnTo(0)
                        .build());

    }
}
