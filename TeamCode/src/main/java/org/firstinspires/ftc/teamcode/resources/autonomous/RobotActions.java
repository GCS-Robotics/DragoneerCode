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
    final double launchRPM = 1800;
    public RobotActions(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry){
        bot = new DecodeBot(hardwareMap, telemetry, dashTelemetry, new int[]{0,1,1});
        bot.flywheels.setTargetRPM(launchRPM);
        bot.flywheels.cancel();
    }
    public RobotActions(HardwareMap hardwareMap, Telemetry telemetry, Telemetry dashTelemetry, int[] preload){
        bot = new DecodeBot(hardwareMap, telemetry, dashTelemetry, preload);
    }
    // Intake Action
    public class Intake implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            bot.flywheels.cancel();
            bot.drum.intakeMode();
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
            bot.drum.intakeMode();
            bot.drum.run(true);
            return false;
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
                bot.flywheels.prime();
                init = false;
            }
            bot.drum.intakeMode();
            bot.drum.run(true);
            bot.flywheels.run(true);
            telemetryPacket.addLine("Priming Launch!");
            return !bot.flywheels.launchersAtSpeed() || !bot.drum.reachedTarget();
        }
    }
    public Action primeLaunch(){return new PrimeLaunch();}
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
        double startTime = -1;
        public boolean secondTime = false;
        public FireArtifact(int artifact){
            this.artifact = artifact;
            timer = new ElapsedTime();
        }
        public FireArtifact(int artifact, boolean secondTime){
            this(artifact);
            this.secondTime = secondTime;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(init){
                if(!bot.flywheels.isPrimed()){
                    bot.flywheels.prime();
                    if(secondTime){
                        bot.drum.targetPosition += bot.drum.ROTATION_TICK / 6;
                    }
                }
                bot.drum.outtakeMode();
                bot.drum.setDrumLaunch(artifact);
                bot.kicker.retract();
                init = false;
                return true;
            }
            bot.drum.run(true);
            bot.flywheels.run(true);
            if(bot.flywheels.launchersAtSpeed() && bot.drum.reachedTarget() && !drumRotated){
                drumRotated = true;
                startTime = timer.seconds();
                bot.kicker.kick();
                bot.drum.launchBall();
            }
            if(startTime != -1 && timer.seconds() - startTime >= 0.5){
                completed = true;
            }
            telemetryPacket.addLine("Drum Done? "+drumRotated);
            telemetryPacket.addLine("Time? "+timer.seconds());
            telemetryPacket.addLine("");
            for(int i=0; i < 3; i++){
                telemetryPacket.addLine(i+": "+bot.drum.getBall(i));
            }
            return !completed;
        }
    }
    public Action fireArtifact(int artifact){return new FireArtifact(artifact);}
    public Action fireArtifact(int artifact, boolean secondTime){return new FireArtifact(artifact, secondTime);}
}
