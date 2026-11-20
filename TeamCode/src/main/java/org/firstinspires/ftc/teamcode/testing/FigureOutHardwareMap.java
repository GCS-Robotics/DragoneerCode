package org.firstinspires.ftc.teamcode.testing;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Figure Out Hardware Map", group = "Testing")
public class FigureOutHardwareMap extends LinearOpMode {
    DcMotor[] motors = new DcMotor[8];
    Servo[] servos = new Servo[4];
    int motorIndex = 0;
    int servoIndex = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        for(int i=0; i<motors.length; i++){
            motors[i] = hardwareMap.get(DcMotor.class, "motor"+(i+1));
        }
        for(int i=0; i<servos.length; i++){
            servos[i] = hardwareMap.get(Servo.class, "servo"+(i+1));
        }
        waitForStart();
        if(opModeIsActive()){while(opModeIsActive()){
            if(gamepad1.dpadLeftWasPressed()){
                motors[motorIndex].setPower(0);
                motorIndex -= 1;
            } else if (gamepad1.dpadRightWasPressed()){
                motorIndex += 1;
            }
            if(gamepad2.dpadLeftWasPressed()){
                servoIndex -= 1;
            } else if (gamepad2.dpadRightWasPressed()){
                servoIndex += 1;
            }
            if(motorIndex < 0){
                motorIndex = motors.length-1;
            } else if(motorIndex >= motors.length){
                motorIndex = 0;
            }
            if(servoIndex < 0){
                servoIndex = servos.length-1;
            } else if(servoIndex >= servos.length){
                servoIndex = 0;
            }
            if(gamepad1.a){
                motors[motorIndex].setPower(0.1);
            } else{
                motors[motorIndex].setPower(0);
            }
            if(gamepad2.a){
                servos[servoIndex].setPosition(0);
            }
            if(gamepad2.b){
                servos[servoIndex].setPosition(0.1);
            }
            telemetry.addData("Motor Index", motorIndex);
            telemetry.addData("Servo Index", servoIndex);
            telemetry.update();
        }}
    }
}
