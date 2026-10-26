package org.firstinspires.ftc.teamcode.robot_modules;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Module<T> {
    public abstract void postTelemetry(Telemetry telemetry);
    public static void postTelemetries(Module[] modules, Telemetry telemetry){
        for(Module i : modules){
            i.postTelemetry(telemetry);
        }
        telemetry.update();
    }
    public static void stopModules(Module[] modules){
        for(Module i : modules){
            if(i.getClass() == RunModule.class){
                ((RunModule)i).stop();
            }
        }
    }
}
