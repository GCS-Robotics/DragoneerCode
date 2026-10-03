package org.firstinspires.ftc.teamcode.robot_modules;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FlowerCollectors extends Module<Boolean>{
    private CRServo leftIntake, rightIntake;
    public FlowerCollectors(HardwareMap hardwareMap, String[] names){
        leftIntake = hardwareMap.get(CRServo.class, names[0]);
        rightIntake = hardwareMap.get(CRServo.class, names[1]);
        rightIntake.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    @Override
    public void run(Boolean running) {
        if(running) {
            leftIntake.setPower(1);
            rightIntake.setPower(1);
        } else{
            leftIntake.setPower(0);
            rightIntake.setPower(0);
        }
    }

    @Override
    public void stop() {
        leftIntake.setPower(0);
        rightIntake.setPower(0);
    }

    @Override
    public void postTelemetry(Telemetry telemetry) {

    }
}
