package org.firstinspires.ftc.teamcode.robot_modules;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Limelight extends Module<Boolean>{

    Limelight3A limelight;

    @Override
    public void run(Boolean aprilTagging) {
        if (aprilTagging == true) {
            limelight.pipelineSwitch(0);
            limelight.start();


        }
    }

    public void trackBalls(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        LLResult result = limelight.getLatestResult();
        double tx = result.getTx();
        double ty = result.getTy();
        double ta = result.getTa();
    }

    @Override
    public void stop() {

    }

    @Override
    public void postTelemetry(Telemetry telemetry) {

    }
}
