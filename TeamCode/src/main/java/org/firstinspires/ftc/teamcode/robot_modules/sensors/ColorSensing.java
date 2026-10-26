package org.firstinspires.ftc.teamcode.robot_modules.sensors;

import static org.firstinspires.ftc.teamcode.Colors.BLUE;
import static org.firstinspires.ftc.teamcode.Colors.NONE;
import static org.firstinspires.ftc.teamcode.Colors.RED;
import static org.firstinspires.ftc.teamcode.Colors.YELLOW;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Colors;
import org.firstinspires.ftc.teamcode.robot_modules.Module;

public class ColorSensing extends Module<Boolean> {
    private NormalizedColorSensor color;
    private final double RED_THRESHOLD = 0.62;
    private final double BLUE_THRESHOLD = 0.62;
    private final double GREEN_THRESHOLD = 0.62;
    public ColorSensing(HardwareMap hardwareMap, String name){
        color = hardwareMap.get(NormalizedColorSensor.class, name);
    }
    public Colors getColor() {
        // Code that figures out if it's red, blue, or yellow.
        NormalizedRGBA rgb = color.getNormalizedColors();
        if(color_test(rgb, true, false, false)){
            return RED;
        } else if(color_test(rgb, false, true, false)){
            return BLUE;
        } else if(color_test(rgb, true, false, true)){
            return YELLOW;
        }
        return NONE;
    }
    private boolean color_test(NormalizedRGBA rgba, boolean red, boolean blue, boolean green){
        boolean red_good = red ? (rgba.red > RED_THRESHOLD) : (rgba.red < RED_THRESHOLD);
        boolean blue_good = blue ? (rgba.blue > BLUE_THRESHOLD) : (rgba.blue < BLUE_THRESHOLD);
        boolean green_good = green ? (rgba.green > GREEN_THRESHOLD) : (rgba.green < GREEN_THRESHOLD);
        return red_good && blue_good && green_good;
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
