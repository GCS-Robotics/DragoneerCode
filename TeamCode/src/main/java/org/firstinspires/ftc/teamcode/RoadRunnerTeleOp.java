package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public final class RoadRunnerTeleOp extends LinearOpMode {
    MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

    @Override
    public void runOpMode() throws InterruptedException {

    }
}