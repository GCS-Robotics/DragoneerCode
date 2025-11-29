package org.firstinspires.ftc.teamcode.automodes;




import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.resources.AutonomousMovements;
import org.firstinspires.ftc.teamcode.resources.WebcamUtilities;
import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;
import org.firstinspires.ftc.teamcode.resources.RobotMechanisms;

import java.util.List;

@Autonomous(name="Three Cycle - Blue")
public class BlueAutonomous extends LinearOpMode {
    MecanumDrive drive;
    RobotMechanisms bobot;
    AutonomousMovements moves;
    WebcamUtilities webcamUtils;
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, new Pose2d(-16, 0, Math.toRadians(-90)));
        bobot = new RobotMechanisms(hardwareMap, telemetry, FtcDashboard.getInstance().getTelemetry());
        moves = new AutonomousMovements(drive, bobot, false);
        webcamUtils = new WebcamUtilities(hardwareMap);
        List<Integer> tags = webcamUtils.getAprilTag();
        int motifTag = -1;
        for(int tag : tags){
            if(tag >= 21 && tag <=23){
                motifTag = tag;
            }
        }
        telemetry.addData("Motif Seen", motifTag);
        waitForStart();
        Actions.runBlocking(
                new SequentialAction(
                        moves.intake(-12),
                        moves.fireMotif(135),
                        moves.intake(12),
                        moves.fireMotif(135),
                        moves.intake(24),
                        moves.fireMotif(135)
            )
        );
        webcamUtils.endVisionPortal();
    }
}
