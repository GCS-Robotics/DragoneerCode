package org.firstinspires.ftc.teamcode.robot_modules;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ServoTransition extends Module<Boolean[]>{
    private CRServo transition;
    public ServoTransition(HardwareMap hardwareMap, String name){
        transition = hardwareMap.get(CRServo.class, "transition");
    }
    @Override
    public void run(Boolean servoButtons[]) {
        if(servoButtons[0]){
            transition.setPower(1);
        } else if(servoButtons[1]){
            transition.setPower(-1);
        } else{
            transition.setPower(0);
        }
    }
    @Override
    public void stop() {
        transition.setPower(0);
    }
    @Override
    public void postTelemetry(Telemetry telemetry) {

    }
}
