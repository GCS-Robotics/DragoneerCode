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
        bot.drum.intakeMode();
        while(opModeIsActive()){
            bot.drive.runDrive(gamepad1);
            bot.runIntake(gamepad1.left_bumper && !bot.launching && !bot.flywheels.isPrimed()); // Run Intakw
            if(gamepad1.start){ // Start Launchers
                bot.flywheels.prime();
                bot.drum.outtakeMode();
                bot.retractKicker();
            }
            if(gamepad1.back){ // Stop Launchers
                bot.flywheels.cancel();
                bot.drum.intakeMode();
                bot.retractKicker();
            }
            if(gamepad1.x){ // Launch Purple
                bot.retractKicker();
                bot.launchBall(1);
            }
            if(gamepad1.a){ // Launch Green
                bot.retractKicker();
                bot.launchBall(0);
            }
            if(gamepad1.dpadUpWasPressed()){ // Increase Launch Speed
                bot.flywheels.setTargetRPM(bot.flywheels.getTargetRPM()+50);
            }
            if(gamepad1.dpadDownWasPressed() &&
                    bot.flywheels.getTargetRPM()-50 >= 0){ // Decrease Launch Speed
                bot.flywheels.setTargetRPM(bot.flywheels.getTargetRPM()-50);
            }
            bot.drum.run(true);
            bot.flywheels.run(true);
            bot.kick();
            bot.postTelemetry();
        }
    }
}
