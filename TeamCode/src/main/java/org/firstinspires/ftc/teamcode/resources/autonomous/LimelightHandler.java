package org.firstinspires.ftc.teamcode.resources.autonomous;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.Collections;
import java.util.List;
import java.lang.Math;

public class LimelightHandler {
    Limelight3A limelight;
    final int APRILTAG_PIPELINE = 0;
    final int MEGATAG_PIPELINE = 1;

    public LimelightHandler(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(APRILTAG_PIPELINE);
        limelight.start();
    }

    public List<FiducialResult> findAprilTags() {
        LLResult result = limelight.getLatestResult();
        // If the result we have isn't what we want
        if (result.getPipelineIndex() != APRILTAG_PIPELINE || !result.isValid()) {
            limelight.pipelineSwitch(APRILTAG_PIPELINE);
            return Collections.emptyList();
        }
        // If the result is what we want
        return result.getFiducialResults();
    }

    public Pose3D getPose(double robotYaw) {
        LLResult result = limelight.getLatestResult();
        limelight.updateRobotOrientation(robotYaw);
        // If the result we have isn't what we want
        if (result.getPipelineIndex() != MEGATAG_PIPELINE || !result.isValid()) {
            limelight.pipelineSwitch(MEGATAG_PIPELINE);
            return null;
        }
        // If the result is what we want
        return result.getBotpose_MT2();
    }

    public double getDistance(double robotYaw) {
        LLResult result = limelight.getLatestResult();
        limelight.updateRobotOrientation(robotYaw);
        // If the result we have isn't what we want
        if (result.getPipelineIndex() != MEGATAG_PIPELINE || !result.isValid()) {
            limelight.pipelineSwitch(MEGATAG_PIPELINE);
            return -1;
        }
        Pose3D pose = result.getBotpose_MT2();
        int tag = result.getFiducialResults().get(0).getFiducialId();
        Vector2d robotPose = new Vector2d(pose.getPosition().x, pose.getPosition().y);
        Vector2d blueGoal = new Vector2d(-1.6002, -1.5494);
        Vector2d redGoal = new Vector2d(-1.6002, 1.5494);
        Vector2d target;
        if(tag == 20){
            target = blueGoal;
        } else if(tag == 24){
            target = redGoal;
        } else{
            return -1;
        }
        return Math.sqrt(Math.pow(target.y - robotPose.y, 2) + Math.pow(target.x - robotPose.x, 2));
    }
}

