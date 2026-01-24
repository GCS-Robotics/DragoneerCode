package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.resources.base_function.DecodeBot;
import org.firstinspires.ftc.teamcode.resources.base_function.Drum;

@TeleOp(name = "Main Drive (2P)", group = "A Main Drive")
public class MainDuoDrive extends LinearOpMode {
    DecodeBot bot;
    @Override
    public void runOpMode() throws InterruptedException {
        bot = new DecodeBot(hardwareMap, telemetry, FtcDashboard.getInstance().getTelemetry());
        waitForStart();
        while(opModeIsActive()){
            bot.run(gamepad1);
            bot.runIntake(gamepad1.left_bumper);
            bot.setOuttake(
                    gamepad2.startWasPressed(),
                    gamepad2.backWasPressed(),
                    gamepad2.xWasPressed(),
                    gamepad2.aWasPressed()
            );
            if(gamepad2.dpadDownWasPressed()){
                bot.flywheels.targetRPM -= 10;
            }
            if(gamepad2.dpadUpWasPressed()){
                bot.flywheels.targetRPM += 10;
            }
            if(gamepad2.leftBumperWasReleased()){
                Drum.targetPosition -=bot.drum.ROTATION_TICK/36;
            }
            if(gamepad2.rightBumperWasPressed()){
                Drum.targetPosition +=bot.drum.ROTATION_TICK/36;
            }
            bot.postTelemetry();
        }
        bot.drum.resetStatics();
    }
}
