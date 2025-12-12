package org.firstinspires.ftc.teamcode.resources.autonomous;

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

import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;

public class AutonomousMovements {
    private MecanumDrive drive;
    private RobotActions bobot;
    private boolean mod;
    private int motifTag;

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

    public Action fireMotif(Pose2d startPose) {
        int modifier = 1;
        double angle = Math.toRadians(225);
        if(mod){
            modifier = -1;
            angle = Math.toRadians(360-225);
        }
        Action getReady = new ParallelAction(
                drive.actionBuilder(startPose)
                        .strafeTo(new Vector2d(24, -24*modifier))
                        .turnTo(Math.toRadians(angle))
                        .build(),
                bobot.primeLaunch(1000));
        Action gpp = new SequentialAction(
                bobot.fireArtifact(0),
                bobot.fireArtifact(1),
                bobot.fireArtifact(1),
                bobot.cancelLaunch());
        Action pgp = new SequentialAction(
                bobot.fireArtifact(1),
                bobot.fireArtifact(0),
                bobot.fireArtifact(1),
                bobot.cancelLaunch());
        Action ppg = new SequentialAction(
                bobot.fireArtifact(1),
                bobot.fireArtifact(1),
                bobot.fireArtifact(0),
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
        int modifier = 1;
        if(mod){
            modifier = -1;
        }
        return fireMotif(new Pose2d(new Vector2d(24, -24 * modifier), Math.toDegrees(90)));
    }


    public Action intake(int xCoordinate, Pose2d startPose){
        int modifier = 1;
        if(mod){
            modifier = -1;
        }
        VelConstraint vel = new VelConstraint() {
            @Override
            public double maxRobotVel(@NonNull Pose2dDual<Arclength> pose2dDual, @NonNull PosePath posePath, double v) {
                return 10;
            }
        };
        return new SequentialAction(
                drive.actionBuilder(drive.localizer.getPose())
                        .strafeTo(new Vector2d(xCoordinate, -24*modifier))
                        .turnTo(Math.toDegrees(90))
                        .build(),
                new RaceAction(
                        drive.actionBuilder(new Pose2d(xCoordinate, -24*modifier, Math.toDegrees(90)))
                                .strafeTo(new Vector2d(xCoordinate, -50*modifier), vel)
                                .strafeTo(new Vector2d(xCoordinate, -24*modifier), vel)
                                .build(),
                        bobot.intake()
                ),
                drive.actionBuilder(new Pose2d(xCoordinate, -24*modifier, Math.toDegrees(90)))
                        .strafeTo(new Vector2d(24, -24*modifier))
                        .build(),
                bobot.stopIntake()
        );
    }

    public Action intake(int xCoordinate){
        int modifier = 1;
        double angle = Math.toRadians(225);
        if(mod){
            modifier = -1;
            angle = Math.toRadians(360-225);
        }
        return intake(xCoordinate, new Pose2d(new Vector2d(24, -24*modifier), angle));
    }
}
