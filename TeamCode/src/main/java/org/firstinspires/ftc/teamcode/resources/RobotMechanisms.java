package org.firstinspires.ftc.teamcode.resources;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotMechanisms {
    private MainDecodeDrive drive;
    public RobotMechanisms(HardwareMap hardwareMap, Telemetry tel, Telemetry dashTel){
        drive = new MainDecodeDrive(hardwareMap, tel, dashTel);
    }
    public class PrimeLaunch implements Action {
        boolean init = true;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(init){
                drive.runOuttake(true, false, false, false);
                init = false;
            }
            return !drive.primed;
        }
    }
    public class CancelLaunch implements Action {
        boolean init = true;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket){
            if(init){
                drive.runOuttake(false, true, false, false);
                init = false;
            }
            return drive.primed;
        }
    }
    public class Intake implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket){
            drive.runIntake(true, false);
            return true;
        }
    }
    public class StopIntake implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket){
            drive.runIntake(false, false);
            return false;
        }
    }
    public class FireGreen implements Action {
        boolean init = true;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(init){
                drive.runOuttake(false, false, false, true);
                init = false;
            }
            return drive.launching;
        }
    }
    public class FirePurple implements Action {
        boolean init = true;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (init) {
                drive.runOuttake(false, false, true, false);
                init = false;
            }
            return drive.launching;
        }
    }
    public Action primeLaunch(){return new PrimeLaunch();}
    public Action cancelLaunch(){return new CancelLaunch();}
    public Action intake(){return new Intake();}
    public Action stopIntake(){return new StopIntake();}
    public Action fireGreen(){return new FireGreen();}
    public Action firePurple(){return new FirePurple();}
}
