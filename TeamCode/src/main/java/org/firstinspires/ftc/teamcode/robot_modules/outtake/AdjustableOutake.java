package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.Module;

public class AdjustableOutake extends Module<Float> {
    Servo servoGap;
    Servo servoHood;
    Servo servoRotate;
    DcMotor flywheelMotor;

    public AdjustableOutake(HardwareMap hardwareMap, String sHood, String sRotate, String sGap, String fMotor) {
        flywheelMotor = hardwareMap.get(DcMotor.class, fMotor);
        servoHood = hardwareMap.get(Servo.class, sHood);
        servoRotate = hardwareMap.get(Servo.class, sRotate);
        servoGap = hardwareMap.get(Servo.class, sGap);
    }

    @Override
    public void run(Float parameter) {
        flywheelMotor.setPower(parameter);
    }

    @Override
    public void stop() {

    }

    @Override
    public void postTelemetry(Telemetry telemetry) {

    }
}
