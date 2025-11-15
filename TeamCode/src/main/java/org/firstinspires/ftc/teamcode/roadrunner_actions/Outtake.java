package org.firstinspires.ftc.teamcode.roadrunner_actions;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Outtake {
    DcMotorEx launchLeft;
    DcMotorEx launchRight;
    Servo kicker;
    DcMotor drum;
    double launchSpeed;
    int[] balls;
    final int ROTATION_TICK = 537;
    int targetBall = -1;
    ColorSensor colorSensor;
    public int[] getBalls(){
        return balls;
    }
    public Outtake(HardwareMap hardwareMap, double lS, int[] b, int tB){
        launchLeft = hardwareMap.get(DcMotorEx.class, "launchLeft");
        launchRight = hardwareMap.get(DcMotorEx.class, "launchRight");
        kicker = hardwareMap.servo.get("kicker");
        drum = hardwareMap.dcMotor.get("drum");
        launchSpeed = lS;
        balls = b;
        targetBall = tB;
    }
    public class SpinUp implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                launchLeft.setPower(1);
                launchRight.setPower(1);
                initialized = true;
            }
            double velLeft = launchLeft.getVelocity();
            double velRight = launchRight.getVelocity();
            return velLeft < 10_000 && velRight < 10_000;
        }
    }
    public class CancelSpin implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                launchLeft.setPower(0);
                launchRight.setPower(0);
                initialized = true;
            }
            double velLeft = launchLeft.getVelocity();
            double velRight = launchRight.getVelocity();
            return velLeft > 10 && velRight > 10;

        }

    }
    public class fireBall implements Action {
        private boolean initialized = false;
        private boolean launching = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                int ballLocation = -1;
                for (int i = 0; i < balls.length; i++) {
                    if (balls[i] == targetBall) {
                        ballLocation = i;
                    }
                    if (ballLocation != -1) {
                        launching = true;
                        if (ballLocation == 0) {
                            drum.setTargetPosition(drum.getTargetPosition() + ROTATION_TICK / 3);
                            int temp = balls[0];
                            balls[0] = balls[2];
                            balls[2] = balls[1];
                            balls[1] = temp;
                        }
                        if (ballLocation == 2) {
                            drum.setTargetPosition(drum.getTargetPosition() + 2 * ROTATION_TICK / 3);
                            int temp = balls[2];
                            balls[2] = balls[0];
                            balls[0] = balls[1];
                            balls[1] = temp;
                        }
                    }
                }
            }
            if (launching && abs(drum.getTargetPosition() - drum.getCurrentPosition()) < ROTATION_TICK / 16) {
                kicker.setPosition(0.65);
                balls[1] = -1;
                return false;
            }
            return true;
        }
    }
}
