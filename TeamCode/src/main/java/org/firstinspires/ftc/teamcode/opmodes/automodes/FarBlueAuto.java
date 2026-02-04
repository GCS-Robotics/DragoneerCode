package org.firstinspires.ftc.teamcode.opmodes.automodes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.resources.autonomous.AutonomousMovements;
import org.firstinspires.ftc.teamcode.resources.autonomous.RobotActions;
import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;

@Autonomous(name = "Far Blue", group = "Far")
public class FarBlueAuto extends LinearOpMode {
    AutonomousMovements bobot;
    Pose2d startPose = new Pose2d(60, 12, Math.toRadians(0));
    @Override
    public void runOpMode() throws InterruptedException {
        bobot = new AutonomousMovements(new MecanumDrive(hardwareMap, startPose), new RobotActions(hardwareMap, telemetry, FtcDashboard.getInstance().getTelemetry()), false);
        waitForStart();
        Actions.runBlocking(safe(bobot.drive.actionBuilder(startPose)
                .strafeTo(new Vector2d(-12, 12))
                .build(), this));
        if(isStopRequested()){return;}

        Actions.runBlocking(safe(bobot.scanTag(), this));
        if(isStopRequested()){return;}

        bobot.drive.updatePoseEstimate();
        Actions.runBlocking(safe(bobot.drive.actionBuilder(bobot.drive.localizer.getPose())
                .strafeTo(new Vector2d(56, 12))
                .turn(Math.toRadians(18))
                .build(), this));
        if(isStopRequested()){return;}
        Actions.runBlocking(safe(
                bobot.motifOrder()
                , this));
        if(isStopRequested()){return;}

        bobot.drive.updatePoseEstimate();
        Actions.runBlocking(safe(bobot.drive.actionBuilder(bobot.drive.localizer.getPose())
                .strafeTo(new Vector2d(36, -12))
                .build(), this));
        if(isStopRequested()){return;}

        while(opModeIsActive()){
            bobot.bobot.bot.run();
            bobot.bobot.bot.setOuttake(false, true, false, false);
        }
    }
    public static Action safe(Action action, LinearOpMode opMode) {
        return packet -> {
            if (!opMode.opModeIsActive()) return false;
            return action.run(packet);
        };
    }
}
