package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.resources.base_function.Drum;

@TeleOp(name = "Prepare Drum", group = "Main Drive")
public class PrepareDrumPosition extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Drum drum = new Drum(hardwareMap, 1, new int[]{-1, -1, -1});
        waitForStart();
        while(opModeIsActive()){
            if(gamepad2.leftBumperWasPressed()){
                drum.rotateSixth();
            }
            if(gamepad2.rightBumperWasPressed()){
                drum.rotateThird();
            }
            drum.run(true);
        }
    }
}
