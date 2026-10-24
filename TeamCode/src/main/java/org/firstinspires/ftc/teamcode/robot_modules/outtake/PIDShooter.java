package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.Module;

public class PIDShooter extends Module<Boolean> {
    private double targetLaunchSpeed;
    Gamepad gamepad1;

    int Speed;
    final private double TICKS_PER_REV = 0;

    public double kP, kI, kD, kF;
    private double sumError = 0;
    private double lastError = 0;
    private double lastTime = 0;
    private DcMotorEx motor;
    public PIDShooter(HardwareMap hardwareMap, String name, double kP, double kI, double kD, double kF){
        motor = hardwareMap.get(DcMotorEx.class, name);
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }

    /**
     * Runs the shooter.
     *
     * @param parameter Whether we want the outtake spinning.
     * @return
     */


    @Override
    public double run(Boolean parameter) {
        motor.setPower(Speed);
        if(gamepad1.rightBumperWasReleased()) {
            Speed += 5;
        }
        return 2;
    }



    public double PIDFCalculate(double target, double current) {
        double currentTime = System.nanoTime() / 1e9;
        double deltaTime = (lastTime == 0) ? 0 : (currentTime - lastTime);

        double error = target - current;

        // Proportional
        double P = kP * error;

        // Integral
        if (deltaTime > 0) {
            sumError += error * deltaTime;
        }
        double I = kI * sumError;

        // Derivative
        double derivative = (deltaTime > 0) ? (error - lastError) / deltaTime : 0;
        double D = kD * derivative;

        // Feedforward
        double F = kF * target;

        // Store for next loop
        lastError = error;
        lastTime = currentTime;

        // Total output
        return P + I + D + F;
    }

    @Override
    public void stop() {
        sumError = 0;
        lastError = 0;
        lastTime = 0;
    }

    @Override
    public void postTelemetry(Telemetry telemetry) {
    }
    public double getTargetLaunchSpeed() {
        return targetLaunchSpeed;
    }
    public void setTargetLaunchSpeed(double targetLaunchSpeed) {
        this.targetLaunchSpeed = targetLaunchSpeed;
    }
}



