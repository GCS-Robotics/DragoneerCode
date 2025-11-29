package org.firstinspires.ftc.teamcode.resources;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class WebcamUtilities {
    /** AprilTag processor instance */
    private final AprilTagProcessor aprilTag;

    /** Vision portal instance */
    private final VisionPortal visionPortal;

    public WebcamUtilities(HardwareMap hardwareMap){
        // Create the AprilTag processor the easy way.
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();

        // Create the vision portal the easy way.
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), aprilTag);
    }
    public void endVisionPortal(){
        visionPortal.close();
    }
    public ArrayList<Integer> getAprilTag() {
        ArrayList<Integer> detections = new ArrayList<>();
        ArrayList<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for(AprilTagDetection detection : currentDetections){
            if(detection.metadata != null){
                detections.add(detection.id);
            }
        }
        return detections; // If no tags are detected
    }
}
