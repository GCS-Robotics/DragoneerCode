package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.resources.base_function.DecodeBot;

@TeleOp(name = "Main Drive (1P)", group = "A Main Drive")
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
                    gamepad1.right_trigger > 0.2,
                    gamepad1.rightBumperWasPressed(),
                    gamepad1.xWasPressed(),
                    gamepad1.aWasPressed()
            );
            if(gamepad1.dpadDownWasPressed()){
                bot.flywheels.targetRPM -= 10;
            }
            if(gamepad1.dpadUpWasPressed()){
                bot.flywheels.targetRPM += 10;
            }
            bot.postTelemetry();
        }
        bot.drum.resetStatics();
    }
}
