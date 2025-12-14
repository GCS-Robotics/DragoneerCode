package org.firstinspires.ftc.teamcode.opmodes.automodes.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.resources.autonomous.AutonomousMovements;
import org.firstinspires.ftc.teamcode.resources.autonomous.RobotActions;
import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;

@Autonomous(name = "One Boi Test")
 public class OneArtifactTest extends LinearOpMode {
    AutonomousMovements bobot;
    Pose2d startPose = new Pose2d(-55, -45, Math.toRadians(55));

    @Override
    public void runOpMode() throws InterruptedException {
        bobot = new AutonomousMovements(new MecanumDrive(hardwareMap, startPose), new RobotActions(hardwareMap, telemetry, FtcDashboard.getInstance().getTelemetry()), false);
        waitForStart();
        if(isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(
                        bobot.bobot.fireArtifact(1),
                        bobot.bobot.fireArtifact(1),
                        bobot.bobot.fireArtifact(0)
                )
        );
    }
}
