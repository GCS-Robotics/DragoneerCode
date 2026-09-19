package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Pollen Test", group = "Test")
public class PollenDetectionTest extends LinearOpMode {
    Limelight3A limelight;
    @Override
    public void runOpMode() throws InterruptedException {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        waitForStart();
        limelight.start();
        while(opModeIsActive()){
            runTelemetry();
        }
    }
    private void runTelemetry(){
        if (limelight != null) {
            com.qualcomm.hardware.limelightvision.LLResult result = limelight.getLatestResult();
            if (result != null && result.isValid()) {
                double tx = result.getTx();
                double ty = result.getTy();
                double ta = 0;
                java.util.List<com.qualcomm.hardware.limelightvision.LLResultTypes.DetectorResult> detectorResults = result.getDetectorResults();
                if (detectorResults != null && !detectorResults.isEmpty()) {
                    ta = detectorResults.get(0).getTargetArea();
                }
                telemetry.addLine("tx: %f" + tx);
                telemetry.addLine("ty: %f" + ty);
                telemetry.addLine("ta: %f" + ta);
            } else {
                telemetry.addLine("No Target Detected");
            }
        } else {
            telemetry.addLine("Limelight not initialized");
        }
        telemetry.update();
    }

}
