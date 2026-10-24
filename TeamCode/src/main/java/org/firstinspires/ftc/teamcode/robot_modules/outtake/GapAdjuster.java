package org.firstinspires.ftc.teamcode.robot_modules.outtake;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot_modules.Module;

public class GapAdjuster extends Module<Float> {

    Servo gapServo;

    public GapAdjuster(HardwareMap hardwareMap, String name) {
        gapServo = hardwareMap.get(Servo.class, name);
    }

    @Override
    public void run(Float parameter) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void postTelemetry(Telemetry telemetry) {

    }
}
