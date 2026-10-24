package org.firstinspires.ftc.teamcode.robot_modules;

import com.qualcomm.hardware.limelightvision.LLFieldMap;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;



import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LimelightApriltag extends Module<Boolean> {

    Limelight3A Limelight;

    final int APRILTAG_PIPELINE = 0;
    final int MEGATAG_PIPELINE = 1;

    LLResult goal;

    double goalPose;
    double distance;


    public LimelightApriltag(HardwareMap hardwareMap, String name) {
        Limelight = hardwareMap.get(Limelight3A.class, name);
        Limelight.pipelineSwitch(APRILTAG_PIPELINE);
        Limelight.start();
    }

    @Override
    public void run(Boolean parameter) {
        goal = Limelight.getLatestResult();

    }
    // not getting botPose for now, waiting for odometry.

    @Override
    public void stop() {

    }

    @Override
    public void postTelemetry(Telemetry telemetry) {

    }
}
