package org.firstinspires.ftc.teamcode.testing;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.utils.Timer;

@TeleOp
public class PedroExampleAuto extends OpMode {

    private Follower follower;
    private Timer timer;

    public enum pathState {
        DRIVE1,
        DRIVE2
    }



    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
