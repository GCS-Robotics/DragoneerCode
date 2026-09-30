package org.firstinspires.ftc.teamcode.robot_modules;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Module<T> {
    public abstract void run(T parameter);
    public abstract void stop();
    public abstract void postTelemetry(Telemetry telemetry);
    public static void stopModules(Module[] modules){
        for(Module i : modules){
            i.stop();
        }
    }
    public static void postTelemetries(Module[] modules, Telemetry telemetry){
        for(Module i : modules){
            i.postTelemetry(telemetry);
        }
    }
}
