package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Four Motor Test", group = "Testing")
public class FourMotorTest extends LinearOpMode {

    DcMotor[] motors = new DcMotor[4];
    @Override
    public void runOpMode() throws InterruptedException {
        for(int i=0; i<motors.length; i++){
            motors[i] = hardwareMap.dcMotor.get("motor"+i);
        }
        waitForStart();
        while(opModeIsActive()){
            int[] speeds = new int[4];
            // Motor 1
            if(gamepad1.dpad_up){
                speeds[0] = 1;
            }
            if(gamepad1.dpad_down){
                speeds[0] = -1;
            }
            // Motor 2
            if(gamepad1.left_stick_y > 0.4){
                speeds[1] = 1;
            }
            if(gamepad1.left_stick_y < -0.4) {
                speeds[1] = -1;
            }
            // Motor 3
            if(gamepad1.right_stick_y > 0.4){
                speeds[2] = 1;
            }
            if(gamepad1.right_stick_y < -0.4) {
                speeds[2] = -1;
            }
            // Motor 4
            if(gamepad1.y){
                speeds[3] = 1;
            }
            if(gamepad1.a){
                speeds[3] = -1;
            }
            // Applying speeds to motors
            for(int i=0; i < motors.length; i++){
                motors[i].setPower(speeds[i]);
            }
        }
    }
}
