package org.firstinspires.ftc.teamcode.resources.base_function;

import static java.lang.Math.abs;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.roadrunning_stuff.MecanumDrive;

public class Drive {
    // All the motors
    private final MecanumDrive drive;
    // Used to convert the factor booleans to numbers, which are more useful
    private int factor(boolean f) {
        if(f){return 1;}
        return -1;
    }
    /**
     * Constructs an object in charge of all driving, for chassis that use Mecanum-Wheel Driving.
     * Needs the Motors, a drive-speed multiplier, and the "Factor."
     * If the motors spin the wrong way, set the corresponding factor to False. Otherwise, set it to True.
     * @param hardwareMap Finds the motors from the hardware map.
     */
    public Drive(HardwareMap hardwareMap) {
        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
    }
    public void runDrive(Gamepad gamepad){
        double speed = 1 - gamepad.right_trigger;
        if(gamepad.left_trigger < 0.2){
            speed *= -1;
        }
        drive.setDrivePowers(new PoseVelocity2d(new Vector2d(-gamepad.left_stick_y*speed, -gamepad.left_stick_x*speed), -gamepad.right_stick_x*speed));
    }
}
