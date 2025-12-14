package org.firstinspires.ftc.teamcode.resources.base_function;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Kicker extends Mechanism{
    public Servo kicker;
    private final double KICKED = 0.65;
    private final double NOT_KICKED = 0.35;
    public Kicker(HardwareMap hardwareMap){
        kicker = hardwareMap.get(Servo.class, "kicker");
    }
    public void kick(){
        kicker.setPosition(KICKED);
    }
    public void retract(){
        kicker.setPosition(NOT_KICKED);
    }
}
