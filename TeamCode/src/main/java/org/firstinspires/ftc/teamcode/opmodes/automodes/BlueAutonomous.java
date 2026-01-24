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

@Autonomous(name = "Blue Autonomous", preselectTeleOp="Main Drive (2P)")
 public class BlueAutonomous extends LinearOpMode {
    AutonomousMovements bobot;
    Pose2d startPose = new Pose2d(-44, -50, Math.toRadians(55));

    @Override
    public void runOpMode() throws InterruptedException {
        bobot = new AutonomousMovements(new MecanumDrive(hardwareMap, startPose), new RobotActions(hardwareMap, telemetry, FtcDashboard.getInstance().getTelemetry()), false);
        waitForStart();
        if(isStopRequested()) return;
        Actions.runBlocking(
                safe(bobot.driveToMotif(startPose), this)
        );
        if(isStopRequested()) return;
        Actions.runBlocking(
                safe(bobot.fireMotif(), this)
        );
        if(isStopRequested()) return;
        Actions.runBlocking(
                safe(bobot.intake(0), this)
        );
        if(isStopRequested()) return;
        Actions.runBlocking(
                safe(bobot.fireMotif(), this)
        );
        Actions.runBlocking(
                safe(bobot.drive.actionBuilder(new Pose2d(-12, 12, Math.toRadians(52)))
                        .strafeTo(new Vector2d(-12, -36))
                        .build(), this)
        );

    }
    public static Action safe(Action action, LinearOpMode opMode) {
        return packet -> {
            if (!opMode.opModeIsActive()) return false;
            return action.run(packet);
        };
    }


}
