package org.firstinspires.ftc.teamcode.resources.base_function;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.resources.States;

public class Color {
    ColorSensor colorSensor;
    public Color(HardwareMap hardwareMap){
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }
    private double[] getRGB(){
        return new double[]{colorSensor.red(), colorSensor.green(), colorSensor.blue()};
    }
    public States.Artifact isGreenOrPurple(){
        double[] rgb = getRGB();
        if(rgb[1] > rgb[2] && rgb[1] > rgb[0] && rgb[1] > 500){
            return States.Artifact.GREEN;
        }
        if(rgb[2] > rgb[1] * 1.2 && rgb[2] > rgb[0] * 1.5 && rgb[2] > 500){
            return States.Artifact.PURPLE;
        }
        return States.Artifact.NONE;
    }
    public void postTelemetry(Telemetry telemetry){
        telemetry.addData("Red", getRGB()[0]);
        telemetry.addData("Green", getRGB()[1]);
        telemetry.addData("Blue", getRGB()[2]);
    }
}
