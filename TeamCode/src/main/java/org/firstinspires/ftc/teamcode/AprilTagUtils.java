package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
@TeleOp(name ="Apriltag test")
@Disabled
public class AprilTagUtils extends LinearOpMode {
    public int getValidAprilTag(Pose2d position, AprilTagDetection[] apriltags) {
        for (AprilTagDetection detection : apriltags)
        telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
         // Should return the April Tag ID that corresponds to the correct motif. Return -1 if it is not discernable.
        return -1;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // On Init
        waitForStart();
        while(opModeIsActive()){
            // Whatever we want happening while opmode is active
        }
    }
}
