package org.firstinspires.ftc.teamcode.robot_modules.sensors;

import static org.firstinspires.ftc.teamcode.Colors.YELLOW;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Colors;
import org.firstinspires.ftc.teamcode.robot_modules.RunModule;

public class ColorSensing extends RunModule<Boolean> {
    private NormalizedColorSensor color;
    public ColorSensing(HardwareMap hardwareMap, String name){
        color = hardwareMap.get(NormalizedColorSensor.class, name);
    }
    @Override
    public void run(Boolean parameter) {
        getColor();
    }

    @Override
    public void stop() {

    }
    public Colors getColor() {
        // Code that figures out if it's red, blue, or yellow.

        return YELLOW;
    }
    @Override
    public void postTelemetry(Telemetry telemetry) {
        String color = "Yellow";
        switch (getColor()){
            case RED:
                color = "Red";
                break;
            case BLUE:
                color = "Blue";
                break;
        }
        telemetry.addData("Color", color);
    }
}
