package org.firstinspires.ftc.teamcode.resources.autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.resources.States;
import org.firstinspires.ftc.teamcode.resources.base_function.DecodeBot;

public class RobotActions {
    public DecodeBot bot;
    final double launchRPM = 1800;
    public RobotActions(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry){
        this(hardwareMap, telemetry, dashTelemetry, new States.Artifact[]{States.Artifact.GREEN , States.Artifact.PURPLE, States.Artifact.PURPLE});
    }
    public RobotActions(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry, States.Artifact[] preload){
        bot = new DecodeBot(hardwareMap, telemetry, dashTelemetry, preload);
        bot.flywheels.setTargetRPM(launchRPM);
        bot.flywheels.cancel();
    }
    // Intake Action
    public class Intake implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            bot.run();
            bot.runIntake(true);
            return true;
        }
    }
    public Action intake(){return new Intake();}
    // Stop Intake Action
    public class StopIntake implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            bot.run();
            bot.runIntake(false);
            return bot.state == States.General.INTAKE;
        }
    }
    public Action stopIntake(){return new StopIntake();}
    // Prime Launch Action
    public class PrimeLaunch implements Action{
        boolean init = true;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(init){
                bot.flywheels.setTargetRPM(launchRPM);
                bot.setOuttake(true, false, false, false);
                init = false;
            }
            bot.drum.run(true);
            bot.flywheels.run(true);
            telemetryPacket.addLine("Priming Launch!");
            return bot.flywheels.state != States.Outtake.READY || bot.drum.state == States.DrumState.MOVING;
        }
    }
    public Action primeLaunch(){return new PrimeLaunch();}
    // Cancel Launch Action
    public class CancelLaunch implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            bot.run();
            bot.setOuttake(false, true, false, false);
            return bot.state == States.General.IDLE;
        }
    }
    public Action cancelLaunch(){return new CancelLaunch();}
    // Fire Ball Action
    public class FireArtifact implements Action{
        States.Artifact artifact;
        ElapsedTime timer;
        boolean init = true;
        boolean completed = false;
        boolean drumRotated = false;
        double startTime = -1;
        public FireArtifact(States.Artifact artifact){
            this.artifact = artifact;
            timer = new ElapsedTime();
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(init){
                if(!bot.flywheels.active()){
                    bot.setOuttake(true, false, false, false);
                }
                if(artifact == States.Artifact.PURPLE){
                    bot.setOuttake(false, false, true, false);
                }
                if(artifact == States.Artifact.GREEN){
                    bot.setOuttake(false, false, false, true);
                }
                init = bot.state != States.General.LAUNCHING;
                return true;
            }
            bot.run();
            if(bot.state == States.General.PRIMED && !drumRotated){
                drumRotated = true;
                startTime = timer.seconds();
            }
            if(startTime != -1 && timer.seconds() - startTime >= 0.5){
                completed = true;
            }
            return !completed;
        }
    }
    public Action fireArtifact(States.Artifact artifact){return new FireArtifact(artifact);}
}
