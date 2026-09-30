package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot_modules.FlowerCollectors;
import org.firstinspires.ftc.teamcode.robot_modules.Intake;
import org.firstinspires.ftc.teamcode.robot_modules.Module;
import org.firstinspires.ftc.teamcode.robot_modules.ServoTransition;
import org.firstinspires.ftc.teamcode.robot_modules.SimpleShooter;
import org.firstinspires.ftc.teamcode.robot_modules.TankDrive;

@TeleOp(name = "Sprint One Drive", group = "Drive")
public class Sprint1Drive extends OpMode {
    public Module drive, intake;
    private final Module[] modules = {drive, intake};
    @Override
    public void init(){
        drive = new TankDrive(hardwareMap, "left_drive", "right_drive");
        intake = new Intake(hardwareMap, "intake");
    }
    @Override
    public void loop(){
        drive.run(gamepad1);
        intake.run(gamepad2.left_trigger);
        Module.postTelemetries(modules, telemetry);
    }
    @Override
    public void stop(){
        Module.stopModules(modules);
    }
}
