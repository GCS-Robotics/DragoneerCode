package org.firstinspires.ftc.teamcode.resources;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class RobotMechanisms {
    private DcMotor launcherLeft;
    private DcMotor launcherRight;
    private DcMotor drumRotor;
    private DcMotor intake;
    private Servo kicker;
    private ColorSensor colorSensor;
    private final int ROTATION_TICK;
    private int[] balls;
    private boolean runIntake = false;
    public RobotMechanisms(HardwareMap hardwareMap, int rotationTick){
        launcherLeft = hardwareMap.dcMotor.get("launcherLeft");
        launcherRight = hardwareMap.dcMotor.get("launcherRight");
        drumRotor = hardwareMap.dcMotor.get("drumRotor");
        intake = hardwareMap.dcMotor.get("intake");
        kicker = hardwareMap.servo.get("kicker");
        colorSensor = hardwareMap.colorSensor.get("BallColor");
        ROTATION_TICK = rotationTick;
        balls = new int[3];
    }
    public class SpinUp implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                launcherLeft.setPower(0.8);
                launcherRight.setPower(0.8);
                initialized = true;
            }
            return true;
        }
    }
    public class RunIntake implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                intake.setPower(1);
                initialized = true;
            }
            int drumPos = drumRotor.getTargetPosition();
            if (drumPos % (ROTATION_TICK / 3) <= ROTATION_TICK / 7) {
                drumRotor.setTargetPosition((int) (drumPos / (ROTATION_TICK / 3)) * (ROTATION_TICK / 3) + ROTATION_TICK / 6);
            }
            int ball = isGreenOrPurple();
            if(ball!=-1
                    && drumRotor.getCurrentPosition() >= drumRotor.getTargetPosition()-ROTATION_TICK/16) {
                drumRotor.setTargetPosition(drumPos+ROTATION_TICK/3);
                // Shift over everything in ball storage
                balls[0] = balls[2];
                balls[2] = balls[1];
                // Adds our new ball
                balls[1] = ball;
            }
            return true;
        }
    }
    public class StopIntake implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                intake.setPower(0);
                initialized = true;
            }
            return true;
        }
    }
    public class StopOuttake implements Action{
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if(!initialized){
                launcherLeft.setPower(0);
                launcherRight.setPower(0);
                initialized=true;
            }
            return true;
        }
    }
    public Action spinUp(){
        return new SpinUp();
    }
    public Action runIntake(){
        return new RunIntake();
    }
    public Action stopIntake(){
        return new StopIntake();
    }
    public Action stopOuttake(){
        return new StopOuttake();
    }

    private double[] getColor() {
        double red = colorSensor.red();
        double green = colorSensor.green();
        double blue = colorSensor.blue();
        return new double[]{red, green, blue};
    }
    private int isGreenOrPurple() {
        double[] colors = getColor();
        if (colors[1] > colors[2] * 1.5 && colors[1] > colors[0] * 2.0) {
            return 0;
        }
        if (colors[2] > colors[1] * 1.2 && colors[0] > colors[1] * 1.2) {
            return 1;
        }
        return -1;
    }
}
