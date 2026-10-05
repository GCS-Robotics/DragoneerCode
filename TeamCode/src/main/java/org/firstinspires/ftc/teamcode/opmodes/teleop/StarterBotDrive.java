package org.firstinspires.ftc.teamcode.opmodes.teleop;

import android.transition.Transition;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robot_modules.FlowerCollectors;
import org.firstinspires.ftc.teamcode.robot_modules.Intake;
import org.firstinspires.ftc.teamcode.robot_modules.Module;
import org.firstinspires.ftc.teamcode.robot_modules.ServoTransition;
import org.firstinspires.ftc.teamcode.robot_modules.Shooter;
import org.firstinspires.ftc.teamcode.robot_modules.SimpleShooter;
import org.firstinspires.ftc.teamcode.robot_modules.TankDrive;

@TeleOp(name = "Starter Bot Drive", group = "Drive")
public class StarterBotDrive extends OpMode {
    public Module drive, intake, transition, shooter, flowerCollector;
    private Module[] modules;
    @Override
    public void init(){
        // Initializing Modules
        drive = new TankDrive(hardwareMap, "left_drive", "right_drive");
        intake = new Intake(hardwareMap, "intake");
        transition = new ServoTransition(hardwareMap, "transition");
        shooter = new SimpleShooter(hardwareMap, "outtake");
        flowerCollector = new FlowerCollectors(hardwareMap, new String[]{"left_intake", "right_intake"});
        modules = new Module[]{drive, intake, transition, shooter, flowerCollector};
    }
    @Override
    public void loop(){
        drive.run(gamepad1);
        transition.run(new Boolean[]{gamepad2.b, gamepad2.x});
        intake.run(gamepad2.left_trigger);
        shooter.run(gamepad2.right_trigger);
        flowerCollector.run(gamepad2.a);
        Module.postTelemetries(modules, telemetry);
    }
    @Override
    public void stop(){
        Module.stopModules(modules);
    }
}