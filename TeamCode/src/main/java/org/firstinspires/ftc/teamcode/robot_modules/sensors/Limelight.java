package org.firstinspires.ftc.teamcode.robot_modules.sensors;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
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
    double goalPose;
    double distance;
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
    @Override
    public void postTelemetry(Telemetry telemetry) {

    }
}
