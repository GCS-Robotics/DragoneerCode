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
    private final Telemetry telemetry;

    public WebcamUtilities(HardwareMap hardwareMap, Telemetry telemetry1){
        telemetry = telemetry1;
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
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for(AprilTagDetection detection : currentDetections){
            if(detection.metadata != null){
                detections.add(detection.id);
            }
        }
        return detections; // If no tags are detected
    }
    public void telemetryAprilTag() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            telemetry.addLine(detection.metadata.toString());
//            if (detection.metadata != null) {
//                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
//                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
//                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
//                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
//            } else {
//                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
//                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
//
//            }

            // Add raw pose info if available
            if (detection.rawPose != null) {
                Orientation rot = Orientation.getOrientation(
                        detection.rawPose.R, AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                telemetry.addData("X Distance", detection.rawPose.x);
                telemetry.addData("Y Distance", detection.rawPose.y);
                telemetry.addData("Z Distance", detection.rawPose.z);
                telemetry.addData("X Angle", rot.firstAngle);
                telemetry.addData("Y Angle", rot.secondAngle);
                telemetry.addData("Z Angle", rot.thirdAngle);
            }
        }   // end for() loop

        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");

    }
}
