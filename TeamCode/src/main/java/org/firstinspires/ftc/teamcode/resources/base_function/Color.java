package org.firstinspires.ftc.teamcode.resources.base_function;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Color {
    ColorSensor colorSensor;
    public Color(HardwareMap hardwareMap){
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }
    private double[] getRGB(){
        return new double[]{colorSensor.red(), colorSensor.green(), colorSensor.blue()};
    }
    public int isGreenOrPurple(){
        double[] rgb = getRGB();
        //if green > blue and green > red*2, and green > 150, return 0
        if(rgb[1] > rgb[2] && rgb[1] > rgb[0] && rgb[1] > 150){
            return 0;
        }
        //if blue > green * 1.2 and blue > red * 1.5 and blue > 150
        if(rgb[2] > rgb[1] * 1.2 && rgb[2] > rgb[0] * 1.5 && rgb[2] > 150){
            return 1;
        }
        return -1;
    }
    public void postTelemetry(Telemetry telemetry){
        telemetry.addData("Red", getRGB()[0]);
        telemetry.addData("Green", getRGB()[1]);
        telemetry.addData("Blue", getRGB()[2]);
    }
}
