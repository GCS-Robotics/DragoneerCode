package org.firstinspires.ftc.teamcode.opmodes.automodes;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PosePath;
import com.acmerobotics.roadrunner.RaceAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.resources.autonomous.AutonomousMovements;
import org.firstinspires.ftc.teamcode.resources.autonomous.RobotActions;
import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;

@Autonomous(name = "Blue Autonomous")
 public class BlueAutonomous extends LinearOpMode {
    AutonomousMovements bobot;
    Pose2d startPose = new Pose2d(-44, -50, Math.toRadians(55));

    @Override
    public void runOpMode() throws InterruptedException {
        bobot = new AutonomousMovements(new MecanumDrive(hardwareMap, startPose), new RobotActions(hardwareMap, telemetry, FtcDashboard.getInstance().getTelemetry()), false);
        waitForStart();
        if(isStopRequested()) return;
        Actions.runBlocking(
                bobot.driveToMotif(startPose)
        );
        if(isStopRequested()) return;
        Actions.runBlocking(
                bobot.fireMotif()
        );
        if(isStopRequested()) return;
        Actions.runBlocking(
                bobot.intake(-3)
        );
        if(isStopRequested()) return;
        Actions.runBlocking(
                bobot.fireMotif()
        );
        if(isStopRequested()) return;
        Actions.runBlocking(
                bobot.intake(20)
        );
        if(isStopRequested()) return;
        Actions.runBlocking(
                bobot.fireMotif()
        );
    }
}
