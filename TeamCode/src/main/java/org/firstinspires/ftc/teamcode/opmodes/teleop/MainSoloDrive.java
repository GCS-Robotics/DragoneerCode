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
            bot.drive.runDrive(gamepad1);
            if(!bot.busy){
                bot.runIntake(gamepad1.left_bumper && !bot.launching && !bot.flywheels.isPrimed()); // Run Intake
                if(gamepad1.start && !bot.flywheels.isPrimed()){ // Start Launchers
                    bot.drum.outtakeMode();
                    bot.flywheels.prime();
                    bot.kicker.retract();
                }
                if(gamepad1.back && bot.flywheels.isPrimed() && !bot.launching){ // Stop Launchers
                    bot.flywheels.cancel();
                    bot.kicker.retract();
                }
                if(gamepad1.x && bot.flywheels.isPrimed() && !bot.launching){ // Launch Purple
                    bot.kicker.retract();
                    bot.launchBall(1);
                }
                if(gamepad1.a && bot.flywheels.isPrimed() && !bot.launching){ // Launch Green
                    bot.kicker.retract();
                    bot.launchBall(0);
                }
                if(gamepad1.dpadUpWasPressed()){ // Increase Launch Speed
                    bot.flywheels.setTargetRPM(bot.flywheels.getTargetRPM()+50);
                }
                if(gamepad1.dpadDownWasPressed() &&
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
