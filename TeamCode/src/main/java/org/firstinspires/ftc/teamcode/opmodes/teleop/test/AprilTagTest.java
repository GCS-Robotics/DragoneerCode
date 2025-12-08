package org.firstinspires.ftc.teamcode.opmodes.teleop.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.resources.WebcamUtilities;

import java.util.List;

@TeleOp(name = "April Tag Detection Test", group = "Test")
public class AprilTagTest extends LinearOpMode {
    WebcamUtilities webcamUtils;
    @Override
    public void runOpMode() throws InterruptedException {
        webcamUtils = new WebcamUtilities(hardwareMap);
        waitForStart();
        while(opModeIsActive()){
            List<Integer> detections = webcamUtils.getAprilTag();
            for(int i = 0; i < detections.size(); i++){
                telemetry.addData("Detection "+i, detections.get(i));
            }
            telemetry.update();
        }
    }
}
