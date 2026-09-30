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
import org.firstinspires.ftc.teamcode.robot_modules.SimpleShooter;
import org.firstinspires.ftc.teamcode.robot_modules.TankDrive;

@TeleOp(name = "Starter Bot Drive", group = "Drive")
public class StarterBotDrive extends OpMode {
    public Module drive, intake, transition, outtake, flowerCollector;
    private final Module[] modules = {drive, intake, transition, outtake, flowerCollector};
    @Override
    public void init(){
        // Initializing Modules
        drive = new TankDrive(hardwareMap, "left_drive", "right_drive");
        intake = new Intake(hardwareMap, "intake");
        transition = new ServoTransition(hardwareMap, "transition");
        outtake = new SimpleShooter(hardwareMap, "outtake");
        flowerCollector = new FlowerCollectors(hardwareMap, new String[]{"left_intake", "right_intake"});
    }
    @Override
    public void loop(){
        drive.run(gamepad1);
        transition.run(gamepad2);
        intake.run(gamepad2.left_trigger);
        outtake.run(gamepad2.right_trigger);
        flowerCollector.run(gamepad2);
        Module.postTelemetries(modules, telemetry);
    }
    @Override
    public void stop(){
        Module.stopModules(modules);
    }
}