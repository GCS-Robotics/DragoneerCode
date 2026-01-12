package org.firstinspires.ftc.teamcode.opmodes.teleop.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.Line;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.resources.autonomous.LimelightHandler;

import java.util.List;

@TeleOp(name = "Megatag Test", group = "Test")
public class MegatagTest extends LinearOpMode {
    LimelightHandler limelight;
    Telemetry dashTel;
    IMU imu;
    @Override
    public void runOpMode() throws InterruptedException {
        dashTel = FtcDashboard.getInstance().getTelemetry();
        limelight = new LimelightHandler(hardwareMap);
        imu = hardwareMap.get(IMU.class, "imu");
        waitForStart();
        while(opModeIsActive()){
            Pose3D pose = limelight.getPose(imu.getRobotYawPitchRollAngles().getYaw());
            if(pose != null){
                for(Telemetry telemetry : new Telemetry[]{telemetry, dashTel}){
                    telemetry.addData("X", pose.getPosition().x);
                    telemetry.addData("Y", pose.getPosition().y);
                    telemetry.addData("Z", pose.getPosition().z);
                    telemetry.update();
                }
            } else{
                for(Telemetry telemetry : new Telemetry[]{telemetry, dashTel}){
                    telemetry.addLine("No valid pose");
                    telemetry.update();
                }
            }
        }
    }
}
