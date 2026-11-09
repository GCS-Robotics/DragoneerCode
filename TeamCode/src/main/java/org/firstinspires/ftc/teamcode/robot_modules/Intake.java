package org.firstinspires.ftc.teamcode.robot_modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake extends RunModule<Boolean> {
    DcMotor motor;
    public Intake(HardwareMap hardwareMap, String name){
        motor = hardwareMap.get(DcMotor.class, name);
    }
    @Override
    public void run(Boolean running) {
        if (running) {
            motor.setPower(1);
        } else {
            stop();
        }
    }

    @Override
    public void stop() {
        motor.setPower(0);
    }

    @Override
    public void postTelemetry(Telemetry telemetry) {
    }
}
