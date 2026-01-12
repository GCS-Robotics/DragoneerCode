package org.firstinspires.ftc.teamcode.resources.autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
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

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.resources.States;
import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;

public class AutonomousMovements {
    private MecanumDrive drive;
    public RobotActions bobot;
    private boolean mod;
    private int motifTag = -1;

    /**
     * Creates the object that handles larger-scale actions in our autonomous modes.
     * @param md The object necessary for movement.
     * @param rm The object necessary for mechanisms.
     * @param m Set to false if on blue-side, set to true if on red-side.
     */
    public AutonomousMovements(MecanumDrive md, RobotActions rm, boolean m){
        drive = md;
        bobot = rm;
        mod = m;
    }

    /**
     * Sets the motif tag according to what was detected
     * @param motifTag The AprilTag ID of the motif.
     */

    public void setMotifTag(int motifTag) {
        this.motifTag = motifTag;
    }
    public Action driveToMotif(Pose2d startPose){
        return new SequentialAction(
                drive.actionBuilder(startPose)
                        .strafeTo(new Vector2d(0, 0))
                        .turnTo(0)
                        .build(),
                scanTag());
    }
    public Action fireMotif(Pose2d startPose) {
        double angle = Math.toRadians(50);
        if(mod){
            angle = Math.toRadians(360-50);
        }
        Action getReady;
        if(startPose.equals(new Pose2d(0, 0, 0))){
            getReady = new RaceAction(
                    drive.actionBuilder(startPose)
                            .turnTo(angle)
                            .build(),
                    bobot.primeLaunch());
        }
        else{
            getReady = new RaceAction(
                    drive.actionBuilder(startPose)
                            .strafeTo(new Vector2d(0, 0))
                            .turnTo(angle)
                            .build(),
                    bobot.primeLaunch());
        }
        Action gpp = new SequentialAction(
                bobot.fireArtifact(States.Artifact.GREEN),
                bobot.fireArtifact(States.Artifact.PURPLE),
                bobot.fireArtifact(States.Artifact.PURPLE),
                bobot.cancelLaunch());
        Action pgp = new SequentialAction(
                bobot.fireArtifact(States.Artifact.PURPLE),
                bobot.fireArtifact(States.Artifact.GREEN),
                bobot.fireArtifact(States.Artifact.PURPLE),
                bobot.cancelLaunch());
        Action ppg = new SequentialAction(
                bobot.fireArtifact(States.Artifact.PURPLE),
                bobot.fireArtifact(States.Artifact.PURPLE),
                bobot.fireArtifact(States.Artifact.GREEN),
                bobot.cancelLaunch());
        if (motifTag == 21) {
            return new SequentialAction(getReady, gpp);
        }
        if (motifTag == 22) {
            return new SequentialAction(getReady, pgp);
        }
        return new SequentialAction(getReady, ppg);
    }
    public Action fireMotif() {
        double angle = Math.toRadians(270);
        if(mod){
            angle = Math.toRadians(360 - 270);
        }
        return fireMotif(new Pose2d(new Vector2d(0, 0), angle));
    }

    public Action intake(int xCoordinate, Pose2d startPose){
        int modifier = 1;
        double angle = Math.toRadians(270);
        if(mod){
            modifier = -1;
            angle = Math.toRadians(90);
        }
        VelConstraint vel = (pose2dDual, posePath, v) -> 8;
        return new SequentialAction(
                drive.actionBuilder(startPose)
                        .strafeTo(new Vector2d(xCoordinate, -12*modifier))
                        .turnTo(angle)
                        .build(),
                new RaceAction(
                        drive.actionBuilder(new Pose2d(xCoordinate, -12*modifier, angle))
                                .strafeTo(new Vector2d(xCoordinate, -45*modifier), vel)
                                .strafeTo(new Vector2d(xCoordinate, -12*modifier), vel)
                                .strafeTo(new Vector2d(0, 0))
                                .build(),
                        bobot.intake()
                ),
                bobot.stopIntake()
        );
    }

    public Action intake(int xCoordinate){
        double angle = Math.toRadians(50);
        if(mod){
            angle = Math.toRadians(360-50);
        }
        return intake(xCoordinate, new Pose2d(new Vector2d(0, 0), angle));
    }
    public class ScanTag implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(motifTag == -1){
                setMotifTag(bobot.bot.getMotifTag());
            }
            telemetryPacket.addLine("Tag Seen: "+motifTag);
            return motifTag == -1;
        }
    }
    public Action scanTag(){return new ScanTag();}
}
