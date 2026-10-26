package org.firstinspires.ftc.teamcode.robot_modules;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class RunModule<T> extends Module<T> {
    public abstract void run(T parameter);
    public abstract void stop();

}
