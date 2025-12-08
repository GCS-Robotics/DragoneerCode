package org.firstinspires.ftc.teamcode.resources;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.RaceAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;

public class AutonomousMovements {
    private MecanumDrive drive;
    private RobotMechanisms bobot;
    private boolean mod;
    private int motifTag;

    /**
     * Creates the object that handles larger-scale actions in our autonomous modes.
     * @param md The object necessary for movement.
     * @param rm The object necessary for mechanisms.
     * @param m Set to false if on blue-side, set to true if on red-side.
     */
    public AutonomousMovements(MecanumDrive md, RobotMechanisms rm, boolean m){
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

    /**
     * Goes to launching position, then fires balls in motif order. If motif is unknown, will fire Purple, Purple, Green.
     * @param angle The firing angle.
     * @return The action that does everything.
     */
    public Action fireMotif(double angle){
        int modifier = 1;
        if(mod){
            modifier = -1;
        }
        Action getReady = new ParallelAction(
                drive.actionBuilder(drive.localizer.getPose())
                        .strafeTo(new Vector2d(24, -24*modifier))
                        .turnTo(Math.toRadians(angle))
                        .build(),
                bobot.primeLaunch());
        Action gpp = new SequentialAction(
                bobot.fireGreen(),
                bobot.firePurple(),
                bobot.firePurple(),
                bobot.cancelLaunch());
        Action pgp = new SequentialAction(
                bobot.firePurple(),
                bobot.fireGreen(),
                bobot.firePurple(),
                bobot.cancelLaunch());
        Action ppg = new SequentialAction(
                bobot.firePurple(),
                bobot.firePurple(),
                bobot.fireGreen(),
                bobot.cancelLaunch());
        if(motifTag == 21){
            return new SequentialAction(getReady, gpp);
        }
        if(motifTag == 22){
            return new SequentialAction(getReady, pgp);
        }
        return new SequentialAction(getReady, ppg);
    }

    /**
     * Grabs balls from a certain x-coordinate (Blue-side only).
     * @param xCoordinate The coordinate of the balls.
     * @return The action that does everything.
     */
    public Action intake(int xCoordinate){
        int modifier = 1;
        if(mod){
            modifier = -1;
        }
        return new SequentialAction(
                drive.actionBuilder(drive.localizer.getPose())
                        .strafeTo(new Vector2d(xCoordinate, -24*modifier))
                        .turnTo(Math.toDegrees(90))
                        .build(),
                new RaceAction(
                        drive.actionBuilder(drive.localizer.getPose())
                                .strafeTo(new Vector2d(xCoordinate, -34.5*modifier))
                                .strafeTo(new Vector2d(xCoordinate, -39.5*modifier))
                                .strafeTo(new Vector2d(xCoordinate, -44.5*modifier))
                                .strafeTo(new Vector2d(xCoordinate, -24*modifier))
                                .build(),
                        bobot.intake()
                ),
                bobot.stopIntake()
        );
    }
}
