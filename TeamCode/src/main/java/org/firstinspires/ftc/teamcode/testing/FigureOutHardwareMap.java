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
            motorIndex = indexBounds(motorIndex, motors.length);
            servoIndex = indexBounds(servoIndex, servos.length);
            if(gamepad1.a){
                motors[motorIndex].setPower(1.0);
            } else if(gamepad1.b) {
                motors[motorIndex].setPower(-1.0);
            } else{
                motors[motorIndex].setPower(0);
            }
            if(gamepad2.a){
                servos[servoIndex].setPosition(0);
            }
            if(gamepad2.b){
                servos[servoIndex].setPosition(1.0);
            }
            telemetry.addData("Motor Index", motorIndex);
            telemetry.addData("Servo Index", servoIndex);
            telemetry.update();
        }}
    }
    private int indexBounds(int index, int max){
        if(index < 0){
            return max-1;
        }
        if(index >= max){
            return 0;
        }
        return index;
    }
}
