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
                drive.actionBuilder(new Pose2d(60, 60, 180))
                        .turnTo(-90)
                        .lineToX(12)
                        .lineToY(-24)
                        .build());
        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(12, -24, -90))
                        .Run()
                        .lineToY(-34)
                        .build());
    }
}
