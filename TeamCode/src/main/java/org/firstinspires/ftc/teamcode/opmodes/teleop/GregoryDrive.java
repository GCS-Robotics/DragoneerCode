package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot_modules.Intake;
import org.firstinspires.ftc.teamcode.robot_modules.Module;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;
import org.firstinspires.ftc.teamcode.robot_modules.drive.Mecanum;
import org.firstinspires.ftc.teamcode.robot_modules.flowers.DeployableRamp;
import org.firstinspires.ftc.teamcode.robot_modules.outtake.AdjustableOuttakeManual;
import org.firstinspires.ftc.teamcode.robot_modules.outtake.Flywheel;
import org.firstinspires.ftc.teamcode.robot_modules.sensors.LimelightModule;
import org.firstinspires.ftc.teamcode.robot_modules.transition.MotorTransition;
import org.firstinspires.ftc.teamcode.robot_modules.transition.ServoTransition;

@TeleOp (name = "gregDrive")
public class GregoryDrive extends OpMode {
    public RunModule drive, intake, deployableRamp, adjustableOuttake, motorTransition;
    public Module limelight;
    private Module[] modules;
    private final double TRIGGER_THRESHOLD = 0.62;

    private double[] pidf = new double[]{0.003, 0.123, 0, 0};
    @Override
    public void init() {
        drive = new Mecanum(hardwareMap, "frontRight", "backRight", "frontLeft", "backLeft");
        intake = new Intake(hardwareMap, "intake");
        deployableRamp = new DeployableRamp(hardwareMap, "ramp");
        adjustableOuttake = new AdjustableOuttakeManual(hardwareMap, "flywheel", "hood", "gap", "yaw", "limelight");
        motorTransition = new MotorTransition(hardwareMap, "transitionMotor");
        limelight = new LimelightModule(hardwareMap, "limelight");
        //servoTransition = new ServoTransition(hardwareMap, "transitionServo");
        modules = new Module[]{drive, intake, deployableRamp, adjustableOuttake, motorTransition, limelight};
    }

    @Override
    public void loop() {
        drive.run(gamepad1);
        ((LimelightModule)limelight).aprilTag();
        intake.run(gamepad2.left_trigger > TRIGGER_THRESHOLD);
        deployableRamp.run(gamepad2.right_trigger > TRIGGER_THRESHOLD);
        adjustableOuttake.run(gamepad2);
        motorTransition.run(new Boolean[]{gamepad2.x, gamepad2.y});
        //servoTransition.run(new Boolean[]{gamepad2.x, gamepad2.y});
        Module.postTelemetries(modules, telemetry);
    }

    @Override
    public void stop() {
        Module.stopModules(modules);
    }
}
