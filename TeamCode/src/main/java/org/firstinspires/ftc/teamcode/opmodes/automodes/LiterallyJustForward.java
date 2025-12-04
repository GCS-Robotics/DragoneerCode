package org.firstinspires.ftc.teamcode.opmodes.automodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.resources.RegularMecanumDrive;

@Autonomous(name="Literally just move forward lol")
public class LiterallyJustForward extends LinearOpMode {
    RegularMecanumDrive drive;
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new RegularMecanumDrive(hardwareMap, 0.01);
        waitForStart();
        drive.moveForward(1);
    }
}
