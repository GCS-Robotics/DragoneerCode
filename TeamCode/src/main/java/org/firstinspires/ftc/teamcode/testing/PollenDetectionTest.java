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
        limelight.pipelineSwitch(6);
        waitForStart();
        limelight.start();
        while(opModeIsActive()){
            telemetry.addData("Something", "Yeah");
            telemetry.update();
        }
    }
}
