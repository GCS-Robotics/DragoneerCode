package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.resources.base_function.DecodeBot;

@TeleOp(name = "Main Drive (1P)", group = "Main Drive")
public class MainSoloDrive extends LinearOpMode {
    DecodeBot bot;
    @Override
    public void runOpMode() throws InterruptedException {
        bot = new DecodeBot(hardwareMap, telemetry, FtcDashboard.getInstance().getTelemetry());
        waitForStart();
        while(opModeIsActive()){
            bot.run(gamepad1);
            bot.runIntake(gamepad1.left_bumper);
            bot.setOuttake(
                    gamepad1.startWasPressed(),
                    gamepad1.backWasPressed(),
                    gamepad1.xWasPressed(),
                    gamepad1.aWasPressed()
            );
            bot.tweakRPM(
                    gamepad1.dpadUpWasPressed(),
                    gamepad1.dpadDownWasPressed(),
                    50
            );
            bot.postTelemetry();
        }
        bot.drum.resetStatics();
    }
}
