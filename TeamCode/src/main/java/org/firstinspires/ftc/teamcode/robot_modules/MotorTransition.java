package org.firstinspires.ftc.teamcode.robot_modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MotorTransition extends Module<Boolean[]> {

    private DcMotor motor;

    public MotorTransition(HardwareMap hardwareMap, String name) {
        motor = hardwareMap.get(DcMotor.class, "transitionMotor");
    }

    @Override
    public void run(Boolean[] buttons) {
        if(buttons[0]){
            motor.setPower(1);
        } else if(buttons[1]){
            motor.setPower(-1);
        } else{
            motor.setPower(0);
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

