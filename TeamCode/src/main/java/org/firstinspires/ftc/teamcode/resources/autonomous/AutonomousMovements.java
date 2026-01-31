package org.firstinspires.ftc.teamcode.resources.autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.RaceAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;

import org.firstinspires.ftc.teamcode.resources.States;
import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;

public class AutonomousMovements {
    public MecanumDrive drive;
    public RobotActions bobot;
    private final boolean redSide;
    public int motifTag = -1;

    /**
     * Creates the object that handles larger-scale actions in our autonomous modes.
     * @param md The object necessary for movement.
     * @param rm The object necessary for mechanisms.
     * @param rS Set to false if on blue-side, set to true if on red-side.
     */
    public AutonomousMovements(MecanumDrive md, RobotActions rm, boolean rS){
        drive = md;
        bobot = rm;
        redSide = rS;
    }
    public Action driveToMotif(Pose2d startPose){
        double yMod = -1;
        if(redSide){
            yMod = 1;
        }
        double fireAngle = Math.toRadians(50);
        if(redSide){
            fireAngle = Math.toRadians(360)-fireAngle;
        }
        return new SequentialAction(
                drive.actionBuilder(startPose)
                        .strafeTo(new Vector2d(-12, 12*yMod))
                        .build(),
                new RaceAction(
                        drive.actionBuilder(new Pose2d(-12, 12*yMod, fireAngle))
                                .turnTo(0)
                                .build(),
                        scanTag()
                ));
    }
    public Action fireMotif() {
        double yMod = -1;
        if(redSide){
            yMod = 1;
        }
        drive.updatePoseEstimate();
        double angle = Math.toRadians(52);
        if(redSide){
            angle = Math.toRadians(360)-angle;
        }
        Action getReady;
        if(drive.localizer.getPose().position.equals(new Vector2d(-12, 12*yMod))){
            getReady = new SequentialAction(
                    drive.actionBuilder(drive.localizer.getPose())
                            .turnTo(angle)
                            .build(),
                    bobot.stopIntake(),
                    bobot.primeLaunch());
        }
        else{
            getReady = new SequentialAction(
                    drive.actionBuilder(drive.localizer.getPose())
                            .strafeTo(new Vector2d(-12, 12*yMod))
                            .turnTo(angle)
                            .build(),
                    bobot.stopIntake(),
                    bobot.primeLaunch());
        }
        return new SequentialAction(getReady, motifOrder());
    }
    public Action motifOrder(){
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
            return gpp;
        }
        if (motifTag == 22) {
            return pgp;
        }
        return ppg;
    }
    public Action intake(int xCoordinate){
        drive.updatePoseEstimate();
        double loadAngle = Math.toRadians(270);
        double yMod = -1;
        if(redSide){
            loadAngle = Math.toRadians(90);
            yMod = 1;
        }
        Action prepare = drive.actionBuilder(drive.localizer.getPose())
                .strafeTo(new Vector2d(xCoordinate, 0))
                .turnTo(loadAngle)
                .build();
        double intakeDistance = 45;
        if(xCoordinate > 0){
            intakeDistance = 55;
        }
        Action intakeMove = drive.actionBuilder(new Pose2d(xCoordinate, 0, loadAngle))
                .strafeTo(new Vector2d(xCoordinate, 14*yMod))
                .strafeTo(new Vector2d(xCoordinate, intakeDistance*yMod), velocity(6))
                .strafeTo(new Vector2d(0, 0), velocity(20))
                .build();
        Action intake = new RaceAction(
                intakeMove,
                bobot.intake()
        );
        return new SequentialAction(prepare, intake);
    }
    private VelConstraint velocity(double vel){
        return (pose2dDual, posePath, v) -> vel;
    }
    public class ScanTag implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(motifTag == -1){
                motifTag = bobot.bot.getMotifTag();
            }
            telemetryPacket.addLine("Tag Seen: "+motifTag);
            return motifTag == -1;
        }
    }
    public Action scanTag(){return new ScanTag();}
}