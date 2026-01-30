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

    private int[] getRGB(){
        return new int[]{colorSensor.red(), colorSensor.green(), colorSensor.blue()};
    }

    /**
     * Detects whether the sensor currently sees a GREEN, PURPLE (magenta), or NONE.
     * Uses HSV hue ranges with normalization and minimum brightness/saturation checks
     * to be robust to lighting and sensor scaling.
     */
    public States.Artifact isGreenOrPurple(){
        int[] rgb = getRGB();
        int r = rgb[0];
        int g = rgb[1];
        int b = rgb[2];

        int sum = r + g + b;
        int max = Math.max(r, Math.max(g, b));

        // Reject very dark readings (no object / ambient)
        if(sum < 60) return States.Artifact.NONE;

        // Normalize to 0..255 based on the max channel so hue calculation is brightness-invariant
        int rScaled = (int)(r * 255f / Math.max(1, max));
        int gScaled = (int)(g * 255f / Math.max(1, max));
        int bScaled = (int)(b * 255f / Math.max(1, max));

        float[] hsv = new float[3];
        android.graphics.Color.RGBToHSV(rScaled, gScaled, bScaled, hsv);
        float hue = hsv[0];   // 0..360
        float sat = hsv[1];   // 0..1
        float val = hsv[2];   // 0..1

        // Require a minimum saturation and brightness to avoid misclassifying very dim/gray readings
        if(val < 0.15 || sat < 0.25) return States.Artifact.NONE;

        // Hue ranges (tuned empirically): green ~ 60..150, purple/magenta ~ 250..330
        if(hue >= 60f && hue <= 150f) return States.Artifact.GREEN;
        if(hue >= 250f && hue <= 330f) return States.Artifact.PURPLE;

        return States.Artifact.NONE;
    }

    public void postTelemetry(Telemetry telemetry){
        int[] rgb = getRGB();
        int r = rgb[0], g = rgb[1], b = rgb[2];
        int max = Math.max(r, Math.max(g, b));
        int rScaled = (int)(r * 255f / Math.max(1, max));
        int gScaled = (int)(g * 255f / Math.max(1, max));
        int bScaled = (int)(b * 255f / Math.max(1, max));
        float[] hsv = new float[3];
        android.graphics.Color.RGBToHSV(rScaled, gScaled, bScaled, hsv);

        telemetry.addData("Raw R", r);
        telemetry.addData("Raw G", g);
        telemetry.addData("Raw B", b);
        telemetry.addData("Scaled R,G,B", "%d,%d,%d", rScaled, gScaled, bScaled);
        telemetry.addData("HSV", "%3.0f°, %1.2f, %1.2f", hsv[0], hsv[1], hsv[2]);
    }
}
