package org.firstinspires.ftc.teamcode.resources.base_function;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Color {
    ColorSensor colorSensor;
    public Color(HardwareMap hardwareMap){
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }
    private double[] getRGB(){
        return new double[]{colorSensor.red(), colorSensor.green(), colorSensor.blue()};
    }
    public int isGreenOrPurple(){
        double colors[] = getRGB();
        if(colors[1] > colors[2] && colors[1] > colors[0] * 2 && colors[1] > 150){
            return 0;
        }
        if(colors[2] > colors[1] * 1.2 && colors[2] > colors[0] * 1.5 && colors[2] > 150){
            return 1;
        }
        return -1;
    }
}
