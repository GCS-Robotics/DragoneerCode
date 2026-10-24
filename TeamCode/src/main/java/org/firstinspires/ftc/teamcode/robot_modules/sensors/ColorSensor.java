package org.firstinspires.ftc.teamcode.robot_modules.sensors;

import static org.firstinspires.ftc.teamcode.Colors.YELLOW;
import static org.firstinspires.ftc.teamcode.Colors.RED;
import static org.firstinspires.ftc.teamcode.Colors.BLUE;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Colors;
import org.firstinspires.ftc.teamcode.robot_modules.Module;

public class ColorSensor extends Module<Boolean> {
    private ColorSensor color;
    public ColorSensor(HardwareMap hardwareMap, String name){
        color = hardwareMap.get(ColorSensor.class, name);
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
