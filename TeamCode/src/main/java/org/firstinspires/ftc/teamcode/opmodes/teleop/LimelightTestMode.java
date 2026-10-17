package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot_modules.Intake;
import org.firstinspires.ftc.teamcode.robot_modules.Limelight;
import org.firstinspires.ftc.teamcode.robot_modules.MecanumDrive;
import org.firstinspires.ftc.teamcode.robot_modules.Module;
import org.firstinspires.ftc.teamcode.robot_modules.SimpleShooter;
import org.firstinspires.ftc.teamcode.robot_modules.MotorTransition;

@TeleOp(name = "LimelighttestMode", group = "Drive")
public class LimelightTestMode extends OpMode {
    public Module drive, intake, transition, shooter, limelight;

    private Module[] modules;
    @Override
    public void init(){
        limelight = new Limelight(hardwareMap, "limelight");
        modules = new Module[]{limelight};
    }
    @Override
    public void loop(){
        limelight.run(gamepad2.left_bumper);
        Module.postTelemetries(modules, telemetry);
    }
    @Override
    public void stop(){
        Module.stopModules(modules);
    }
}
