package org.firstinspires.ftc.teamcode;

import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;

public class Tuning {
    @Tuner
    public static Procedure pinpointTuner() {
        return new PinpointTuner();
    }
}