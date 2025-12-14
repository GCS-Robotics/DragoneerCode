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
            bot.drive.runDrive(gamepad1);
            if(!bot.busy){
                bot.runIntake(gamepad2.left_trigger > 0.2 && !bot.launching && !bot.flywheels.isPrimed()); // Run Intake
                if(gamepad2.start && !bot.flywheels.isPrimed()){ // Start Launchers
                    bot.drum.outtakeMode();
                    bot.flywheels.prime();
                    bot.retractKicker();
                }
                if(gamepad2.back && bot.flywheels.isPrimed()){ // Stop Launchers
                    bot.flywheels.cancel();
                    bot.retractKicker();
                }
                if(gamepad2.x){ // Launch Purple
                    bot.retractKicker();
                    bot.launchBall(1);
                }
                if(gamepad2.a){ // Launch Green
                    bot.retractKicker();
                    bot.launchBall(0);
                }
                if(gamepad2.bWasPressed() && !bot.launching){
                    bot.drum.rotateThird();
                }
                if(gamepad2.dpadUpWasPressed()){ // Increase Launch Speed
                    bot.flywheels.setTargetRPM(bot.flywheels.getTargetRPM()+50);
                }
                if(gamepad2.dpadDownWasPressed() &&
                        bot.flywheels.getTargetRPM()-50 >= 0){ // Decrease Launch Speed
                    bot.flywheels.setTargetRPM(bot.flywheels.getTargetRPM()-50);
                }
                if(bot.flywheels.isPrimed()){
                    bot.drum.outtakeMode();
                }
                bot.drum.run(true);
                bot.flywheels.run(true);
                bot.kick();
            }
            if(bot.busy){
                bot.drum.run(true);
                bot.reindexing();
                bot.drive.runDrive(gamepad1);
            }
            bot.postTelemetry();
        }
        bot.drum.resetBalls();
    }
}
