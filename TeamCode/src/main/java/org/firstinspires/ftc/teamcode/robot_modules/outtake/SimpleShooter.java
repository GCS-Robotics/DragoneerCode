package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;

public class SimpleShooter extends RunModule<Float> {
    DcMotor motor;
    public SimpleShooter(HardwareMap hardwareMap, String name){
        motor = hardwareMap.get(DcMotor.class, name);
    }
    @Override
    public void run(Float speed) {
        motor.setPower(speed);
    }
    @Override
    public void stop() {
        motor.setPower(0);
    }
    @Override
    public void postTelemetry(Telemetry telemetry) {

    }
}
