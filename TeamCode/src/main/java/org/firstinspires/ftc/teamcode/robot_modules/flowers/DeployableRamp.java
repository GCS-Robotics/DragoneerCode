package org.firstinspires.ftc.teamcode.robot_modules.flowers;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;

public class DeployableRamp extends RunModule<Boolean> {
    private Servo servo;
    private final double DOWN_POS = 1.0;
    private final double UP_POS = 0.0;
    public DeployableRamp(HardwareMap hardwareMap, String name){
        servo=hardwareMap.get(Servo.class, name);
    }

    @Override
    public void run(Boolean deployed) {
        if(deployed){
            servo.setPosition(DOWN_POS);
        } else{
            servo.setPosition(UP_POS);
        }
    }

    @Override
    public void stop() {
        servo.setPosition(UP_POS);
    }

    @Override
    public void postTelemetry(Telemetry telemetry) {
        boolean is_deployed = servo.getPosition() == DOWN_POS;
        telemetry.addData("Is Deployed?", is_deployed);
    }
}
