package org.firstinspires.ftc.teamcode.robot_modules;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Module<T> {
    public abstract void run(T parameter);
    public abstract void stop();
    public abstract void postTelemetry(Telemetry telemetry);
    public abstract void run(Gamepad gamepad);
}
