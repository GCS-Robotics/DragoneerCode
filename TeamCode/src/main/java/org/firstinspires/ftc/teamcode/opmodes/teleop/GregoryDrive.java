package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robot_modules.Intake;
import org.firstinspires.ftc.teamcode.robot_modules.Module;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;
import org.firstinspires.ftc.teamcode.robot_modules.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.robot_modules.flowers.DeployableRamp;
import org.firstinspires.ftc.teamcode.robot_modules.outtake.AdjustableOuttake;

public class GregoryDrive extends OpMode {
    public RunModule drive, intake, deployableRamp, adjustableOuttake;
    private Module[] modules;
    private final double TRIGGER_THRESHOLD = 0.62;
    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap, "frontRight", "backRight", "frontLeft", "backLeft");
        intake = new Intake(hardwareMap, "intake");
        deployableRamp = new DeployableRamp(hardwareMap, "ramp");
        adjustableOuttake = new AdjustableOuttake(hardwareMap, "flywheel", "hood", "gap");
        modules = new Module[]{drive, intake, deployableRamp, adjustableOuttake};
    }

    @Override
    public void loop() {
        drive.run(gamepad1);
        intake.run(gamepad2.left_trigger);
        deployableRamp.run(gamepad2.right_trigger > TRIGGER_THRESHOLD);
        adjustableOuttake.run(gamepad2);
        Module.postTelemetries(modules, telemetry);
    }

    @Override
    public void stop() {
        Module.stopModules(modules);
    }
}
