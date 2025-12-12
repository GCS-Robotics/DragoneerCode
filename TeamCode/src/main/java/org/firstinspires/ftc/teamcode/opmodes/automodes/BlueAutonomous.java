package org.firstinspires.ftc.teamcode.opmodes.automodes;

import androidx.annotation.NonNull;

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

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        if(isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(
                        bobot.fireMotif(new Pose2d(-55, -45, Math.toRadians(55))),
                        bobot.intake(-12),
                        bobot.fireMotif(),
                        bobot.intake(12),
                        bobot.fireMotif(),
                        bobot.intake(36),
                        bobot.fireMotif()
                )
        );
    }
}
