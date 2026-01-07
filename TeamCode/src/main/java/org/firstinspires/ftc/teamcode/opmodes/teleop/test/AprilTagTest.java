package org.firstinspires.ftc.teamcode.opmodes.teleop.test;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.resources.autonomous.LimelightHandler;

import java.util.List;

@TeleOp(name = "April Tag Test", group = "Test")
public class AprilTagTest extends LinearOpMode {
    LimelightHandler limelight;
    @Override
    public void runOpMode() throws InterruptedException {
        limelight = new LimelightHandler(hardwareMap);
        waitForStart();
        while(opModeIsActive()){
            List<FiducialResult> fiducialResults = limelight.findAprilTags();
            telemetry.addLine("Fiducial Results:");
            for(int i = 0 ; i < fiducialResults.size(); i ++){
                telemetry.addData("Tag "+i, fiducialResults.get(i).getFiducialId());
            }
            telemetry.update();
        }
    }
}
