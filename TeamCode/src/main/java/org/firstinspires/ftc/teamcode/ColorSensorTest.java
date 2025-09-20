package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Color Sensing")
public class ColorSensorTest extends LinearOpMode {

    ColorSensor color;

    @Override
    public void runOpMode() {
        // Get the color sensor from hardwareMap
        color = hardwareMap.get(ColorSensor.class, "Color");

        final byte errorRange = 50;

        int red;
        int green;
        int blue;

        // Wait for the Play button to be pressed
        waitForStart();

        // While the Op Mode is running, update the telemetry values.
        while (opModeIsActive()) {
            red = color.red();
            green = color.green();
            blue = color.blue();
            if((green > blue+(errorRange*2)) && (green > red+(errorRange*2))) {
                telemetry.addData("Color", "Green");
            }
            else if((blue-red == errorRange)) {
                telemetry.addData("Color", "purple");
            }
            telemetry.addData("Red", color.red());
            telemetry.addData("Green", color.green());
            telemetry.addData("Blue", color.blue());
            telemetry.update();
        }
    }
}