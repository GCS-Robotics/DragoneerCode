package org.firstinspires.ftc.teamcode.robot_modules.sensors;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.Module;

public class Limelight extends Module<Boolean> {

    Limelight3A limelight;

    double tx;
    double ty;
    double ta;

    public Limelight(HardwareMap hardwareMap, String name) {
        limelight = hardwareMap.get(Limelight3A.class, name);
        limelight.pipelineSwitch(1);
        limelight.start();
    }

    @Override
    public void run(Boolean aprilTagging) {
        if (aprilTagging == true) {
            trackBalls();
        }
    }

    public void trackBalls() {
        LLResult result = limelight.getLatestResult();
        tx = result.getTx();
        ty = result.getTy();
        ta = result.getTa();
    }

    @Override
    public void stop() {

    }

    @Override
    public void postTelemetry(Telemetry telemetry) {
        telemetry.addData("Tx:", tx);
        telemetry.addData("Ty:", ty);
        telemetry.addData("Ta:", ta);
    }
}
