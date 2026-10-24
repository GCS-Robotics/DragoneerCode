package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robot_modules.Intake;
import org.firstinspires.ftc.teamcode.robot_modules.Module;
import org.firstinspires.ftc.teamcode.robot_modules.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.robot_modules.flowers.DeployableRamp;

public class GregoryDrive extends OpMode {
    public Module drive, intake, deployableRamp;
    private Module[] modules;
    private final double TRIGGER_THRESHOLD = 0.62;
    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, "frontRight", "backRight", "frontLeft", "backLeft");
        intake = new Intake(hardwareMap, "intake");
        deployableRamp = new DeployableRamp(hardwareMap, "deployableRamp");
        modules = new Module[]{drive, intake, deployableRamp};
    }

    @Override
    public void loop() {
        drive.run(gamepad1);
        intake.run(gamepad2.left_trigger);
        deployableRamp.run(gamepad2.right_trigger > TRIGGER_THRESHOLD);
        Module.postTelemetries(modules, telemetry);
    }

    @Override
    public void stop() {
        Module.stopModules(modules);
    }
}
