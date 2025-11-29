package org.firstinspires.ftc.teamcode.automodes;




import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;
import org.firstinspires.ftc.teamcode.resources.RobotMechanisms;

@Autonomous(name="Three Cycle - Blue")
public class BlueAutonomous extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
        RobotMechanisms bobot = new RobotMechanisms(hardwareMap, 288);

        waitForStart();

        Actions.runBlocking(
                drive.actionBuilder(new Pose2d(0,0,0))
                        .turnTo(Math.toRadians(-90))
                        .strafeTo(new Vector2d(12, -24))
                        .strafeTo(new Vector2d(12, -34.5))
                        .strafeTo(new Vector2d(12, -39.5))
                        .strafeTo(new Vector2d(12, -44.5))
                        .strafeTo(new Vector2d(12, -24))
                        .strafeTo(new Vector2d(24, -24))
                        .turnTo(Math.toRadians(135))

                        // SECOND CYCLE
                        .turnTo(Math.toRadians(-90))
                        .strafeTo(new Vector2d(-12, -24))
                        .strafeTo(new Vector2d(-12, -34.5))
                        .strafeTo(new Vector2d(-12, -39.5))
                        .strafeTo(new Vector2d(-12, -44.5))
                        .strafeTo(new Vector2d(-12, -24))
                        .strafeTo(new Vector2d(24, -24))
                        .turnTo(Math.toRadians(135))

                        // THIRD CYCLE
                        .turnTo(Math.toRadians(-90))
                        .strafeTo(new Vector2d(-36, -24))
                        .strafeTo(new Vector2d(-36, -34.5))
                        .strafeTo(new Vector2d(-36, -39.5))
                        .strafeTo(new Vector2d(-36, -44.5))
                        .strafeTo(new Vector2d(-36, -24))
                        .strafeTo(new Vector2d(24, -24))
                        .turnTo(Math.toRadians(135))

                        // GO HOME
                        .strafeTo(new Vector2d(24, 36))
                        .strafeTo(new Vector2d(-36, 36))
                        .turnTo(Math.toRadians(0))

                        .build()
        );
    }
}
