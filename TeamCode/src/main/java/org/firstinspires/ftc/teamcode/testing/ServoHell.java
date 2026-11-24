package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Testing Hell")
public class ServoHell extends LinearOpMode {
    Servo servo;
    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.get(Servo.class, "hood");
        waitForStart();
        while(opModeIsActive()){
            if(gamepad2.b){
                servo.setPosition(0.0);
            }
            if(gamepad2.a){
                servo.setPosition(1.0);
            }
        }
    }
}
