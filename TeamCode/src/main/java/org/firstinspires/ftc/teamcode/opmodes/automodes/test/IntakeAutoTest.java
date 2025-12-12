package org.firstinspires.ftc.teamcode.opmodes.automodes.test;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.resources.AutonomousMovements;
import org.firstinspires.ftc.teamcode.resources.RobotMechanisms;
import org.firstinspires.ftc.teamcode.resources.WebcamUtilities;
import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;

import java.util.List;

@Autonomous(name="Intake Test", group = "Test")
public class IntakeAutoTest extends LinearOpMode {
    MecanumDrive drive;
    RobotMechanisms bobot;
    AutonomousMovements moves;
    WebcamUtilities webcamUtils;
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, new Pose2d(-60, -45, Math.toRadians(52-14)));
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
        if(isStopRequested()){return;}
        Actions.runBlocking(
                new SequentialAction(
                        bobot.intake()
            )
        );
        webcamUtils.endVisionPortal();
    }
}
