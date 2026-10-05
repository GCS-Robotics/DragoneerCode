package org.firstinspires.ftc.teamcode.robot_modules;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter extends Module<Gamepad> {
    private DcMotorEx motor;
    public double P=0.002, I=0.25, D=0.005, F=0;
    private final double MOTOR_TICK_COUNT = 28;
    public double TARGET_RPM = 1000;
    public boolean revving = false;
    private PIDFController pidf;
    public Shooter(HardwareMap hardwareMap, String name){
        pidf = new PIDFController(P, I, D, F);
        motor = hardwareMap.get(DcMotorEx.class, name);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    @Override
    public void run(Gamepad gamepad){
        if(gamepad.dpadUpWasPressed()){
            TARGET_RPM = Math.min(TARGET_RPM+100,6000);
        }
        if(gamepad.dpadDownWasPressed()){
            TARGET_RPM = Math.max(TARGET_RPM-100, 0);
        }
        if(gamepad.aWasPressed()){
            revving = !revving;
        }
        double target = 0;
        if(revving){
            target = TARGET_RPM;
        }

        motor.setPower(pidf.calculate(motor.getVelocity(), target));
    }
    @Override
    public void stop(){
        motor.setPower(0);
    }
    @Override
    public void postTelemetry(Telemetry telemetry){
        double currentVelocityTicks = motor.getVelocity();
        double currentRPM = (currentVelocityTicks / MOTOR_TICK_COUNT) * 60;
        telemetry.addData("Target RPM", TARGET_RPM);
        telemetry.addData("Current RPM", "%.2f", currentRPM);
    }
}
