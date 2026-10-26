package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot_modules.Intake;
import org.firstinspires.ftc.teamcode.robot_modules.sensors.Limelight;
import org.firstinspires.ftc.teamcode.robot_modules.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.robot_modules.Module;
import org.firstinspires.ftc.teamcode.robot_modules.outtake.SimpleShooter;
import org.firstinspires.ftc.teamcode.robot_modules.transition.MotorTransition;

@TeleOp(name = "Sprint One Drive", group = "Drive")
public class Sprint1Drive extends OpMode {
    public Module drive, intake, transition, shooter, limelight;
    private Module[] modules;
    @Override
    public void init(){
        drive = new MecanumDrive(hardwareMap, "frontRight", "backRight", "frontLeft", "backLeft");
        intake = new Intake(hardwareMap, "intake");
        transition = new MotorTransition(hardwareMap, "transitionMotor");
        shooter = new SimpleShooter(hardwareMap, "shooter");
        limelight = new Limelight(hardwareMap, "limelight");
        modules = new Module[]{drive, intake, transition, shooter};
    }
    @Override
    public void loop(){
        drive.run(gamepad1);
        intake.run(gamepad2.left_trigger);
        transition.run(new Boolean[]{gamepad2.b, gamepad2.x});
        shooter.run(gamepad2.right_trigger);
        limelight.run(gamepad2.left_bumper);
        Module.postTelemetries(modules, telemetry);
    }
    @Override
    public void stop(){
        Module.stopModules(modules);
    }
}
