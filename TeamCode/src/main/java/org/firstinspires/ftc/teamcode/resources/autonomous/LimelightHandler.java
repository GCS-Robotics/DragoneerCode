package org.firstinspires.ftc.teamcode.resources.autonomous;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.Collections;
import java.util.List;

public class LimelightHandler {
    Limelight3A limelight;
    final int APRILTAG_PIPELINE = 0;
    final int MEGATAG_PIPELINE = 1;
    public LimelightHandler(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(APRILTAG_PIPELINE);
        limelight.start();
    }
    public List<FiducialResult> findAprilTags(){
        LLResult result = limelight.getLatestResult();
        // If the result we have isn't what we want
        if(result.getPipelineIndex() != APRILTAG_PIPELINE || !result.isValid()){
            limelight.pipelineSwitch(APRILTAG_PIPELINE);
            return Collections.emptyList();
        }
        // If the result is what we want
        return result.getFiducialResults();
    }
    public Pose3D getPose(double robotYaw){
        LLResult result = limelight.getLatestResult();
        limelight.updateRobotOrientation(robotYaw);
        // If the result we have isn't what we want
        if(result.getPipelineIndex() != MEGATAG_PIPELINE || !result.isValid()){
            limelight.pipelineSwitch(MEGATAG_PIPELINE);
            return null;
        }
        // If the result is what we want
        return result.getBotpose_MT2();
    }
}
