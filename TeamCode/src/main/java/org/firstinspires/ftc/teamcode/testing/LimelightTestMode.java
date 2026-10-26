package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot_modules.sensors.Limelight;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;

@TeleOp(name = "LimelighttestMode", group = "Drive")
public class LimelightTestMode extends OpMode {
    public RunModule limelight;
    boolean aprilTagging = true;

    private RunModule[] modules;
    @Override
    public void init(){
        limelight = new Limelight(hardwareMap, "limelight");
        modules = new RunModule[]{limelight};
    }
    @Override
    public void loop(){
        limelight.run(aprilTagging);
        RunModule.postTelemetries(modules, telemetry);
    }
    @Override
    public void stop(){
        RunModule.stopModules(modules);
    }
}
