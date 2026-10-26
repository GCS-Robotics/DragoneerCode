package org.firstinspires.ftc.teamcode.robot_modules.sensors;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.Module;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;

public class Limelight extends Module<Boolean> {
    Limelight3A limelight;
    private LLResult ballResult;
    private LLResult tagResult;
    final int APRILTAG_PIPELINE = 0;
    final int BALL_PIPELINE = 1;
    double distanceFloor;
    double distance;
    double ty;
    final double GOAL_HEIGHT = 54;
    final double CAMERA_HEIGHT = 8.5;
    final double MOUNTING_ANGLE = 0;
    public Limelight(HardwareMap hardwareMap, String name) {
        limelight = hardwareMap.get(Limelight3A.class, name);
        limelight.start();
    }
    public void trackBalls() {
        limelight.pipelineSwitch(BALL_PIPELINE);
        ballResult = limelight.getLatestResult();
    }
    public void aprilTag(){
        limelight.pipelineSwitch(APRILTAG_PIPELINE);
        tagResult = limelight.getLatestResult();

    }
    public double getDistance(){
        ty = tagResult.getTy();
        distanceFloor = (GOAL_HEIGHT - CAMERA_HEIGHT) / Math.tan(MOUNTING_ANGLE + ty);
        distance = Math.sqrt(Math.pow(distanceFloor, 2) + Math.pow(GOAL_HEIGHT - CAMERA_HEIGHT, 2));
        return distance;
    }
    @Override
    public void postTelemetry(Telemetry telemetry) {

    }
}
