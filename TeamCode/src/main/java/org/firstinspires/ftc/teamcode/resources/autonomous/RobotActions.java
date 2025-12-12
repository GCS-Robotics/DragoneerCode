package org.firstinspires.ftc.teamcode.resources.autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.resources.base_function.DecodeBot;

public class RobotActions {
    public DecodeBot bot;
    public RobotActions(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry){
        bot = new DecodeBot(hardwareMap, telemetry, dashTelemetry, new int[]{-1,-1,-1});
    }
    public RobotActions(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry, int[] preload){
        bot = new DecodeBot(hardwareMap, telemetry, dashTelemetry, preload);
    }
    // Intake Action
    public class Intake implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            bot.runIntake(true);
            bot.drum.run(true);
            return true;
        }
    }
    public Action intake(){return new Intake();}
    // Stop Intake Action
    public class StopIntake implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            bot.intake.run(false);
            bot.drum.run(true);
            return true;
        }
    }
    public Action stopIntake(){return new StopIntake();}
    // Prime Launch Action
    public class PrimeLaunch implements Action{
        boolean init = true;
        double rpm;
        private PrimeLaunch(double speed){
            rpm = speed;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(init){
                bot.flywheels.prime();
                init = false;
            }
            bot.flywheels.run(true);
            return !bot.flywheels.launchersAtSpeed();
        }
    }
    public Action primeLaunch(double rpm){return new PrimeLaunch(rpm);}
    // Cancel Launch Action
    public class CancelLaunch implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            bot.flywheels.cancel();
            return false;
        }
    }
    public Action cancelLaunch(){return new CancelLaunch();}
    // Fire Ball Action
    public class FireArtifact implements Action{
        int artifact;
        ElapsedTime timer;
        boolean init = true;
        boolean completed = false;
        boolean drumRotated = false;
        public FireArtifact(int artifact){
            this.artifact = artifact;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(init){
                if(!bot.flywheels.isPrimed()){
                    bot.flywheels.prime();
                }
                bot.drum.setDrumLaunch(artifact);
                bot.retractKicker();
            }
            bot.drum.run(true);
            bot.flywheels.run(true);
            if(drumRotated && timer.seconds() < 0.125){
                completed = true;
            }
            if(bot.drum.reachedTarget()){
                drumRotated = true;
                timer = new ElapsedTime();
                bot.deployKicker();
            }
            return !completed;
        }
    }
    public Action fireArtifact(int artifact){return new FireArtifact(artifact);}
}
