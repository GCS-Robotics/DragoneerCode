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
        drive = new MainDecodeDrive(hardwareMap, tel, dashTel, 1, 1000, 1, 0.01);
    }
    public RobotMechanisms(HardwareMap hardwareMap, Telemetry tel, Telemetry dashTel, int[] preload){
        this(hardwareMap, tel, dashTel);
        drive.setPreload(preload);
    }
    public class PrimeLaunch implements Action {
        boolean init = true;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(init){
                drive.runOuttake(true, false, false, false);
                init = false;
                return true;
            }
            drive.runDrum();
            drive.runOuttake(false, false, false, false);
            return !drive.isSpunUp();
        }
    }
    public class CancelLaunch implements Action {
        boolean init = true;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket){
            if(init){
                drive.runOuttake(false, true, false, false);
                init = false;
                return true;
            }
            drive.runDrum();
            drive.runOuttake(false, false, false, false);
            return false;
        }
    }
    public class Intake implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket){
            drive.runIntake(true, false);
            drive.runDrum();
            return true;
        }
    }
    public class StopIntake implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket){
            drive.runIntake(false, false);
            drive.runDrum();
            return false;
        }
    }
    public class FireGreen implements Action {
        boolean init = true;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if(init){
                drive.runOuttake(true, false, false, true);
                init = false;
                return true;
            }
            drive.runDrum();
            drive.runOuttake(false, false, false, false);
            return drive.isLaunching();
        }
    }
    public class FirePurple implements Action {
        boolean init = true;
        boolean started = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (init) {
                drive.runOuttake(true, false, true, false); // fire
                init = false;
                return true;
            }
            drive.runDrum();
            drive.runOuttake(false, false, false, false);
            if (!started) {
                started = true;
                return true;
            }

            return drive.isLaunching();
        }
    }

    public Action primeLaunch(){return new PrimeLaunch();}
    public Action cancelLaunch(){return new CancelLaunch();}
    public Action intake(){return new Intake();}
    public Action stopIntake(){return new StopIntake();}
    public Action fireGreen(){return new FireGreen();}
    public Action firePurple(){return new FirePurple();}
}
