package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.resources.base_function.DecodeBot;

@TeleOp(name = "Main Drive (2P)", group = "Main Drive")
public class MainDuoDrive extends LinearOpMode {
    DecodeBot bot;
    @Override
    public void runOpMode() throws InterruptedException {
        bot = new DecodeBot(hardwareMap, telemetry, FtcDashboard.getInstance().getTelemetry());
        waitForStart();
        while(opModeIsActive()){
            bot.run(gamepad1);
            bot.runIntake(gamepad2.left_trigger > 0.2);
            bot.setOuttake(
                    gamepad2.startWasPressed(),
                    gamepad2.backWasPressed(),
                    gamepad2.xWasPressed(),
                    gamepad2.aWasPressed()
            );
            bot.tweakRPM(
                    gamepad2.dpadUpWasPressed(),
                    gamepad2.dpadDownWasPressed(),
                    50
            );
            bot.postTelemetry();
        }
        bot.drum.resetBalls();
    }
}
