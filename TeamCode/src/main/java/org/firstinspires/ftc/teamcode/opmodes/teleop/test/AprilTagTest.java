package org.firstinspires.ftc.teamcode.opmodes.teleop.test;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

public class AprilTagTest extends LinearOpMode {
    Limelight3A limelight;
    @Override
    public void runOpMode() throws InterruptedException {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
        waitForStart();
        while(opModeIsActive()){
            LLResult result = limelight.getLatestResult();
            if(result != null){
                if(result.isValid()){
                    List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
                    telemetry.addData("Tag Amount", fiducialResults.size());
                    for(int i = 0; i < fiducialResults.size(); i++){
                        telemetry.addData("Tag "+i, fiducialResults.get(i).getFiducialId());
                    }
                }
            }
        }
    }
}
