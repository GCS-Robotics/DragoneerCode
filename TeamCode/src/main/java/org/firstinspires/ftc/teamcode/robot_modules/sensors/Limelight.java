package org.firstinspires.ftc.teamcode.robot_modules.sensors;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.Module;

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
    final double CAMERA_HEIGHT = 13;
    final double MOUNTING_ANGLE = 0;
    final double G = 9.81;
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
    private double getDistance(){
        ty = tagResult.getTy();
        distanceFloor = (GOAL_HEIGHT - CAMERA_HEIGHT) / Math.tan(MOUNTING_ANGLE + ty);
        distance = Math.sqrt(Math.pow(distanceFloor, 2) + Math.pow(GOAL_HEIGHT - CAMERA_HEIGHT, 2));
        return distance;
    }
    private double getXDistance(){
        return distanceFloor;
    }
    private double getYDistance(){
        return GOAL_HEIGHT - CAMERA_HEIGHT;
    }
    public double getAngleRads(double min, double max){
        double h = getYDistance();
        double d = getXDistance();
        double angle = Math.atan((h+Math.sqrt(h*h+d*d)/d));
        return Math.min(Math.max(angle,min), max);
    }
    public double getVelocity(double launchAngle){
        double h = getYDistance();
        double d = getXDistance();
        // Returns ball velocity in Meters per Second
        double numerator = (G*d*d);
        double denominator = 2*Math.pow(Math.cos(launchAngle),2)*(d*Math.tan(launchAngle)-h);
        return Math.sqrt(numerator / denominator);
    }

    public double getAngleDegrees(double min, double max) {
        double h = getYDistance();
        double d = getXDistance();
        double angle = Math.atan((h+Math.sqrt(h*h+d*d)/d));
        double angleDegrees = Math.toDegrees(angle);
        return Math.min(Math.max(angleDegrees,min), max);
    }

    public double getYaw() {
        return tagResult.getTx();
    }


    @Override
    public void postTelemetry(Telemetry telemetry) {
        telemetry.addData("Distance: ", getDistance());
        telemetry.addData("Angle: ", getAngleRads(0.383972435, 0.785398));
        telemetry.addData("Velocity: ", getVelocity(getAngleRads(0.383972435, 0.785398)));
    }
}
