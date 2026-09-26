package org.firstinspires.ftc.teamcode.robot_modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter extends Module<Float> {
    private DcMotorEx motor;
    public static double P=0, I=0, D=0, F=0;
    private static final double MOTOR_TICK_COUNT = 28;
    public static double TARGET_RPM = 2000;
    public Shooter(HardwareMap hardwareMap, String name){
        motor = hardwareMap.get(DcMotorEx.class, name);
        motor.setDirection(DcMotor.Direction.REVERSE);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    @Override
    public void run(Float target_rpm){
        TARGET_RPM = target_rpm;
        PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        double targetTicksPerSecond = TARGET_RPM * MOTOR_TICK_COUNT / 60;
        motor.setVelocity(targetTicksPerSecond);
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

    @Override
    public void run(Gamepad gamepad) {

    }

    public static void setPIDF(float p, float i, float d, float f){
        P=p;
        I=i;
        D=d;
        F=f;
    }
}
